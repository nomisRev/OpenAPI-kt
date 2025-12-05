package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ResolvedSchema.Reference
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.ktor.client.HttpClient
import kotlin.collections.fold
import kotlin.jvm.JvmInline

// TODO better names..
inline fun <A> ctx(openAPI: OpenAPI, block: context(ApiCtx) () -> A): A =
    ApiCtx(openAPI).use { block(it) }

class ApiCtx(val openAPI: OpenAPI) : AutoCloseable {
    private val client: HttpClient = HttpClient()
    private val cache: MutableList<NamingContext.Reference> = mutableListOf()

    /** This is meant for `Schema.description` which can sometimes reference other schemas descriptions. */
    tailrec suspend fun ReferenceOr<String>?.get(): String? = when (this) {
        is ReferenceOr.Value -> value
        null -> null
        is ReferenceOr.Reference ->  // readOnly & writeOnly don't affect description
            when (val resolved = resolve(SchemaContext.Output)) {
                is Reference -> resolved.schema.description.get()
                is Value -> resolved.schema.description.get()
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
    suspend fun ReferenceOr<Schema>.resolve(context: SchemaContext): ResolvedSchema = when (this) {
        is ReferenceOr.Reference -> resolve(context)
        is ReferenceOr.Value<Schema> -> Value(value)
    }

    /*
     * Traverse a schema to determine if it has readOnly or writeOnly in any (nested) properties.
     * Checks `allOf`, `oneOf`, `anyOf`, `properties`, and `items`.
     */
    private suspend fun Schema.readOrWriteOnly(): Boolean {
        suspend fun ReferenceOr<Schema>.schema(): Schema = when (this) {
            is ReferenceOr.Value<Schema> -> value
            is ReferenceOr.Reference -> {
                val name = ref.drop("#/components/schemas/".length)
                return when (val nested = openAPI.components.schemas[name]) {
                    is ReferenceOr.Reference -> remoteSchema(nested.ref)
                    is ReferenceOr.Value<Schema> -> nested.value
                    null -> throw IllegalStateException("Schema $name could not be found in ${openAPI.components.schemas}.")
                }
            }
        }

        suspend fun Collection<ReferenceOr<Schema>>?.readOrWriteOnly() = orEmpty().fold(false) { acc, refOrSchema ->
            acc || refOrSchema.schema().readOrWriteOnly()
        }

        return allOf.readOrWriteOnly() || oneOf.readOrWriteOnly() || properties?.values.readOrWriteOnly() ||
                anyOf.readOrWriteOnly() || listOfNotNull(items).readOrWriteOnly()
    }

    /**
     * When resolving top-level schemas, we need to:
     *   1. Track all top-level schemas we resolved in [cache] so we can generate only the schema's we actually use.
     *   2. Check the schema has only readOnly & writeOnly properties (or nested properties)
     *      - If there are we need to generate a read _or_ write variant of the schema
     *      - If it doesn't, we can use it as a regular schema; this means schemas are [context] dependent
     */
    private suspend fun ReferenceOr.Reference.resolve(context: SchemaContext): ResolvedSchema {
        val name = ref.drop("#/components/schemas/".length)

        val schema = when (val nested = openAPI.components.schemas[name]) {
            is ReferenceOr.Reference -> remoteSchema(nested.ref)
            is ReferenceOr.Value<Schema> -> nested.value
            null -> throw IllegalStateException("Schema $name could not be found in ${openAPI.components.schemas}.")
        }

        val contextSpecific = schema.readOrWriteOnly()
        val context = if (contextSpecific) context else null
        val reference = NamingContext.Reference(name, context)
        cache.add(reference)

        return Reference(reference, schema)
    }

    override fun close() {
        client.close()
    }
}

// ApiCtx syntax
context(ctx: ApiCtx)
suspend fun ReferenceOr<Schema>.resolve(context: SchemaContext): ResolvedSchema = with(ctx) { resolve(context) }

context(ctx: ApiCtx)
suspend fun ReferenceOr<String>?.get(): String? = with(ctx) { get() }

context(ctx: ApiCtx)
suspend fun ResolvedSchema.description(): String? = with(ctx) { description() }

sealed interface ResolvedSchema {
    @JvmInline
    value class Value(val schema: Schema) : ResolvedSchema
    data class Reference(val name: NamingContext.Reference, val schema: Schema) : ResolvedSchema
}
