@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.Server
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.routes.toRoutes
import io.github.nomisrev.openapi.transformers.topLevelNames
import io.ktor.http.HttpMethod
import kotlin.collections.flatMapTo

data class ApiTree(
    val name: String,
    val operations: Map<HttpMethod, Route>,
    val children: List<PathNode>,
    val models: List<Model> = emptyList(),
    val servers: List<Server> = emptyList(),
)

data class PathNode(
    val segment: PathSegment,
    val operations: Map<HttpMethod, Route>,
    val children: List<PathNode>,
)

fun Iterable<Route>.buildTree(
    name: String,
    models: List<Model> = emptyList(),
    servers: List<Server> = emptyList(),
): ApiTree {
    val routes = validatedConcreteRoutes()
    val rootBuilder = PathNodeBuilder(segment = null)

    for (route in routes) {
        rootBuilder.insert(route.segments, route)
    }

    return ApiTree(
        name = name,
        operations = rootBuilder.operations,
        children = rootBuilder.children.map { it.build() },
        models = models,
        servers = servers,
    )
}

private tailrec suspend fun Set<NamingContext.Reference>.topLevelModels(registry: Registry): List<Model> {
    val models = with(registry) { map { it.toModel() } }
    val newNames = models.flatMapTo(mutableSetOf()) { it.topLevelNames() }
    return if (newNames == this) models else newNames.topLevelModels(registry)
}

suspend fun OpenAPI.toApiTree(
    name: String = info.title.toPascalCase(),
    preprocessor: OpenApiPreprocessor = OpenApiPreprocessor.None,
    engine: SchemaTransformerEngine = SchemaTransformerEngine.default(),
): ApiTree {
    val preprocessed = preprocessedBy(preprocessor)
    return Registry(preprocessed, engine).use { registry ->
        val globalServers = preprocessed.servers.normalizeForClientGeneration()
        val routes = with(registry) { preprocessed.toRoutes() }
        val models = with(registry) {
            routes
                .flatMapTo(mutableSetOf()) { it.topLevelNames() }
                .topLevelModels(registry)
        }
        routes.buildTree(name = name, models = models, servers = globalServers)
    }
}

private fun Route.topLevelNames(): Set<NamingContext.Reference> =
    parameters.topLevelNames() + returns.topLevelNames() + body.topLevelNames()

private fun Route.Bodies?.topLevelNames(): Set<NamingContext.Reference> =
    this?.types.orEmpty().flatMapTo(mutableSetOf()) { (_, body) ->
        when (body) {
            is Route.Body.Multipart.Value -> body.parameters.flatMap { it.type.topLevelNames() }
            is Route.Body.Multipart.Ref -> body.value.topLevelNames()
            is Route.Body.FormUrlEncoded -> body.parameters.flatMap { it.type.topLevelNames() }
            is Route.Body.SetBody -> body.type.topLevelNames()
            is Route.Body.OverloadedBody -> body.type.topLevelNames()
        }
    }

private fun Route.Returns.topLevelNames(): Set<NamingContext.Reference> {
    val defaultNested = default?.types?.values.orEmpty().flatMapTo(mutableSetOf()) { it.topLevelNames() }
    val responsesNested = responses.values.flatMapTo(mutableSetOf()) { returnType ->
        returnType.types.values.flatMap { it.topLevelNames() }
    }
    return defaultNested + responsesNested
}

private fun List<Route.Input>.topLevelNames(): Set<NamingContext.Reference> =
    flatMapTo(mutableSetOf()) { it.type.topLevelNames() }

private fun List<Server>.normalizeForClientGeneration(): List<Server> {
    if (size != 1) return this
    val only = first()
    val isImplicitDefault = only.url == "/" &&
        only.description == null &&
        only.variables.isNullOrEmpty() &&
        only.extensions.isNullOrEmpty()
    return if (isImplicitDefault) emptyList() else this
}

private class PathNodeBuilder(
    var segment: PathSegment?,
    val sourceRoute: Route? = null,
    val operations: MutableMap<HttpMethod, Route> = mutableMapOf(),
    val children: MutableList<PathNodeBuilder> = mutableListOf(),
) {
    fun insert(
        segments: List<PathSegment>,
        route: Route,
        parentSegments: List<PathSegment> = emptyList(),
    ) {
        if (segments.isEmpty()) {
            operations[route.method] = route
            return
        }

        val head = segments.first()
        val tail = segments.drop(1)
        val existing = children.firstOrNull { it.segment.sameIdentityAs(head) }
        if (existing != null) {
            val existingSegment = requireNotNull(existing.segment)
            existingSegment.requireCompatibleWith(
                other = head,
                nodePath = parentSegments + existingSegment,
                existingRoute = requireNotNull(existing.sourceRoute),
                incomingRoute = route,
            )
            existing.segment = existingSegment.preferredRepresentation(head)
            existing.insert(tail, route, parentSegments + existingSegment)
        } else {
            val child = PathNodeBuilder(head, sourceRoute = route)
            children.add(child)
            child.insert(tail, route, parentSegments + head)
        }
    }

    fun build(): PathNode = PathNode(
        segment = requireNotNull(segment) { "Empty segment for root" },
        operations = operations.toMap(),
        children = children.map { it.build() },
    )
}

/**
 * Normalizes a raw path segment string so that hyphen-separated names (used in literal URL
 * segments like "secret-scanning") and underscore-separated names (used in OpenAPI enum values
 * like "secret_scanning") are treated as equivalent for identity and compatibility purposes.
 */
private fun String.normalizeSegmentSeparators(): String = replace('-', '_')

private fun PathSegment?.sameIdentityAs(other: PathSegment): Boolean = when {
    this is PathSegment.Literal && other is PathSegment.Literal ->
        name.normalizeSegmentSeparators() == other.name.normalizeSegmentSeparators()
    this is PathSegment.Literal && other is PathSegment.FixedValue ->
        name.normalizeSegmentSeparators() == other.wireValue.normalizeSegmentSeparators()
    this is PathSegment.Parameter && other is PathSegment.Parameter -> name == other.name
    this is PathSegment.Parameter && other is PathSegment.OverloadedParameter -> name == other.name
    this is PathSegment.OverloadedParameter && other is PathSegment.Parameter -> name == other.name
    this is PathSegment.OverloadedParameter && other is PathSegment.OverloadedParameter -> name == other.name
    this is PathSegment.FixedValue && other is PathSegment.Literal ->
        wireValue.normalizeSegmentSeparators() == other.name.normalizeSegmentSeparators()
    this is PathSegment.FixedValue && other is PathSegment.FixedValue ->
        wireValue.normalizeSegmentSeparators() == other.wireValue.normalizeSegmentSeparators()
    else -> false
}

private fun PathSegment.preferredRepresentation(other: PathSegment): PathSegment = when {
    this is PathSegment.FixedValue -> this
    other is PathSegment.FixedValue -> other
    else -> this
}

private val SharedPathNodeNamingContext = NamingContext.reference("Shared", SchemaContext.Null)

private fun PathSegment.requireCompatibleWith(
    other: PathSegment,
    nodePath: List<PathSegment>,
    existingRoute: Route,
    incomingRoute: Route,
) {
    if (normalizedForCompatibility() == other.normalizedForCompatibility()) return

    val conflicts = listOf(
        existingRoute to compatibilityDescription(),
        incomingRoute to other.compatibilityDescription(),
    ).sortedBy { (route, _) -> route.descriptor() }

    val routes = conflicts.joinToString(separator = "; ") { (route, segment) ->
        "${route.descriptor()} -> $segment"
    }
    throw IllegalArgumentException(
        "Conflicting shared path parameter node '${nodePath.toPathTemplate()}': " +
            "$routes. Shared path nodes must resolve to the same segment shape."
    )
}

private fun Route.descriptor(): String = "${method.value} $path"

private fun Iterable<Route>.validatedConcreteRoutes(): List<Route> {
    val routes = toList()
    if (routes.isEmpty()) return routes

    val duplicatesByDescriptor = routes.groupBy { route -> route.descriptor() }
    for (descriptor in duplicatesByDescriptor.keys.sorted()) {
        val duplicates = duplicatesByDescriptor.getValue(descriptor)
        val variants = duplicates
            .map(Route::normalizedForConcreteCollision)
            .distinct()
        if (variants.size > 1) {
            val details = variants
                .map(Route::concreteCollisionDescription)
                .sorted()
                .joinToString(separator = "; ")
            throw IllegalArgumentException(
                "Conflicting concrete route '$descriptor': $details"
            )
        }
    }

    val seen = mutableSetOf<String>()
    return routes.filter { route ->
        seen.add(route.descriptor())
    }
}

private fun List<PathSegment>.toPathTemplate(): String {
    val path = joinToString(separator = "/") { segment ->
        when (segment) {
            is PathSegment.FixedValue -> segment.wireValue
            is PathSegment.Literal -> segment.name
            is PathSegment.Parameter -> "{${segment.name}}"
            is PathSegment.OverloadedParameter -> "{${segment.name}}"
        }
    }
    return if (path.isEmpty()) "/" else "/$path"
}

private fun PathSegment.normalizedForCompatibility(): PathSegment = when (this) {
    is PathSegment.FixedValue -> PathSegment.Literal(wireValue.normalizeSegmentSeparators())
    is PathSegment.Literal -> PathSegment.Literal(name.normalizeSegmentSeparators())
    is PathSegment.Parameter -> copy(model = model.normalizedForCompatibility())
    is PathSegment.OverloadedParameter -> copy(type = type.normalizedForCompatibility() as Model.Union)
}

private fun Route.normalizedForConcreteCollision(): Route = copy(
    summary = null,
    segments = segments.map(PathSegment::normalizedForCompatibility),
    body = body.normalizedForConcreteCollision(),
    parameters = parameters.map(Route.Input::normalizedForConcreteCollision),
    returns = returns.normalizedForConcreteCollision(),
    extensions = emptyMap(),
)

private fun Route.Input.normalizedForConcreteCollision(): Route.Input =
    copy(type = type.normalizedForCompatibility(), description = null)

private fun Route.Bodies?.normalizedForConcreteCollision(): Route.Bodies? =
    this?.copy(
        types = types.mapValues { (_, body) -> body.normalizedForConcreteCollision() },
        extensions = emptyMap(),
    )

private fun Route.Body.normalizedForConcreteCollision(): Route.Body = when (this) {
    is Route.Body.SetBody -> copy(
        type = type.normalizedForCompatibility(),
        description = null,
        extensions = emptyMap(),
    )

    is Route.Body.OverloadedBody -> copy(
        type = type.normalizedForCompatibility() as Model.Union,
        description = null,
        extensions = emptyMap(),
    )

    is Route.Body.FormUrlEncoded -> copy(
        parameters = parameters.map { it.copy(type = it.type.normalizedForCompatibility()) },
        description = null,
        extensions = emptyMap(),
    )

    is Route.Body.Multipart.Value -> copy(
        parameters = parameters.map { it.copy(type = it.type.normalizedForCompatibility()) },
        description = null,
        extensions = emptyMap(),
    )

    is Route.Body.Multipart.Ref -> copy(
        value = value.normalizedForCompatibility(),
        description = null,
        extensions = emptyMap(),
    )
}

private fun Route.Returns.normalizedForConcreteCollision(): Route.Returns = copy(
    default = default?.normalizedForConcreteCollision(),
    responses = responses.mapValues { (_, value) -> value.normalizedForConcreteCollision() },
    extensions = emptyMap(),
)

private fun Route.ReturnType.normalizedForConcreteCollision(): Route.ReturnType = copy(
    types = types.mapValues { (_, model) -> model.normalizedForCompatibility() },
    extensions = emptyMap(),
)

private fun Route.concreteCollisionDescription(): String = buildString {
    append("parameters=")
    append(parameters.joinToString(prefix = "[", postfix = "]") { input ->
        "${input.input.name.lowercase()}:${input.name}:${input.type.compatibilityDescription()}"
    })
    append(", body=")
    append(body?.types?.entries?.joinToString(prefix = "[", postfix = "]") { (contentType, body) ->
        "$contentType:${body.concreteCollisionDescription()}"
    } ?: "null")
    append(", returns=")
    append(returns.concreteCollisionDescription())
    append(", deprecated=")
    append(deprecated)
}

private fun Route.Body.concreteCollisionDescription(): String = when (this) {
    is Route.Body.SetBody -> "SetBody(${type.compatibilityDescription()})"
    is Route.Body.OverloadedBody -> "OverloadedBody(${type.compatibilityDescription()})"
    is Route.Body.FormUrlEncoded ->
        "FormUrlEncoded(${parameters.joinToString { "${it.name}:${it.type.compatibilityDescription()}" }})"

    is Route.Body.Multipart.Value ->
        "MultipartValue(${parameters.joinToString { "${it.name}:${it.type.compatibilityDescription()}" }})"

    is Route.Body.Multipart.Ref -> "MultipartRef(${value.compatibilityDescription()})"
}

private fun Route.Returns.concreteCollisionDescription(): String = buildString {
    append("default=")
    append(default?.concreteCollisionDescription() ?: "null")
    append(", responses=")
    append(
        responses.entries
            .sortedBy { it.key.value }
            .joinToString(prefix = "[", postfix = "]") { (status, response) ->
                "${status.value}:${response.concreteCollisionDescription()}"
            }
    )
}

private fun Route.ReturnType.concreteCollisionDescription(): String =
    types.entries
        .sortedBy { it.key.toString() }
        .joinToString(prefix = "[", postfix = "]") { (contentType, model) ->
            "$contentType:${model.compatibilityDescription()}"
        }

private fun Model.normalizedForCompatibility(): Model = when (this) {
    is Model.Collection -> normalizedForCompatibility()
    is Model.DiscriminatedObject -> normalizedForCompatibility()
    is Model.Enum -> normalizedForCompatibility()
    is Model.Object -> normalizedForCompatibility()
    is Model.OneOf -> normalizedForCompatibility()
    is Model.AnyOf -> normalizedForCompatibility()

    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Primitive.Boolean,
    is Model.Primitive.Double,
    is Model.Primitive.Float,
    is Model.Primitive.Int,
    is Model.Primitive.Long,
    is Model.Primitive.String,
    is Model.Primitive.Unit,
    is Model.Reference,
    is Model.Uuid -> clearedCompatibilityMetadata()
}

private fun Model.Collection.normalizedForCompatibility(): Model = copy(
    inner = inner.normalizedForCompatibility(),
    description = null,
    title = null,
)

private fun Model.DiscriminatedObject.normalizedForCompatibility(): Model = copy(
    context = SharedPathNodeNamingContext,
    abstractProperties = abstractProperties.mapValues { (_, property) ->
        property.copy(model = property.model.normalizedForCompatibility())
    },
    subtypes = subtypes.map { subtype ->
        subtype.copy(
            context = SharedPathNodeNamingContext,
            description = null,
            title = null,
            properties = subtype.properties.mapValues { (_, property) ->
                property.copy(model = property.model.normalizedForCompatibility())
            },
            additionalProperties = subtype.additionalProperties.normalizedForCompatibility(),
        )
    },
    description = null,
    title = null,
)

private fun Model.Enum.normalizedForCompatibility(): Model = copy(
    context = SharedPathNodeNamingContext,
    inner = inner.normalizedForCompatibility(),
    description = null,
    title = null,
)

private fun Model.Object.normalizedForCompatibility(): Model = copy(
    context = SharedPathNodeNamingContext,
    description = null,
    title = null,
    properties = properties.mapValues { (_, property) ->
        property.copy(model = property.model.normalizedForCompatibility())
    },
    additionalProperties = additionalProperties.normalizedForCompatibility(),
)

private fun Model.OneOf.normalizedForCompatibility(): Model = copy(
    context = SharedPathNodeNamingContext,
    cases = cases.map { case ->
        case.copy(model = case.model.normalizedForCompatibility())
    },
    description = null,
    title = null,
)

private fun Model.AnyOf.normalizedForCompatibility(): Model = copy(
    context = SharedPathNodeNamingContext,
    cases = cases.map { case ->
        case.copy(model = case.model.normalizedForCompatibility())
    },
    description = null,
    title = null,
)

private fun Model.clearedCompatibilityMetadata(): Model =
    if (this is Model.ByteArray) copy(description = null, title = null)
    else if (this is Model.Date) copy(description = null, title = null)
    else if (this is Model.DateTime) copy(description = null, title = null)
    else if (this is Model.FreeFormJson) copy(description = null, title = null)
    else if (this is Model.Primitive.Boolean) copy(description = null, title = null)
    else if (this is Model.Primitive.Double) copy(description = null, title = null)
    else if (this is Model.Primitive.Float) copy(description = null, title = null)
    else if (this is Model.Primitive.Int) copy(description = null, title = null)
    else if (this is Model.Primitive.Long) copy(description = null, title = null)
    else if (this is Model.Primitive.String) copy(description = null, title = null)
    else if (this is Model.Primitive.Unit) copy(description = null, title = null)
    else if (this is Model.Reference) copy(
        context = SharedPathNodeNamingContext,
        description = null,
        title = null,
    )
    else if (this is Model.Uuid) copy(description = null, title = null)
    else error("Unsupported model: $this")

private fun Model.Object.AdditionalProperties.normalizedForCompatibility(): Model.Object.AdditionalProperties =
    when (this) {
        is Model.Object.AdditionalProperties.Allowed -> this
        is Model.Object.AdditionalProperties.Schema ->
            Model.Object.AdditionalProperties.Schema(value.normalizedForCompatibility())
    }

private fun PathSegment.compatibilityDescription(): String = when (val segment = normalizedForCompatibility()) {
    is PathSegment.FixedValue -> "FixedValue(wireValue=${segment.wireValue})"
    is PathSegment.Literal -> "Literal(name=${segment.name})"
    is PathSegment.Parameter -> "Parameter(name=${segment.name}, model=${segment.model.compatibilityDescription()})"
    is PathSegment.OverloadedParameter ->
        "OverloadedParameter(name=${segment.name}, type=${segment.type.compatibilityDescription()})"
}

@Suppress("CyclomaticComplexMethod")
private fun Model.compatibilityDescription(): String {
    val suffix = if (isNullable) "?" else ""
    return when (this) {
        is Model.ByteArray -> "ByteArray$suffix"
        is Model.Collection -> "List<${inner.compatibilityDescription()}>$suffix"
        is Model.Date -> "Date$suffix"
        is Model.DateTime -> "DateTime$suffix"
        is Model.DiscriminatedObject -> "DiscriminatedObject(discriminator=$discriminator)$suffix"
        is Model.Enum -> "Enum(values=${values.compatibilityDescription()})$suffix"
        is Model.FreeFormJson -> "FreeFormJson$suffix"
        is Model.Object -> {
            val propertiesDescription = properties.entries
                .sortedBy { it.key }
                .joinToString(prefix = "[", postfix = "]") { (name, property) ->
                    "$name:${property.model.compatibilityDescription()}"
                }
            val kind = if (isScalarWrapper) "ScalarWrapper" else "Object"
            "$kind(properties=$propertiesDescription)$suffix"
        }
        is Model.Primitive.Boolean -> "Boolean$suffix"
        is Model.Primitive.Double -> "Double$suffix"
        is Model.Primitive.Float -> "Float$suffix"
        is Model.Primitive.Int -> "Int$suffix"
        is Model.Primitive.Long -> "Long$suffix"
        is Model.Primitive.String -> "String$suffix"
        is Model.Primitive.Unit -> "Unit$suffix"
        is Model.Reference -> "Reference$suffix"
        is Model.OneOf -> "OneOf(cases=${cases.map { it.model.compatibilityDescription() }})$suffix"
        is Model.AnyOf -> "AnyOf(cases=${cases.map { it.model.compatibilityDescription() }})$suffix"
        is Model.Uuid -> "Uuid$suffix"
    }
}

private fun List<Model.EnumValue>.compatibilityDescription(): String =
    joinToString(prefix = "[", postfix = "]")
