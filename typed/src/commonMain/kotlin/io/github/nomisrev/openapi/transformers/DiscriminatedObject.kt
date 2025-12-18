package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.registry.peek
import kotlinx.coroutines.flow.MutableStateFlow

typealias AtomicStateFlow<A> = MutableStateFlow<A>


context(scope: Registry.Scope)
suspend fun ResolvedSchema.toDiscriminatedObject(context: SchemaContext): Model.DiscriminatedObject {
    val discriminator = requireNotNull(schema.discriminator) { "Discriminator required for discriminated object" }
    val mapping = requireNotNull(discriminator.mapping) { "Discriminator mapping required" }
    require(this is ResolvedSchema.Reference) { "Expected reference for discriminated object" }

    val mappings = mapping.filterValues { it != "#/components/schemas/${reference.name}" }

    val abstractProperties = properties(schema.properties!!, context)
        .filter { it.value.model !is Model.FreeFormJson }

    val subtypes = mappings.map { (mappedName, ref) ->
        val name = name.nest(NamingContext.DiscriminatedObjectCase(mappedName))
        val subtypeSchema = ReferenceOr.Reference(ref).peek()
        val allOf = requireNotNull(subtypeSchema.allOf) { "Subtype schema must be allOf" }

        val (superRequired, superProps) =
            collectSuperTypeProperties(Pair(schema.required, schema.properties.orEmpty()), allOf, mapping)

        val peeked =
            (allOf - allOf.single { refOrSchema -> refOrSchema.isSubtype(mapping) }).singleOrNull()?.peek() ?: Schema()
        val single = peeked.properties.orEmpty()
        val properties = buildMap {
            putAll(superProps)
            single.forEach { (name, refOrSchema) ->
                if (containsKey(name)) put(name, ReferenceOr.value(getValue(name).peek().merge(refOrSchema.peek())))
                else put(name, refOrSchema)
            }
        }

        ResolvedSchema.Value(
            name,
            peeked.copy(
                properties = properties,
                required = superRequired + peeked.required
            )
        ).toObject(context, properties)

//        Model.DiscriminatedObject.Case(
//            name,
//            properties.map { (propName, schema) ->
//                Model.Object.Property(
//                    propName,
//                    schema.toModel(name.nest(NamingContext.ObjectProperty(propName)), context),
//                    required.contains(propName)
//                )
//            }
//        )
    }

    return Model.DiscriminatedObject(
        name,
        abstractProperties,
        listOfNotNull(simpleCase(mapping, abstractProperties)) + subtypes,
        description(),
        schema.title,
        discriminator.propertyName,
        isNullable
    )
}

inline fun <A, B> Map<A, B>.merge(other: Map<A, B>, merge: (key: A, B, B) -> B): Map<A, B> =
    buildMap {
        putAll(this@merge)
        other.forEach { (key, value) ->
            put(key, get(key)?.let { merge(key, it, value) } ?: value)
        }
    }

fun Schema.isEmpty(): Boolean =
    properties.isNullOrEmpty() &&
            additionalProperties == null &&
            enum.isNullOrEmpty() &&
            items == null &&
            anyOf.isNullOrEmpty() &&
            allOf.isNullOrEmpty() &&
            oneOf.isNullOrEmpty()

/*
 * This includes properties from intermediate parents, but intermediate parents are currently not generated.
 * See: BundleElement, LocalizableBundleElement, StateBundleElement in youtrack.json.
 * Where StateBundleElement is actually a subtype of LocalizableBundleElement that is a subtype of the top parent BundleElement.
 * In our current implementation StateBundleElement is just a child of BundleElement, but in this function we ensure it also has the properties of `LocalizableBundleElement`.
 * As a downside, you cannot match on `LocalizableBundleElement` and also cover `StateBundleElement` but need to explicitly cover both.
 * This cannot conveniently be modeled in Kotlin, and the additional boilerplate during 'when' is simpler than the more complex data structure.
 */
context(scope: Registry.Scope)
private tailrec suspend fun ResolvedSchema.Reference.collectSuperTypeProperties(
    properties: Pair<List<String>, Map<String, ReferenceOr<Schema>>>,
    allOf: List<ReferenceOr<Schema>>,
    mapping: Map<String, String>
): Pair<List<String>, Map<String, ReferenceOr<Schema>>> {
    val parentClass = allOf.single { refOrSchema -> refOrSchema.isSubtype(mapping) }
    val schema = (allOf - parentClass).single().peek()
    val next = combineProperties(properties, schema)
    val parentSchema = parentClass.peek()
    val superTypeOrNull = parentSchema.allOf?.singleOrNull { it.isSubtype(mapping) }
    return if (superTypeOrNull != null) collectSuperTypeProperties(next, parentSchema.allOf!!, mapping) else next
}

context(ctx: Registry.Scope)
private suspend fun combineProperties(
    properties: Pair<List<String>, Map<String, ReferenceOr<Schema>>>,
    schema: Schema
): Pair<List<String>, Map<String, ReferenceOr<Schema>>> = Pair(
    properties.first + schema.required,
    properties.second.merge(schema.properties.orEmpty()) { name, a, b ->
        when (a) {
            is ReferenceOr.Reference -> when (b) {
                is ReferenceOr.Value<Schema> -> {
                    require(b.value.isEmpty()) { "Cannot merge non-empty schema $b with reference $a" }
                    b
                }

                is ReferenceOr.Reference -> {
                    require(a.ref == b.ref) { "Cannot merge property $name from different referened schemas: ${a.ref}, ${b.ref}" }
                    a
                }
            }

            is ReferenceOr.Value<Schema> -> when (b) {
                is ReferenceOr.Reference -> {
                    require(a.value.isEmpty()) { "Cannot merge non-empty schema $a with reference $b" }
                    b
                }

                is ReferenceOr.Value<Schema> -> {
                    if (a.value.isEmpty()) b
                    else if (b.value.isEmpty()) a
                    else {
                        val result = a.value.merge(b.value)
                        require(result.type == Type.Basic.Object) { "Cannot merge non-object schemas $a, $b" }
                        ReferenceOr.value(result)
                    }
                }
            }
        }
    }
)

private fun ReferenceOr<Schema>.isSubtype(mapping: Map<String, String>): Boolean =
    if (this is ReferenceOr.Reference) this.ref in mapping.values else false

private fun ResolvedSchema.Reference.simpleCase(
    mapping: Map<String, String>,
    abstractProperties: Map<String, Model.Object.Property>
): Model.Object {
    val mappingName = mapping.entries
        .singleOrNull { (_, ref) -> ref == "#/components/schemas/${reference.name}" }
        ?.key ?: throw IllegalStateException("Expected exactly one self mapping for discriminated object")

    val selfName =
        if (mappingName == reference.name) name.nest(NamingContext.DiscriminatedObjectCase("Default"))
        else name.nest(NamingContext.DiscriminatedObjectCase(mappingName))

    return Model.Object(
        selfName,
        null,
        null,
        abstractProperties,
        emptySet(),
        Model.Object.AdditionalProperties.Allowed(false),
        isNullable
    )
}

context(ctx: Registry.Scope)
suspend fun Schema.merge(other: Schema): Schema = when {
    this == other -> this
    type != null && type == other.type -> when (type!!) {
        Schema.Type.Basic.Array -> mergeCommon(other)
            .mergeCollectionConstraints(other) // TODO merge items
        Schema.Type.Basic.Object -> mergeObject(other)
        Schema.Type.Basic.Number -> mergeCommon(other).numberConstraints(other) // TODO Select biggest format
        Schema.Type.Basic.Boolean -> mergeCommon(other)
        Schema.Type.Basic.Integer -> mergeCommon(other).numberConstraints(other) // TODO Select biggest format
        Schema.Type.Basic.Null -> mergeCommon(other)
        Schema.Type.Basic.String -> mergeCommon(other).mergeTextConstraints(other)
        is Schema.Type.Array -> mergeCommon(other)
    }

    properties != null || other.properties != null -> mergeObject(other)
    enum?.isNotEmpty() == true && other.enum?.isNotEmpty() == true -> TODO("allOf Enum")
    else -> TODO("Merge not supported: $this, $other")
}

fun Schema.mergeCommon(other: Schema): Schema = copy(
    type = type ?: other.type,
    description = description ?: other.description,
    nullable = let(nullable, other.nullable) { a, b -> a || b },
    default = default ?: other.default
)

fun Schema.numberConstraints(other: Schema): Schema =
    copy(
//        format = format ?: other.format,
        minimum = let(minimum, other.minimum, ::maxOf),
        maximum = let(maximum, other.maximum, ::minOf),
        exclusiveMinimum = exclusiveMinimum ?: false || other.exclusiveMinimum ?: false,
        exclusiveMaximum = exclusiveMaximum ?: false || other.exclusiveMaximum ?: false,
        multipleOf = multipleOf ?: other.multipleOf // TODO
    )

fun Schema.mergeTextConstraints(other: Schema): Schema =
    copy(
        minLength = let(minLength, other.minLength, ::maxOf),
        maxLength = let(maxLength, other.maxLength, ::minOf),
        pattern = pattern ?: pattern // TODO
    )

fun Schema.mergeObjectConstraints(other: Schema): Schema =
    copy(
        minProperties = let(minProperties, other.minProperties, ::maxOf),
        maxProperties = let(maxProperties, other.maxProperties, ::minOf)
    )

fun Schema.mergeCollectionConstraints(other: Schema): Schema =
    copy(
        minItems = let(minItems, other.minItems, ::maxOf),
        maxItems = let(maxItems, other.maxItems, ::minOf),
        uniqueItems = uniqueItems ?: false || other.uniqueItems ?: false
    )

context(ctx: Registry.Scope)
suspend fun Schema.mergeObject(other: Schema): Schema {
    val properties = buildMap {
        properties.orEmpty().forEach { (name, schema) -> put(name, schema) }
        other.properties?.forEach { (name, schema) ->
            if (containsKey(name)) put(name, ReferenceOr.value(get(name)!!.peek().merge(schema.peek())))
            else put(name, schema)
        }
    }

    return mergeCommon(other)
        .mergeObjectConstraints(other)
        .copy(
            properties = properties,
            additionalProperties = additionalProperties?.merge(other.additionalProperties)
        )
}

private inline fun <A> let(a: A?, b: A?, block: (A, A) -> A): A? =
    if (a != null && b != null) block(a, b) else a ?: b

context(ctx: Registry.Scope)
private suspend fun AdditionalProperties?.merge(other: AdditionalProperties?): AdditionalProperties? =
    let(this, other) { a, b ->
        when (a) {
            is AdditionalProperties.PSchema -> when (b) {
                is Allowed -> a
                is AdditionalProperties.PSchema -> AdditionalProperties.PSchema(
                    ReferenceOr.value(
                        a.value.peek().merge(b.value.peek())
                    )
                )
            }

            is Allowed -> when (b) {
                is Allowed -> Allowed(a.value && b.value)
                is AdditionalProperties.PSchema -> b
            }
        }
    }
