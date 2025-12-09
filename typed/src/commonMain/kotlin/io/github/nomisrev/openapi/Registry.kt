package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ResolvedSchema.Reference
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.ktor.client.HttpClient
import kotlin.collections.fold

inline fun <A> registry(openAPI: OpenAPI, block: context(Registry) () -> A): A =
    Registry(openAPI).use { block(it) }

class Registry(val openAPI: OpenAPI) : AutoCloseable {
    private val client: HttpClient = HttpClient()
    private val cache: MutableList<NamingContext.Reference> = mutableListOf()

    fun names(): Set<NamingContext.Reference> = cache.toSet()

    private suspend fun remoteSchema(url: String): Schema =
        TODO("Remote schemas not supported yet.")

    private fun String.schemaName() = drop("#/components/schemas/".length)

    override fun close() = client.close()

    suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model {
        val scope = when (this) {
            is ReferenceOr.Reference -> {
                val referenceName = ref.drop("#/components/schemas/".length)
                val schema = when (val schema = openAPI.components.schemas[referenceName]) {
                    is ReferenceOr.Reference -> remoteSchema(schema.ref)
                    is ReferenceOr.Value<Schema> -> schema.value
                    null -> throw IllegalStateException("Schema $referenceName could not be found in ${openAPI.components.schemas.keys}.")
                }
                val anchor =
                    if (schema.recursiveAnchor == true) Pair(name as NamingContext.Reference, schema) else null
                ScopeImpl(anchor, mutableSetOf(referenceName))
            }

            is ReferenceOr.Value<Schema> -> {
                val anchor =
                    if (value.recursiveAnchor == true) Pair(name, value) else null
                ScopeImpl(anchor, mutableSetOf())
            }
        }
        return with(scope) { resolve(name, context).toModel(context) }
    }

    interface Scope {
        /**
         * Resolves a `ReferenceOr<Schema>` taking into account [context] whether it appears as [SchemaContext.Input],
         * or [SchemaContext.Output].
         */
        suspend fun ReferenceOr<Schema>.resolve(name: NamingContext, context: SchemaContext): ResolvedSchema

        // Predicates
        suspend fun ReferenceOr<Schema>.readOnly(): Boolean?
        suspend fun ReferenceOr<Schema>.writeOnly(): Boolean?
        suspend fun ResolvedSchema.isOpenEnumeration(): Boolean
        suspend fun Reference.isObjectWithDiscriminator(): Boolean
        suspend fun ResolvedSchema.isAnyOfNullableType(): Boolean
        suspend fun ResolvedSchema.isOneOfNullableType(): Boolean
        suspend fun ResolvedSchema.description(): String?
    }

    // Layer of indirection to keep `Registry.Scope` construction private
    private inner class ScopeImpl(
        private val currentAnchor: Pair<NamingContext, Schema>?,
        private val expanding: MutableSet<String>
    ) : Scope {
        /**
         * This function unsafely resolves a schema reference without registering it in cache.
         * Used to write predicates in `Registry.Scope`, and actual resolving.
         */
        private suspend fun ReferenceOr<Schema>.unsafeResolve(): Schema = when (this) {
            is ReferenceOr.Value<Schema> -> value
            is ReferenceOr.Reference if ref == "#" -> TODO()
            is ReferenceOr.Reference -> {
                // TODO expanding check..
                val name = ref.drop("#/components/schemas/".length)
                when (val nested = openAPI.components.schemas[name]) {
                    is ReferenceOr.Reference -> remoteSchema(nested.ref)
                    is ReferenceOr.Value<Schema> -> nested.value
                    null -> throw IllegalStateException("Schema $name could not be found in ${openAPI.components.schemas.keys}.")
                }
            }
        }

        /**
         * Resolves a `ReferenceOr<Schema>` taking into account [context] whether it appears as [SchemaContext.Input],
         * or [SchemaContext.Output].
         */
        override suspend fun ReferenceOr<Schema>.resolve(name: NamingContext, context: SchemaContext): ResolvedSchema =
            when (this) {
                is ReferenceOr.Reference -> resolve(context)
                is ReferenceOr.Value<Schema> -> Value(name, value)
            }

        override suspend fun ReferenceOr<Schema>.readOnly(): Boolean? = unsafeResolve().readOnly
        override suspend fun ReferenceOr<Schema>.writeOnly(): Boolean? = unsafeResolve().writeOnly

        /*
         * Traverse a schema to determine if it has readOnly or writeOnly in any (nested) properties.
         * Checks `allOf`, `oneOf`, `anyOf`, `properties`, and `items`.
         */
        private suspend fun Schema.readOrWriteOnly(): Boolean {
            suspend fun ReferenceOr<Schema>.schema(): Schema? = when (this) {
                is ReferenceOr.Value<Schema> -> value
                is ReferenceOr.Reference if ref == "#" -> null
                is ReferenceOr.Reference -> unsafeResolve()
            }

            suspend fun Collection<ReferenceOr<Schema>>?.readOrWriteOnly() = orEmpty().fold(false) { acc, refOrSchema ->
                acc || refOrSchema.schema().let { writeOnly ?: false || readOnly ?: false }
            }

            return allOf.readOrWriteOnly() || oneOf.readOrWriteOnly() || properties?.values.readOrWriteOnly() ||
                    anyOf.readOrWriteOnly() || listOfNotNull(items).readOrWriteOnly()
        }

        /**
         * When resolving top-level schemas, we need to:
         *   1. Track all top-level schemas we resolved in [cache] so we can generate only the schema's used.
         *   2. Check the schema has only readOnly & writeOnly properties (or nested properties)
         *      - If there are we need to generate a read _or_ write variant of the schema
         *      - If it doesn't, we can use it as a regular schema; this means schemas are [context] dependent
         */
        private suspend fun ReferenceOr.Reference.resolve(context: SchemaContext): ResolvedSchema {
            val name = ref.drop("#/components/schemas/".length)
            val schema = when (this) {
                is ReferenceOr.Reference if ref == "#" -> TODO()
                is ReferenceOr.Reference -> {
                    // TODO expanding check..
                    val name = ref.drop("#/components/schemas/".length)
                    when (val nested = openAPI.components.schemas[name]) {
                        is ReferenceOr.Reference -> remoteSchema(nested.ref)
                        is ReferenceOr.Value<Schema> -> nested.value
                        null -> throw IllegalStateException("Schema $name could not be found in ${openAPI.components.schemas.keys}.")
                    }
                }
            }
            val contextSpecific = schema.readOrWriteOnly()
            val context = if (contextSpecific) context else null
            val reference = NamingContext.Reference(name, context)
            cache.add(reference)
            return Reference(reference, schema)
        }

        override suspend fun ResolvedSchema.isOpenEnumeration(): Boolean {
            val anyOf = schema.anyOf ?: return false
            if (anyOf.size != 2) return false
            val enum = anyOf.singleOrNull { it.unsafeResolve().enum != null } ?: return false
            val other = (anyOf - enum).singleOrNull() ?: return false
            // TODO: what about other open enums? Should we detect type of enum at parse time?
            return other.unsafeResolve().type == Schema.Type.Basic.String
        }

        override suspend fun Reference.isObjectWithDiscriminator(): Boolean =
            schema.properties != null &&
                    schema.discriminator?.mapping?.isNotEmpty() == true &&
                    schema.discriminator?.mapping?.all { (_, ref) ->
                        val mappingName = ref.schemaName()
                        if (name == NamingContext.Reference(mappingName, null)) {
                            true
                        } else {
                            val s = openAPI.components.schemas[mappingName]?.unsafeResolve()
                            s?.allOf != null && s.type == null
                        }
                    } ?: false

        override suspend fun ResolvedSchema.isAnyOfNullableType(): Boolean {
            val anyOf = schema.anyOf ?: return false
            return anyOf.size == 2 && anyOf.singleOrNull { it.unsafeResolve().type == Schema.Type.Basic.Null } != null
        }

        override suspend fun ResolvedSchema.isOneOfNullableType(): Boolean {
            val oneOf = schema.oneOf ?: return false
            return oneOf.size == 2 && oneOf.singleOrNull { it.unsafeResolve().type == Schema.Type.Basic.Null } != null
        }

        /** This is meant for `Schema.description` which can sometimes reference other schemas descriptions. */
        private tailrec suspend fun ReferenceOr<String>?.get(): String? = when (this) {
            is ReferenceOr.Value -> value
            null -> null
            is ReferenceOr.Reference -> unsafeResolve().description.get()
        }

        override suspend fun ResolvedSchema.description(): String? = when (this) {
            is Reference -> this@description.schema.description.get()
            is Value -> this@description.schema.description.get()
        }
    }
}

context(ctx: Registry)
suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
    with(ctx) { toModel(name, context) }

/**
 * Resolve a `ReferenceOr<Schema>` **this will register** the `Schema` according to the `context` as a consumed schema.
 * Only use this method if you're going to _consume_ the resolved schema.
 * Otherwise, use the predicates available on `ReferenceOr<Schema>` below first to determine if you need this schema.
 */
context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.resolve(name: NamingContext, context: SchemaContext): ResolvedSchema =
    with(ctx) {
        resolve(name, context)
    }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.readOnly(): Boolean? = with(ctx) { readOnly() }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.writeOnly(): Boolean? = with(ctx) { writeOnly() }

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isOpenEnumeration(): Boolean = with(ctx) { isOpenEnumeration() }

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isAnyOfNullableType(): Boolean = with(ctx) { isAnyOfNullableType() }

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.isOneOfNullableType(): Boolean = with(ctx) { isOneOfNullableType() }

context(ctx: Registry.Scope)
suspend fun Reference.isObjectWithDiscriminator(): Boolean = with(ctx) { isObjectWithDiscriminator() }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<String>?.get(): String? = with(ctx) { get() }

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.description(): String? = with(ctx) { description() }

sealed interface ResolvedSchema {
    val schema: Schema
    val name: NamingContext
    val isNullable: Boolean
        get() = schema.nullable ?: false

    data class Value(
        override val name: NamingContext,
        override val schema: Schema
    ) : ResolvedSchema

    data class Reference(
        override val name: NamingContext.Reference,
        override val schema: Schema
    ) : ResolvedSchema
}
