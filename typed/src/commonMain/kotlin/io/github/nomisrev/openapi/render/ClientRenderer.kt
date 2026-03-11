package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.API
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.parser.Parameter
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

context(ctx: Renderer)
fun API.renderApiFile(): String = buildString {
    renderApiInterface()
    newLine()
    newLine()
    renderApiImpls(listOf(name))
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
                val implTypeName = "Ktor$typeName"
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
private fun API.renderApiInterface() {
    val interfaceName = name.toPascalCase()
    val children = nested
    val hasMembers = children.isNotEmpty() || routes.isNotEmpty()
    if (!hasMembers) {
        append("interface $interfaceName")
        return
    }

    +"interface $interfaceName {"
    indented {
        children.forEachIndexed { index, child ->
            val propName = child.name.toCamelCase()
            val typeName = child.name.toPascalCase()
            +"val $propName: $typeName"
            if (index < children.size - 1) newLine()
        }

        if (children.isNotEmpty() && routes.isNotEmpty()) {
            newLine()
        }

        routes.forEachIndexed { index, route ->
            +renderSuspendFun(route)
            if (index < routes.size - 1) {
                newLine()
            }
        }

        if (routes.isNotEmpty() && children.isNotEmpty()) {
            newLine()
        }

        if (children.isNotEmpty() && routes.isEmpty()) {
            newLine()
        }

        children.forEachIndexed { index, child ->
            child.renderApiInterface()
            if (index < children.size - 1) {
                newLine()
                newLine()
            }
        }

        if (children.isNotEmpty()) {
            newLine()
        }
    }
    append("}")
}

context(ctx: Renderer, builder: StringBuilder)
private fun API.renderApiImpls(path: List<String>) {
    renderApiImpl(path)
    nested.forEach { child ->
        newLine()
        newLine()
        child.renderApiImpls(path + child.name)
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun API.renderApiImpl(path: List<String>) {
    ctx.import(TypeName.Class("io.ktor.client", "HttpClient"))

    val implName = path.implName()
    val interfaceName = path.interfaceName()
    val children = nested
    val hasBody = children.isNotEmpty() || routes.isNotEmpty()
    if (!hasBody) {
        append("internal class $implName(private val client: HttpClient) : $interfaceName")
        return
    }

    +"internal class $implName(private val client: HttpClient) : $interfaceName {"
    indented {
        children.forEachIndexed { index, child ->
            val propName = child.name.toCamelCase()
            val childPath = path + child.name
            +"override val $propName: ${childPath.interfaceName()} = ${childPath.implName()}(client)"
            if (index < children.size - 1) {
                newLine()
            }
        }

        if (children.isNotEmpty() && routes.isNotEmpty()) {
            newLine()
        }

        routes.forEachIndexed { index, route ->
            renderOperationImpl(route)
            if (index < routes.size - 1) {
                newLine()
            }
        }
    }
    append("}")
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
private fun renderSuspendFun(route: Route): String = buildString {
    val returnType = route.resolveReturnType()
    val body = route.preferredBodyOrNull()
    val params = route.signatureParameters(body)

    if (route.deprecated) {
        appendLine("@Deprecated(\"Deprecated by the API provider\")")
    }

    params.forEach { input ->
        import(input.type)
    }

    body?.signatureParameters?.forEach { input ->
        import(input.type)
    }

    if (params.isEmpty()) {
        append("suspend fun ${route.operationId}(): $returnType")
    } else {
        appendLine("suspend fun ${route.operationId}(")
        params.forEachIndexed { index, input ->
            val paramName = input.name
            val typeName = input.type.toTypeName().type()
            val hasDefault = input.hasDefault()
            val isNullable = !input.isRequired && !hasDefault
            val line = buildString {
                append("$paramName: $typeName")
                if (isNullable) append("?")
                when {
                    hasDefault -> append(" = ${input.type.renderDefault()}")
                    isNullable -> append(" = null")
                }
                append(",")
            }
            appendLine("    $line")
        }
        append("): $returnType")
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderOperationImpl(route: Route) {
    val returnType = route.resolveReturnType()
    val method = route.method.ktorFunction()
    val body = route.preferredBodyOrNull()
    val params = route.signatureParameters(body)

    if (route.deprecated) {
        +"@Deprecated(\"Deprecated by the API provider\")"
    }

    params.forEach { input ->
        import(input.type)
    }

    body?.signatureParameters?.forEach { input ->
        import(input.type)
    }

    val routeParams = route.sortedParameters()
    val pathParams = routeParams.filter { it.input == Parameter.Input.Path }
    val blockParams = routeParams.filter { it.input != Parameter.Input.Path }

    // Build the URL with path parameter interpolation
    val url = if (pathParams.isEmpty()) {
        "\"${route.path}\""
    } else {
        val interpolated = pathParams.fold(route.path) { path, input ->
            path.replace("{${input.name}}", "\$${input.name.toParamName()}")
        }
        "\"$interpolated\""
    }

    if (params.isEmpty()) {
        +"override suspend fun ${route.operationId}(): $returnType ="
        indented {
            ctx.import(TypeName.Class("io.ktor.client.call", "body"))
            +"client.$method($url).body()"
        }
    } else {
        val paramList = params.joinToString(", ") { input ->
            val paramName = input.name
            val typeName = input.type.toTypeName().type()
            val hasDefault = input.hasDefault()
            val isNullable = !input.isRequired && !hasDefault
            buildString {
                append("$paramName: $typeName")
                if (isNullable) append("?")
            }
        }

        if (blockParams.isEmpty() && body == null) {
            // Only path params — no request block needed
            +"override suspend fun ${route.operationId}($paramList): $returnType ="
            indented {
                ctx.import(TypeName.Class("io.ktor.client.call", "body"))
                +"client.$method($url).body()"
            }
        } else {
            // Has query/header/cookie params — need a request block
            +"override suspend fun ${route.operationId}($paramList): $returnType ="
            indented {
                ctx.import(TypeName.Class("io.ktor.client.call", "body"))
                +"client.$method($url) {"
                indented {
                    blockParams.forEach { input ->
                        renderParamPlacement(input)
                    }
                    body?.let {
                        renderBodyPlacement(it)
                    }
                }
                +"}.body()"
            }
        }
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderParamPlacement(input: Route.Input) {
    val paramName = input.name.toParamName()
    val isNullable = !input.isRequired && !input.type.hasDefault()

    when (input.input) {
        Parameter.Input.Query -> {
            if (isNullable) {
                +"$paramName?.let { parameter(\"${input.name}\", it) }"
            } else {
                +"parameter(\"${input.name}\", $paramName)"
            }
        }
        Parameter.Input.Header -> {
            ctx.import(TopLevelFunction("io.ktor.client.request", "header"))
            if (isNullable) {
                +"$paramName?.let { header(\"${input.name}\", it) }"
            } else {
                +"header(\"${input.name}\", $paramName)"
            }
        }
        Parameter.Input.Cookie -> {
            ctx.import(TopLevelFunction("io.ktor.client.request", "cookie"))
            if (isNullable) {
                +"$paramName?.let { cookie(\"${input.name}\", it) }"
            } else {
                +"cookie(\"${input.name}\", $paramName)"
            }
        }
        Parameter.Input.Path -> {} // Handled via URL interpolation
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

private data class SignatureParameter(
    val wireName: String,
    val name: String,
    val type: Model,
    val isRequired: Boolean,
    val allowModelDefault: Boolean,
) {
    fun hasDefault(): Boolean = allowModelDefault && type.hasDefault()
}

private sealed interface RequestBody {
    val required: Boolean
    val signatureParameters: List<SignatureParameter>

    data class SetBody(
        override val required: Boolean,
        val contentType: ContentType,
        val type: Model,
    ) : RequestBody {
        override val signatureParameters: List<SignatureParameter> = listOf(
            SignatureParameter(
                wireName = "body",
                name = "body",
                type = type,
                isRequired = required,
                allowModelDefault = required,
            )
        )
    }

    data class MultipartInline(
        override val required: Boolean,
        val parameters: List<Route.Body.Multipart.FormData>,
    ) : RequestBody {
        override val signatureParameters: List<SignatureParameter> =
            parameters.map { form ->
                SignatureParameter(
                    wireName = form.name,
                    name = form.name.toParamName(),
                    type = form.type,
                    isRequired = required,
                    allowModelDefault = required,
                )
            }
    }

    data class MultipartRef(
        override val required: Boolean,
        val type: Model,
    ) : RequestBody {
        override val signatureParameters: List<SignatureParameter> = listOf(
            SignatureParameter(
                wireName = "body",
                name = "body",
                type = type,
                isRequired = required,
                allowModelDefault = required,
            )
        )
    }

    data class FormUrlEncoded(
        override val required: Boolean,
        val parameters: List<Route.Body.Multipart.FormData>,
    ) : RequestBody {
        override val signatureParameters: List<SignatureParameter> =
            parameters.map { form ->
                SignatureParameter(
                    wireName = form.name,
                    name = form.name.toParamName(),
                    type = form.type,
                    isRequired = required,
                    allowModelDefault = required,
                )
            }
    }
}

private fun Route.signatureParameters(body: RequestBody?): List<SignatureParameter> {
    val standard = sortedParameters().map { input ->
        SignatureParameter(
            wireName = input.name,
            name = input.name.toParamName(),
            type = input.type,
            isRequired = input.isRequired,
            allowModelDefault = true,
        )
    }
    val resolvedBody = body ?: return standard
    val bodyParameters = resolvedBody.signatureParameters
    if (bodyParameters.isEmpty()) return standard

    return if (resolvedBody.required) {
        val firstOptional = standard.indexOfFirst { !it.isRequired && !it.hasDefault() }
        val splitIndex = if (firstOptional == -1) standard.size else firstOptional
        standard.take(splitIndex) + bodyParameters + standard.drop(splitIndex)
    } else {
        standard + bodyParameters
    }
}

private fun Route.preferredBodyOrNull(): RequestBody? {
    val bodies = body ?: return null
    val body = bodies.preferredBodyOrNull() ?: return null
    return when (body) {
        is Route.Body.SetBody -> RequestBody.SetBody(
            required = bodies.required,
            contentType = body.contentType,
            type = body.type,
        )
        is Route.Body.Multipart.Value -> RequestBody.MultipartInline(
            required = bodies.required,
            parameters = body.parameters,
        )
        is Route.Body.Multipart.Ref -> RequestBody.MultipartRef(
            required = bodies.required,
            type = body.value,
        )
        is Route.Body.FormUrlEncoded -> RequestBody.FormUrlEncoded(
            required = bodies.required,
            parameters = body.parameters,
        )
    }
}

private fun Route.Bodies.preferredBodyOrNull(): Route.Body? =
    findBodyOrNull(ContentType.Application.Json)
        ?: findBodyOrNull(ContentType.Application.Xml)
        ?: findBodyOrNull(ContentType.Application.OctetStream)
        ?: findBodyOrNull(ContentType.Text.Plain)
        ?: findBodyOrNull(ContentType.MultiPart.FormData)
        ?: findBodyOrNull(ContentType.Application.FormUrlEncoded)

private fun Route.Bodies.findBodyOrNull(contentType: ContentType): Route.Body? =
    types.entries.firstNotNullOfOrNull { (key, value) ->
        if (contentType.match(key)) {
            when (value) {
                is Route.Body.SetBody -> value.copy(contentType = key)
                else -> value
            }
        } else null
    }

private fun ContentType.asExpression(): String = when {
    ContentType.Application.Json.match(this) -> "ContentType.Application.Json"
    ContentType.Application.Xml.match(this) -> "ContentType.Application.Xml"
    ContentType.Application.OctetStream.match(this) -> "ContentType.Application.OctetStream"
    ContentType.Text.Plain.match(this) -> "ContentType.Text.Plain"
    ContentType.MultiPart.FormData.match(this) -> "ContentType.MultiPart.FormData"
    ContentType.Application.FormUrlEncoded.match(this) -> "ContentType.Application.FormUrlEncoded"
    else -> "ContentType.parse(\"$this\")"
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderBodyPlacement(body: RequestBody) {
    when (body) {
        is RequestBody.SetBody -> {
            ctx.import(
                TypeName.Class("io.ktor.http", "ContentType"),
                TopLevelFunction("io.ktor.client.request", "contentType"),
                TopLevelFunction("io.ktor.client.request", "setBody"),
            )
            +"contentType(${body.contentType.asExpression()})"
            if (body.required) {
                +"setBody(body)"
            } else {
                +"body?.let { setBody(it) }"
            }
        }
        is RequestBody.MultipartInline -> {
            ctx.import(
                TopLevelFunction("io.ktor.client.request", "setBody"),
                TypeName.Class("io.ktor.client.request.forms", "MultiPartFormDataContent"),
                TopLevelFunction("io.ktor.client.request.forms", "formData"),
            )
            +"setBody("
            indented {
                +"MultiPartFormDataContent("
                indented {
                    +"formData {"
                    indented {
                        body.signatureParameters.forEach { parameter ->
                            val value = parameter.name
                            val appendValue = if (parameter.type is Model.ByteArray) value else "$value.toString()"
                            if (parameter.isRequired) {
                                +"append(\"${parameter.wireName}\", $appendValue)"
                            } else {
                                +"$value?.let { append(\"${parameter.wireName}\", ${if (parameter.type is Model.ByteArray) "it" else "it.toString()"}) }"
                            }
                        }
                    }
                    +"}"
                }
                +")"
            }
            +")"
        }
        is RequestBody.MultipartRef -> {
            ctx.import(
                TypeName.Class("io.ktor.http", "ContentType"),
                TopLevelFunction("io.ktor.client.request", "contentType"),
                TopLevelFunction("io.ktor.client.request", "setBody"),
            )
            +"contentType(ContentType.MultiPart.FormData)"
            if (body.required) {
                +"setBody(body)"
            } else {
                +"body?.let { setBody(it) }"
            }
        }
        is RequestBody.FormUrlEncoded -> {
            ctx.import(
                TypeName.Class("io.ktor.http", "ContentType"),
                TypeName.Class("io.ktor.http", "Parameters"),
                TopLevelFunction("io.ktor.http", "formUrlEncode"),
                TopLevelFunction("io.ktor.client.request", "contentType"),
                TopLevelFunction("io.ktor.client.request", "setBody"),
            )
            +"contentType(ContentType.Application.FormUrlEncoded)"
            +"setBody("
            indented {
                +"Parameters.build {"
                indented {
                    body.signatureParameters.forEach { parameter ->
                        val value = parameter.name
                        if (parameter.isRequired) {
                            +"append(\"${parameter.wireName}\", $value.toString())"
                        } else {
                            +"$value?.let { append(\"${parameter.wireName}\", it.toString()) }"
                        }
                    }
                }
                +"}.formUrlEncode()"
            }
            +")"
        }
    }
}

private fun Route.ReturnType.preferredModel(): Model =
    types.firstModelFor(ContentType.Application.Json)
        ?: types.firstModelFor(ContentType.Application.Xml)
        ?: types.firstModelFor(ContentType.Text.Plain)
        ?: types.firstModelFor(ContentType.Application.OctetStream)
        ?: types.values.first()

private fun Map<ContentType, Model>.firstModelFor(contentType: ContentType): Model? =
    entries.firstNotNullOfOrNull { (ct, model) -> if (contentType.match(ct)) model else null }

private fun List<String>.implName(): String = "Ktor" + joinToString("") { it.toPascalCase() }

private fun List<String>.interfaceName(): String = joinToString(".") { it.toPascalCase() }

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

/**
 * Sorts parameters into the spec-defined order:
 * 1. Path parameters (in declaration order)
 * 2. Required query/cookie params (alphabetical)
 * 3. Required header params (alphabetical)
 * 4. Optional query/cookie params (alphabetical)
 * 5. Optional header params (alphabetical)
 *
 * Cookie params are treated as header params per spec §2.
 * A parameter is "optional" only when it has no default and is not required.
 */
private fun Route.sortedParameters(): List<Route.Input> {
    val path = parameters.filter { it.input == Parameter.Input.Path }
    val nonPath = parameters.filter { it.input != Parameter.Input.Path }

    fun Route.Input.isOptional(): Boolean = !isRequired && !type.hasDefault()

    val requiredQuery = nonPath
        .filter { !it.isOptional() && (it.input == Parameter.Input.Query || it.input == Parameter.Input.Cookie) }
        .sortedBy { it.name }
    val requiredHeader = nonPath
        .filter { !it.isOptional() && it.input == Parameter.Input.Header }
        .sortedBy { it.name }
    val optionalQuery = nonPath
        .filter { it.isOptional() && (it.input == Parameter.Input.Query || it.input == Parameter.Input.Cookie) }
        .sortedBy { it.name }
    val optionalHeader = nonPath
        .filter { it.isOptional() && it.input == Parameter.Input.Header }
        .sortedBy { it.name }

    return path + requiredQuery + requiredHeader + optionalQuery + optionalHeader
}

/**
 * Renders a Model's default value as a Kotlin literal.
 */
private fun Model.renderDefault(): String = when (this) {
    is Model.Primitive.Int -> (default as Model.Default.Value<*>).value.toString()
    is Model.Primitive.Long -> "${(default as Model.Default.Value<*>).value}L"
    is Model.Primitive.Float -> "${(default as Model.Default.Value<*>).value}f"
    is Model.Primitive.Double -> (default as Model.Default.Value<*>).value.toString()
    is Model.Primitive.Boolean -> (default as Model.Default.Value<*>).value.toString()
    is Model.Primitive.String -> "\"${(default as Model.Default.Value<*>).value}\""
    else -> "null" // Fallback for types where defaults aren't supported yet
}
