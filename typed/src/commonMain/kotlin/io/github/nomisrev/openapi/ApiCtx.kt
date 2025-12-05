package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Resolved.Reference
import io.github.nomisrev.openapi.Resolved.Value
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.ktor.client.HttpClient
import kotlin.jvm.JvmInline

// TODO better names..
inline fun <A> ctx(openAPI: OpenAPI, block: context(ApiCtx) () -> A): A =
    ApiCtx(openAPI).use { block(it) }

class ApiCtx(val openAPI: OpenAPI) : AutoCloseable {
    private val client: HttpClient = HttpClient()

//    private val cache: ConcurrentMap<NamingContext, Model> = ConcurrentMap()
//    fun getOrCompute(context: NamingContext): Model =
//        cache.computeIfAbsent(context) { TODO() }

    suspend fun remoteSchema(url: String): Resolved.Reference<Schema> = TODO("Remote schemas not supported yet.")

    override fun close() {
        client.close()
    }
}

sealed interface Resolved<out A> {
    @JvmInline
    value class Value<A>(val value: A) : Resolved<A>
    data class Reference<A>(val name: String, val value: A) : Resolved<A>
}

inline fun <A, B> Resolved<A>.fold(value: (Value<A>) -> B, reference: (Reference<A>) -> B): B = when (this) {
    is Reference<A> -> reference(this)
    is Value<A> -> value(this)
}

context(ctx: ApiCtx)
suspend fun ReferenceOr<Schema>.resolve(): Resolved<Schema> = when (this) {
    is ReferenceOr.Reference -> schema()
    is ReferenceOr.Value<Schema> -> Value(value)
}

context(ctx: ApiCtx)
private suspend fun ReferenceOr.Reference.schema(): Resolved.Reference<Schema> {
    val name = ref.drop("#/components/schemas/".length)
    return when (val nested = ctx.openAPI.components.schemas[name]) {
        is ReferenceOr.Reference -> ctx.remoteSchema(nested.ref)
        is ReferenceOr.Value<Schema> -> Reference(name, nested.value)
        null -> throw IllegalStateException("Schema $name could not be found in ${ctx.openAPI.components.schemas}.")
    }
}

context(ctx: ApiCtx)
tailrec suspend fun ReferenceOr<String>?.get(): String? = when (this) {
    is ReferenceOr.Value -> value
    null -> null
    is ReferenceOr.Reference -> schema().value.description.get()
}

context(ctx: ApiCtx)
fun ReferenceOr<Parameter>.resolve(): Resolved<Parameter> = when (this) {
    is ReferenceOr.Value -> Value(value)
    is ReferenceOr.Reference -> {
        val referenceName = ref.drop("#/components/parameters/".length)
        when (val parameter = ctx.openAPI.components.parameters[referenceName]) {
            is ReferenceOr.Reference -> TODO("Remote parameters not supported yet.")
            is ReferenceOr.Value<Parameter> -> Resolved.Reference(referenceName, parameter.value)
            null -> throw IllegalStateException("Parameter $referenceName could not be found in ${ctx.openAPI.components.parameters}.")
        }
    }
}