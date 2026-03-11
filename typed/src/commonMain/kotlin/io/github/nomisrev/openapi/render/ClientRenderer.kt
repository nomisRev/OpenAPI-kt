package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod

/**
 * Renders a root interface with suspend functions for each operation,
 * an internal implementation class backed by Ktor HttpClient,
 * and a top-level factory function.
 */
context(ctx: Renderer)
fun Root.renderRootFile(): String = buildString {
    val interfaceName = name.toPascalCase()
    val implName = "Ktor${interfaceName}"

    // Root interface
    renderRootInterface(interfaceName)
    newLine()
    newLine()

    // Impl class
    renderRootImpl(interfaceName, implName)
    newLine()
    newLine()

    // Factory function
    renderFactory(interfaceName, implName)
}

context(ctx: Renderer, builder: StringBuilder)
private fun Root.renderRootInterface(interfaceName: String) {
    if (operations.isEmpty() && endpoints.isEmpty()) {
        append("interface $interfaceName")
        return
    }

    +"interface $interfaceName {"
    indented {
        // Child interface properties
        endpoints.forEachIndexed { index, api ->
            val propName = api.name.toCamelCase()
            val typeName = api.name.toPascalCase()
            +"val $propName: $typeName"
            if (index < endpoints.size - 1 || operations.isNotEmpty()) newLine()
        }

        // Root-level operations
        operations.forEachIndexed { index, route ->
            +renderSuspendFun(route)
            if (index < operations.size - 1) newLine()
        }
    }
    append("}")
}

context(ctx: Renderer, builder: StringBuilder)
private fun Root.renderRootImpl(interfaceName: String, implName: String) {
    ctx.import(TypeName.Class("io.ktor.client", "HttpClient"))

    val hasBody = endpoints.isNotEmpty() || operations.isNotEmpty()
    if (!hasBody) {
        append("internal class $implName(private val client: HttpClient) : $interfaceName")
    } else {
        +"internal class $implName(private val client: HttpClient) : $interfaceName {"
        indented {
            // Child impl instantiation
            endpoints.forEach { api ->
                val propName = api.name.toCamelCase()
                val typeName = api.name.toPascalCase()
                val implTypeName = "${typeName}Impl"
                +"override val $propName: $typeName = $implTypeName(client)"
            }
            if (endpoints.isNotEmpty() && operations.isNotEmpty()) newLine()

            // Root-level operation implementations
            operations.forEachIndexed { index, route ->
                renderOperationImpl(route)
                if (index < operations.size - 1) newLine()
            }
        }
        append("}")
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderFactory(interfaceName: String, implName: String) {
    ctx.import(
        TypeName.Class("io.ktor.client", "HttpClient"),
        TypeName.Class("io.ktor.client", "HttpClientConfig"),
        TypeName.Class("io.ktor.client.plugins.contentnegotiation", "ContentNegotiation"),
        TopLevelFunction("io.ktor.serialization.kotlinx.json", "json"),
        TopLevelFunction("io.ktor.client.plugins", "defaultRequest"),
    )

    +"fun ${interfaceName}Client("
    indented {
        +"baseUrl: String,"
        +"block: HttpClientConfig<*>.() -> Unit = {},"
    }
    +"): $interfaceName {"
    indented {
        +"val client = HttpClient {"
        indented {
            +"install(ContentNegotiation) { json() }"
            +"defaultRequest { url(baseUrl) }"
            +"block()"
        }
        +"}"
        +"return ${implName}(client)"
    }
    append("}")
}

context(ctx: Renderer)
private fun renderSuspendFun(route: Route): String {
    val returnType = route.resolveReturnType()
    return "suspend fun ${route.operationId}(): $returnType"
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderOperationImpl(route: Route) {
    val returnType = route.resolveReturnType()
    val method = route.method.ktorFunction()

    +"override suspend fun ${route.operationId}(): $returnType ="
    indented {
        ctx.import(TypeName.Class("io.ktor.client.call", "body"))
        +"client.$method(\"${route.path}\").body()"
    }
}

context(ctx: Renderer)
private fun Route.resolveReturnType(): String {
    val responses = returns.responses
    // Phase 1: single response, no default → return body type directly
    val (_, returnType) = responses.entries.first()
    val model = returnType.preferredModel()
    import(model)
    return model.toTypeName().type()
}

private fun Route.ReturnType.preferredModel(): Model =
    types.firstModelFor(ContentType.Application.Json)
        ?: types.firstModelFor(ContentType.Application.Xml)
        ?: types.firstModelFor(ContentType.Text.Plain)
        ?: types.firstModelFor(ContentType.Application.OctetStream)
        ?: types.values.first()

private fun Map<ContentType, Model>.firstModelFor(contentType: ContentType): Model? =
    entries.firstNotNullOfOrNull { (ct, model) -> if (contentType.match(ct)) model else null }

private fun HttpMethod.ktorFunction(): String = when (this) {
    HttpMethod.Get -> "get"
    HttpMethod.Post -> "post"
    HttpMethod.Put -> "put"
    HttpMethod.Patch -> "patch"
    HttpMethod.Delete -> "delete"
    HttpMethod.Head -> "head"
    HttpMethod.Options -> "options"
    else -> "HttpMethod(\"$value\")"
}
