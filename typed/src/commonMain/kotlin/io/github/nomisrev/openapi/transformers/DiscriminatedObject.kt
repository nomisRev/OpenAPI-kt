package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.merge
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
import io.github.nomisrev.openapi.registry.schemaName
import kotlin.collections.component1
import kotlin.collections.component2

context(scope: Registry.Scope)
suspend fun Schema.discriminatedSubtypeOrNull(context: SchemaContext, name: String): NamingContext? {
    if (allOf.orEmpty().isEmpty()) return null

    val references = allOf!!.mapNotNull { refOrSchema ->
        when (refOrSchema) {
            is ReferenceOr.Reference -> refOrSchema
            is ReferenceOr.Value<Schema> -> null
        }
    }

    val singleReference = references.singleOrNull() ?: return null
    val singlePeeked = singleReference.peek()
    return when (singleReference) {
        else if singlePeeked.allOf.orEmpty().size in 1..2 -> singlePeeked.discriminatedSubtypeOrNull(context, name)
        else if singlePeeked.discriminator?.mapping.orEmpty().containsValue("#/components/schemas/$name") ->
            NamingContext(
                NamingContext.Reference(singleReference.ref.schemaName(), context),
                listOf(NamingContext.DiscriminatedObjectCase(name))
            )

        else -> null
    }
}

context(scope: Registry.Scope)
suspend fun ResolvedSchema.toDiscriminatedObject(context: SchemaContext): Model.DiscriminatedObject {
    val discriminator = requireNotNull(schema.discriminator) { "Discriminator required for discriminated object" }
    val mapping = requireNotNull(discriminator.mapping) { "Discriminator mapping required" }
    require(this is ResolvedSchema.Reference) { "Expected reference for discriminated object" }

    val mappings = mapping.filterValues { it != "#/components/schemas/${reference.name}" }

    val abstractProperties = properties(schema.properties!!, context)
        .filter { (name, prop) -> prop.model !is Model.FreeFormJson && name != discriminator.propertyName }

    val subtypes = mappings.map { (mappedName, ref) ->
        val name = name.nest(NamingContext.DiscriminatedObjectCase(mappedName))
        val subtypeSchema = ReferenceOr.Reference(ref).peek()
        val allOf = requireNotNull(subtypeSchema.allOf) { "Subtype schema must be allOf" }

        val parentClass = allOf.single { it.isSubtype(mapping) }
        val (superRequired, superProps) =
            parentClass.collectSuperTypeProperties(mapping, context)

        val peeked =
            (allOf - parentClass).singleOrNull()?.peek() ?: Schema()
        val peekedProperties =
            peeked.properties.orEmpty().filter { (_, refOrSchema) -> refOrSchema.takeIf(context) != null }

        val properties =
            superProps.merge(peekedProperties) { _, a, b -> a.combine(b) }
                .filter { (name, _) -> name != discriminator.propertyName }

        ResolvedSchema.Value(
            name,
            peeked.copy(
                properties = properties,
                required = superRequired + peeked.required
            )
        )   // TODO: To remove 'resolveReference' make this toObject to work with resolveReference = false
            //  It's the only place where indirect recursion due to subtype relationship is broken and results in OOM
            .toObject(context, properties)
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
private tailrec suspend fun ReferenceOr<Schema>.collectSuperTypeProperties(
    mapping: Map<String, String>,
    context: SchemaContext,
    properties: Pair<List<String>, Map<String, ReferenceOr<Schema>>> = Pair(emptyList(), emptyMap())
): Pair<List<String>, Map<String, ReferenceOr<Schema>>> {
    val parentSchema = peek()
    val superTypeOrNull = parentSchema.allOf?.singleOrNull { it.isSubtype(mapping) }
    return if (superTypeOrNull != null) {
        val schema = (parentSchema.allOf!! - superTypeOrNull).singleOrNull()?.peek()
        val schemaProps = schema?.properties.orEmpty()
            .filter { (_, refOrSchema) -> refOrSchema.takeIf(context) != null }

        superTypeOrNull.collectSuperTypeProperties(
            mapping,
            context,
            Pair(
                properties.first + schema?.required.orEmpty(),
                properties.second.merge(schemaProps) { _, a, b -> a.combine(b) }
            )
        )
    } else {
        Pair(
            properties.first + parentSchema.required,
            parentSchema.properties.orEmpty().merge(properties.second) { _, a, b -> a.combine(b) }
        )
    }
}

context(ctx: Registry.Scope)
private suspend fun ReferenceOr<Schema>.combine(
    other: ReferenceOr<Schema>,
): ReferenceOr<Schema> = when (this) {
    is ReferenceOr.Reference -> when (other) {
        is ReferenceOr.Value<Schema> -> {
            require(other.value.isEmpty()) { "Cannot merge non-empty schema $other with reference $this" }
            other
        }

        is ReferenceOr.Reference -> {
            // TODO properly implement finding the right type
            if (peek().allOf.orEmpty().contains(other)) other
            else if (other.peek().allOf.orEmpty().contains(this)) this
            else other //throw IllegalStateException("Cannot merge unrelated referenced schemas: $this, $other")
        }
    }

    is ReferenceOr.Value<Schema> -> when (other) {
        is ReferenceOr.Reference -> {
            require(this.value.isEmpty()) { "Cannot merge non-empty schema $this with reference $other" }
            other
        }

        is ReferenceOr.Value<Schema> -> {
            if (this.value.isEmpty()) other
            else if (other.value.isEmpty()) this
            else {
                val result = this.value.merge(other.value)
                ReferenceOr.value(result)
            }
        }
    }
}

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
        Model.Object.AdditionalProperties.Allowed(false),
        isNullable
    )
}

context(ctx: Registry.Scope)
suspend fun Schema.merge(other: Schema): Schema = when {
    this == other -> this
    type != null && type == other.type -> when (type!!) {
        Type.Basic.Array -> mergeCommon(other)
            .mergeCollectionConstraints(other)
            .copy(items = let(items, other.items) { a, b -> a.combine(b) })

        Type.Basic.Object -> mergeObject(other)
        Type.Basic.Number -> mergeCommon(other).numberConstraints(other) // TODO Select biggest format
        Type.Basic.Boolean -> mergeCommon(other)
        Type.Basic.Integer -> mergeCommon(other).numberConstraints(other) // TODO Select biggest format
        Type.Basic.Null -> mergeCommon(other)
        Type.Basic.String -> mergeCommon(other).mergeTextConstraints(other)
        is Type.Array -> mergeCommon(other)
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
        pattern = pattern ?: other.pattern // TODO: intersection of multiple patterns is non-trivial
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
    val properties = properties.orEmpty().merge(other.properties.orEmpty()) { _, a, b -> a.combine(b) }

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
