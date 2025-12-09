package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ResolvedSchema.Reference
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.resolve
import io.ktor.client.HttpClient
import kotlin.collections.fold
import kotlin.jvm.JvmInline

inline fun <A> registry(openAPI: OpenAPI, block: context(Registry) () -> A): A =
    Registry(openAPI).use { block(it) }

class Registry(val openAPI: OpenAPI) : AutoCloseable {
    private val client: HttpClient = HttpClient()
    private val cache: MutableList<NamingContext.Reference> = mutableListOf()

    fun names(): Set<NamingContext.Reference> = cache.toSet()

    /** This is meant for `Schema.description` which can sometimes reference other schemas descriptions. */
    tailrec suspend fun ReferenceOr<String>?.get(): String? = when (this) {
        is ReferenceOr.Value -> value
        null -> null
        is ReferenceOr.Reference -> getUnresolved().description.get()
    }

    private suspend fun ReferenceOr<Schema>.getUnresolved(): Schema = when (this) {
        is ReferenceOr.Value<Schema> -> value
        is ReferenceOr.Reference -> {
            val name = ref.drop("#/components/schemas/".length)
            when (val nested = openAPI.components.schemas[name]) {
                is ReferenceOr.Reference -> remoteSchema(nested.ref)
                is ReferenceOr.Value<Schema> -> nested.value
                null -> throw IllegalStateException("Schema $name could not be found in ${openAPI.components.schemas.keys}.")
            }
        }
    }

    suspend fun ResolvedSchema.description(): String? = when (this) {
        is Reference -> this@description.schema.description.get()
        is Value -> this@description.schema.description.get()
    }

    private suspend fun remoteSchema(url: String): Schema =
        TODO("Remote schemas not supported yet.")

    /**
     * Resolves a `ReferenceOr<Schema>` taking into account [context] whether it appears as [SchemaContext.Input],
     * or [SchemaContext.Output].
     */
    suspend fun ReferenceOr<Schema>.resolve(name: NamingContext, context: SchemaContext): ResolvedSchema = when (this) {
        is ReferenceOr.Reference -> resolve(context)
        is ReferenceOr.Value<Schema> -> Value(name, value)
    }

    suspend fun ReferenceOr<Schema>.readOnly(): Boolean? = getUnresolved().readOnly
    suspend fun ReferenceOr<Schema>.writeOnly(): Boolean? = getUnresolved().writeOnly

    /*
     * Traverse a schema to determine if it has readOnly or writeOnly in any (nested) properties.
     * Checks `allOf`, `oneOf`, `anyOf`, `properties`, and `items`.
     */
    private suspend fun Schema.readOrWriteOnly(): Boolean {
        suspend fun ReferenceOr<Schema>.schema(): Schema = when (this) {
            is ReferenceOr.Value<Schema> -> value
            is ReferenceOr.Reference -> getUnresolved()
        }

        suspend fun Collection<ReferenceOr<Schema>>?.readOrWriteOnly() = orEmpty().fold(false) { acc, refOrSchema ->
            acc || refOrSchema.schema().let { writeOnly ?: false || readOnly ?: false }
        }

        return allOf.readOrWriteOnly() || oneOf.readOrWriteOnly() || properties?.values.readOrWriteOnly() ||
                anyOf.readOrWriteOnly() || listOfNotNull(items).readOrWriteOnly()
    }

    private fun String.schemaName() = drop("#/components/schemas/".length)

    /**
     * When resolving top-level schemas, we need to:
     *   1. Track all top-level schemas we resolved in [cache] so we can generate only the schema's used.
     *   2. Check the schema has only readOnly & writeOnly properties (or nested properties)
     *      - If there are we need to generate a read _or_ write variant of the schema
     *      - If it doesn't, we can use it as a regular schema; this means schemas are [context] dependent
     */
    private suspend fun ReferenceOr.Reference.resolve(context: SchemaContext): ResolvedSchema {
        val name = ref.drop("#/components/schemas/".length)
        val schema = getUnresolved()
        val contextSpecific = schema.readOrWriteOnly()
        val context = if (contextSpecific) context else null
        val reference = NamingContext.Reference(name, context)
        cache.add(reference)
        return Reference(reference, schema)
    }

    suspend fun ResolvedSchema.isOpenEnumeration(): Boolean {
        val anyOf = schema.anyOf ?: return false
        if (anyOf.size != 2) return false
        val enum = anyOf.singleOrNull { it.getUnresolved().enum != null } ?: return false
        val other = (anyOf - enum).singleOrNull() ?: return false
        // TODO: what about other open enums? Should we detect type of enum at parse time?
        return other.getUnresolved().type == Schema.Type.Basic.String
    }

    suspend fun Reference.isObjectWithDiscriminator(): Boolean =
        schema.properties != null &&
                schema.discriminator?.mapping?.isNotEmpty() == true &&
                schema.discriminator?.mapping?.all { (_, ref) ->
                    val mappingName = ref.schemaName()
                    if (name == NamingContext.Reference(mappingName, null)) {
                        true
                    } else {
                        val s = openAPI.components.schemas[mappingName]?.getUnresolved()
                        s?.allOf != null && s.type == null
                    }
                } ?: false

    suspend fun ResolvedSchema.isAnyOfNullableType(): Boolean {
        val anyOf = schema.anyOf ?: return false
        return anyOf.size == 2 && anyOf.singleOrNull { it.getUnresolved().type == Schema.Type.Basic.Null } != null
    }

    suspend fun ResolvedSchema.isOneOfNullableType(): Boolean {
        val oneOf = schema.oneOf ?: return false
        return oneOf.size == 2 && oneOf.singleOrNull { it.getUnresolved().type == Schema.Type.Basic.Null } != null
    }

    override fun close() {
        client.close()
    }
}

// ApiCtx syntax
context(ctx: Registry)
suspend fun ReferenceOr<Schema>.resolve(name: NamingContext, context: SchemaContext): ResolvedSchema =
    with(ctx) { resolve(name, context) }

context(ctx: Registry)
suspend fun ReferenceOr<Schema>.readOnly(): Boolean? = with(ctx) { readOnly() }

context(ctx: Registry)
suspend fun ReferenceOr<Schema>.writeOnly(): Boolean? = with(ctx) { writeOnly() }

context(ctx: Registry)
suspend fun ResolvedSchema.isOpenEnumeration(): Boolean = with(ctx) { isOpenEnumeration() }

context(ctx: Registry)
suspend fun ResolvedSchema.isAnyOfNullableType(): Boolean = with(ctx) { isAnyOfNullableType() }

context(ctx: Registry)
suspend fun ResolvedSchema.isOneOfNullableType(): Boolean = with(ctx) { isOneOfNullableType() }

context(ctx: Registry)
suspend fun Reference.isObjectWithDiscriminator(): Boolean = with(ctx) { isObjectWithDiscriminator() }

context(ctx: Registry)
suspend fun ReferenceOr<String>?.get(): String? = with(ctx) { get() }

context(ctx: Registry)
suspend fun ResolvedSchema.description(): String? = with(ctx) { description() }

sealed interface ResolvedSchema {
    val schema: Schema
    val name: NamingContext

    data class Value(
        override val name: NamingContext,
        override val schema: Schema
    ) : ResolvedSchema

    data class Reference(
        override val name: NamingContext.Reference,
        override val schema: Schema
    ) : ResolvedSchema
}
