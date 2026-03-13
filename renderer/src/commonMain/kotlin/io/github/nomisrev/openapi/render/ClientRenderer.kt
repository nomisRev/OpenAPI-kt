package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.API
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.Server
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.isTopLevel
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
    if (servers.isNotEmpty()) {
        newLine()
        newLine()
        renderServerInterface(interfaceName)
    }
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

    val inlineModels = operations.inlineModels()

    +"interface $interfaceName {"
    indented {
        // Child interface properties
        endpoints.forEachIndexed { index, api ->
            val propName = api.name.toCamelCase()
            val typeName = api.name.toPascalCase()
            +"val $propName: $typeName"
            if (index < endpoints.size - 1 || inlineModels.isNotEmpty() || operations.isNotEmpty()) newLine()
        }

        if (inlineModels.isNotEmpty()) {
            renderInlineModels(inlineModels)
            if (operations.isNotEmpty()) newLine()
        }

        // Root-level operations
        operations.forEachIndexed { index, route ->
            renderRouteSignature(route)
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
                renderOperationImpl(route, interfaceName)
                if (index < operations.size - 1) newLine()
            }
        }
        append("}")
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun Root.renderServerInterface(interfaceName: String) {
    val serverInterfaceName = "${interfaceName}Server"
    val caseNames = servers.caseNames()

    +"sealed interface $serverInterfaceName {"
    indented {
        +"val url: String"
        newLine()

        servers.forEachIndexed { index, server ->
            renderServerCase(serverInterfaceName, caseNames[index], server)
            if (index < servers.lastIndex) {
                newLine()
                newLine()
            }
        }

        if (servers.isNotEmpty()) {
            newLine()
            newLine()
        }
        +"data class Custom(override val url: String) : $serverInterfaceName"
    }
    +"}"
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderServerCase(
    serverInterfaceName: String,
    caseName: String,
    server: Server,
) {
    val variables = server.variables.orEmpty().entries.toList()
    if (variables.isEmpty()) {
        +"data object $caseName : $serverInterfaceName {"
        indented {
            +"override val url = ${server.url.toKotlinStringLiteral()}"
        }
        +"}"
        return
    }

    val renderedVariables = variables.renderVariables(caseName)

    +"data class $caseName("
    indented {
        renderedVariables.forEach { variable ->
            +"val ${variable.parameterName}: ${variable.parameterType} = ${variable.defaultExpression},"
        }
    }
    +") : $serverInterfaceName {"
    indented {
        +"override val url: String"
        indented {
            val interpolationByName = renderedVariables.associateBy({ it.wireName }, { it.interpolationExpression })
            +"get() = ${server.url.toInterpolatedKotlinStringLiteral(interpolationByName)}"
        }

        val enums = renderedVariables.mapNotNull { it.enumDeclaration }
        if (enums.isNotEmpty()) {
            newLine()
            enums.forEachIndexed { index, enumDeclaration ->
                +"enum class ${enumDeclaration.name}(val value: String) {"
                indented {
                    enumDeclaration.entries.forEach { (entryName, value) ->
                        +"$entryName(${value.toKotlinStringLiteral()}),"
                    }
                }
                +"}"
                if (index < enums.lastIndex) {
                    newLine()
                }
            }
        }
    }
    +"}"
}

context(ctx: Renderer, builder: StringBuilder)
private fun API.renderApiInterface() {
    val interfaceName = name.toPascalCase()
    val children = nested
    val inlineModels = routes.inlineModels()
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
            if (index < children.size - 1 || inlineModels.isNotEmpty() || routes.isNotEmpty()) newLine()
        }

        if (inlineModels.isNotEmpty()) {
            renderInlineModels(inlineModels)
            if (routes.isNotEmpty()) {
                newLine()
            }
        }

        routes.forEachIndexed { index, route ->
            renderRouteSignature(route)
            if (index < routes.size - 1) {
                newLine()
            }
        }

        if (children.isNotEmpty()) {
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
            renderOperationImpl(route, interfaceName)
            if (index < routes.size - 1) {
                newLine()
            }
        }
    }
    append("}")
}

context(ctx: Renderer, builder: StringBuilder)
private fun Root.renderFactory(interfaceName: String, implName: String) {
    ctx.import(
        TypeName.Class("io.ktor.client", "HttpClient"),
        TypeName.Class("io.ktor.client", "HttpClientConfig"),
        TypeName.Class("io.ktor.client.plugins.contentnegotiation", "ContentNegotiation"),
        TopLevelFunction("io.ktor.serialization.kotlinx.json", "json"),
        TopLevelFunction("io.ktor.client.plugins", "defaultRequest"),
    )

    +"fun ${interfaceName}Client("
    indented {
        if (servers.isEmpty()) {
            +"baseUrl: String,"
        } else {
            val serverInterfaceName = "${interfaceName}Server"
            val defaultServerCase = servers.caseNames().first()
            val defaultServerExpression = if (servers.first().variables.orEmpty().isEmpty()) {
                "$serverInterfaceName.$defaultServerCase"
            } else {
                "$serverInterfaceName.$defaultServerCase()"
            }
            +"server: $serverInterfaceName = $defaultServerExpression,"
        }
        +"block: HttpClientConfig<*>.() -> Unit = {},"
    }
    +"): $interfaceName {"
    indented {
        +"val client = HttpClient {"
        indented {
            +"install(ContentNegotiation) { json() }"
            if (servers.isEmpty()) {
                +"defaultRequest { url(baseUrl) }"
            } else {
                +"defaultRequest { url(server.url) }"
            }
            +"block()"
        }
        +"}"
        +"return ${implName}(client)"
    }
    append("}")
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderRouteSignature(route: Route) {
    if (route.usesSealedReturnType()) {
        route.renderSealedResultType()
        newLine()
    }
    +renderSuspendFun(route)
}

context(ctx: Renderer)
private fun List<Route>.inlineModels(): List<Model> =
    asSequence()
        .flatMap { it.nested.asSequence() }
        .filterIsInstance<Model.ContextHolder>()
        .distinctBy { it.name().fqName }
        .sortedBy { it.name().fqName }
        .map { it as Model }
        .toList()

context(ctx: Renderer, builder: StringBuilder)
private fun renderInlineModels(models: List<Model>) {
    models.forEachIndexed { index, model ->
        +model.render()
        if (index < models.lastIndex) {
            newLine()
            newLine()
        }
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun Route.renderSealedResultType() {
    val resultTypeName = sealedResultTypeName()

    +"sealed interface $resultTypeName {"
    indented {
        returns.responses.entries
            .sortedBy { (status, _) -> status.value }
            .forEachIndexed { index, (status, returnType) ->
                +renderStatusCase(resultTypeName, status, returnType)
                if (index < returns.responses.size - 1 || returns.default != null) {
                    newLine()
                }
            }

        returns.default?.let { defaultReturn ->
            val model = defaultReturn.preferredModel()
            import(model)
            ctx.import(TypeName.Class("io.ktor.http", "HttpStatusCode"))
            val modelType = model.toTypeName().type()
            +"data class Default(val status: HttpStatusCode, val value: $modelType) : $resultTypeName"
        }
    }
    +"}"
}

context(ctx: Renderer)
private fun Route.renderStatusCase(
    resultTypeName: String,
    status: io.ktor.http.HttpStatusCode,
    returnType: Route.ReturnType,
): String {
    val caseName = status.sealedCaseName()
    val model = returnType.preferredModel()
    import(model)
    val isEmpty = returnType.isEmptyBody()

    return if (isEmpty) {
        "data object $caseName : $resultTypeName"
    } else {
        val modelType = model.toTypeName().type()
        "data class $caseName(val value: $modelType) : $resultTypeName"
    }
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
        append("suspend fun ${route.operationId.toCamelCase()}(): $returnType")
    } else {
        appendLine("suspend fun ${route.operationId.toCamelCase()}(")
        params.forEachIndexed { index, input ->
            val paramName = input.name
            val typeName = input.type.renderTypeName()
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
private fun renderOperationImpl(route: Route, interfaceName: String) {
    val returnType = route.resolveReturnType(interfaceName)
    val method = route.method.ktorFunction()
    if (method.matches(Regex("[A-Za-z_][A-Za-z0-9_]*"))) {
        ctx.import(TopLevelFunction("io.ktor.client.request", method))
    }
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

    val url = if (pathParams.isEmpty()) {
        "\"${route.path}\""
    } else {
        val interpolated = pathParams.fold(route.path) { path, input ->
            path.replace("{${input.name}}", "\$${input.name.toParamName()}")
        }
        "\"$interpolated\""
    }

    val paramList = params.joinToString(", ") { input ->
        val paramName = input.name
        val typeName = input.type.renderTypeName(interfaceName)
        val hasDefault = input.hasDefault()
        val isNullable = !input.isRequired && !hasDefault
        buildString {
            append("$paramName: $typeName")
            if (isNullable) append("?")
        }
    }

    if (route.usesSealedReturnType()) {
        if (params.isEmpty()) {
            +"override suspend fun ${route.operationId.toCamelCase()}(): $returnType {"
        } else {
            +"override suspend fun ${route.operationId.toCamelCase()}($paramList): $returnType {"
        }

        indented {
            ctx.import(
                TypeName.Class("io.ktor.http", "HttpStatusCode"),
                TopLevelFunction("io.ktor.client.call", "body"),
            )
            val requiresRequestBlock = blockParams.isNotEmpty() || body != null
            if (requiresRequestBlock) {
                +"val response = client.$method($url) {"
                indented {
                    blockParams.forEach { input ->
                        renderParamPlacement(input)
                    }
                    body?.let { requestBody ->
                        renderBodyPlacement(requestBody)
                    }
                }
                +"}"
            } else {
                +"val response = client.$method($url)"
            }
            +"return when (response.status) {"
            indented {
                route.returns.responses.entries
                    .sortedBy { (status, _) -> status.value }
                    .forEach { (status, returnTypeCase) ->
                        val caseName = status.sealedCaseName()
                        if (returnTypeCase.isEmptyBody()) {
                            +"${status.asWhenCondition()} -> $returnType.$caseName"
                        } else {
                            +"${status.asWhenCondition()} -> $returnType.$caseName(response.body())"
                        }
                    }

                if (route.returns.default != null) {
                    +"else -> $returnType.Default(response.status, response.body())"
                } else {
                    ctx.import(TypeName.Class("io.ktor.client.plugins", "ResponseException"))
                    +"else -> throw ResponseException(response, \"Undocumented status code: ${'$'}{response.status}\")"
                }
            }
            +"}"
        }
        +"}"
        return
    }

    if (params.isEmpty()) {
        +"override suspend fun ${route.operationId.toCamelCase()}(): $returnType ="
        indented {
            ctx.import(TypeName.Class("io.ktor.client.call", "body"))
            +"client.$method($url).body()"
        }
    } else if (blockParams.isEmpty() && body == null) {
        +"override suspend fun ${route.operationId.toCamelCase()}($paramList): $returnType ="
        indented {
            ctx.import(TypeName.Class("io.ktor.client.call", "body"))
            +"client.$method($url).body()"
        }
    } else {
        +"override suspend fun ${route.operationId.toCamelCase()}($paramList): $returnType ="
        indented {
            ctx.import(TypeName.Class("io.ktor.client.call", "body"))
            +"client.$method($url) {"
            indented {
                blockParams.forEach { input ->
                    renderParamPlacement(input)
                }
                body?.let { requestBody ->
                    renderBodyPlacement(requestBody)
                }
            }
            +"}.body()"
        }
    }
}

context(ctx: Renderer, builder: StringBuilder)
private fun renderParamPlacement(input: Route.Input) {
    val paramName = input.name.toParamName()
    val isNullable = !input.isRequired && !input.type.hasDefault()

    when (input.input) {
        Parameter.Input.Query -> {
            ctx.import(TopLevelFunction("io.ktor.client.request", "parameter"))
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
private fun Route.resolveReturnType(interfaceName: String? = null): String {
    if (usesSealedReturnType()) {
        val resultTypeName = sealedResultTypeName()
        return if (interfaceName == null) resultTypeName else "$interfaceName.$resultTypeName"
    }

    val (_, returnType) = returns.responses.entries.first()
    val model = returnType.preferredModel()
    import(model)
    return model.renderTypeName(interfaceName)
}

context(ctx: Renderer)
private fun Model.renderTypeName(interfaceName: String? = null): String = when (this) {
    is Model.Collection if inner is Model.FreeFormJson -> TypeName.JsonArray.type()
    is Model.Collection -> "List<${inner.renderTypeName(interfaceName)}>"
    is Model.Object -> {
        val additional = additionalProperties
        if (properties.isEmpty() && additional is Model.Object.AdditionalProperties.Allowed && additional.value) {
            TypeName.JsonObject.type()
        } else if (properties.isEmpty() && additional is Model.Object.AdditionalProperties.Schema) {
            additional.value.renderTypeName(interfaceName)
        } else {
            val typeName = toTypeName().type()
            if (interfaceName != null && !context.isTopLevel()) "$interfaceName.$typeName" else typeName
        }
    }

    is Model.ContextHolder -> {
        val typeName = toTypeName().type()
        if (interfaceName != null && !context.isTopLevel()) "$interfaceName.$typeName" else typeName
    }
    else -> toTypeName().type()
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
                TopLevelFunction("io.ktor.http", "contentType"),
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
                            val needsToString = parameter.type.needsToString()
                            val appendValue = if (needsToString) "$value.toString()" else value
                            if (parameter.isRequired) {
                                +"append(\"${parameter.wireName}\", $appendValue)"
                            } else {
                                +"$value?.let { append(\"${parameter.wireName}\", ${if (needsToString) "it.toString()" else "it"}) }"
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
                TopLevelFunction("io.ktor.http", "contentType"),
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
                TopLevelFunction("io.ktor.http", "contentType"),
                TopLevelFunction("io.ktor.client.request", "setBody"),
            )
            +"contentType(ContentType.Application.FormUrlEncoded)"
            +"setBody("
            indented {
                +"Parameters.build {"
                indented {
                    body.signatureParameters.forEach { parameter ->
                        val value = parameter.name
                        val needsToString = parameter.type.needsToString()
                        if (parameter.isRequired) {
                            +"append(\"${parameter.wireName}\", ${if (needsToString) "$value.toString()" else value})"
                        } else {
                            +"$value?.let { append(\"${parameter.wireName}\", ${if (needsToString) "it.toString()" else "it"}) }"
                        }
                    }
                }
                +"}.formUrlEncode()"
            }
            +")"
        }
    }
}

private data class RenderedServerVariable(
    val wireName: String,
    val parameterName: String,
    val parameterType: String,
    val defaultExpression: String,
    val interpolationExpression: String,
    val enumDeclaration: RenderedServerEnum? = null,
)

private data class RenderedServerEnum(
    val name: String,
    val entries: List<RenderedServerEnumEntry>,
)

private data class RenderedServerEnumEntry(
    val name: String,
    val value: String,
)

private fun List<Server>.caseNames(): List<String> {
    val hasMultipleServers = size > 1
    val usedNames = mutableSetOf("Custom")

    return mapIndexed { index, server ->
        val fallback = if (hasMultipleServers) "Server${index + 1}" else "Default"
        val baseName = server.description
            ?.toServerCaseNameOrNull()
            ?.takeIf { it.isValidParamName() }
            ?: fallback

        var candidate = baseName
        var duplicateCounter = 2
        while (!usedNames.add(candidate)) {
            candidate = "$baseName$duplicateCounter"
            duplicateCounter += 1
        }
        candidate
    }
}

private fun List<Map.Entry<String, Server.Variable>>.renderVariables(caseName: String): List<RenderedServerVariable> {
    val usedEnumNames = mutableSetOf<String>()
    return mapIndexed { index, (wireName, variable) ->
        val parameterName = wireName.toParamName()
        val enumValues = variable.enum.orEmpty()
        if (enumValues.isEmpty()) {
            RenderedServerVariable(
                wireName = wireName,
                parameterName = parameterName,
                parameterType = "String",
                defaultExpression = variable.default.toKotlinStringLiteral(),
                interpolationExpression = "\${$parameterName}",
            )
        } else {
            val fallbackEnumName = "${caseName}Variable${index + 1}"
            val proposedEnumName = wireName.toPascalCase().takeIf { it.isValidParamName() } ?: fallbackEnumName
            var enumName = proposedEnumName
            var duplicateCounter = 2
            while (!usedEnumNames.add(enumName)) {
                enumName = "$proposedEnumName$duplicateCounter"
                duplicateCounter += 1
            }

            val enumEntries = enumValues.map { rawValue ->
                RenderedServerEnumEntry(name = toEnumValueName(rawValue), value = rawValue)
            }
            val defaultEntryName = enumEntries.firstOrNull { it.value == variable.default }?.name
                ?: enumEntries.first().name

            RenderedServerVariable(
                wireName = wireName,
                parameterName = parameterName,
                parameterType = enumName,
                defaultExpression = "$enumName.$defaultEntryName",
                interpolationExpression = "\${$parameterName.value}",
                enumDeclaration = RenderedServerEnum(name = enumName, entries = enumEntries),
            )
        }
    }
}

private fun String.toServerCaseNameOrNull(): String? {
    val words = splitToWords().map { it.trim() }.filter { it.isNotEmpty() }
    if (words.isEmpty()) return null

    val stripped = if (words.last().equals("server", ignoreCase = true)) words.dropLast(1) else words
    if (stripped.isEmpty()) return null

    return stripped.joinToString(" ").toPascalCase().takeIf { it.isNotBlank() }
}

private fun String.toInterpolatedKotlinStringLiteral(interpolationByName: Map<String, String>): String {
    val placeholderRegex = "\\{([^{}]+)}".toRegex()
    val rendered = buildString {
        var start = 0
        for (match in placeholderRegex.findAll(this@toInterpolatedKotlinStringLiteral)) {
            val matchStart = match.range.first
            val matchEndExclusive = match.range.last + 1
            append(this@toInterpolatedKotlinStringLiteral.substring(start, matchStart).escapeKotlinString())

            val placeholderName = match.groupValues[1]
            val interpolation = interpolationByName[placeholderName]
            if (interpolation == null) {
                append(match.value.escapeKotlinString())
            } else {
                append(interpolation)
            }
            start = matchEndExclusive
        }

        append(this@toInterpolatedKotlinStringLiteral.substring(start).escapeKotlinString())
    }

    return "\"$rendered\""
}

private fun String.toKotlinStringLiteral(): String = "\"${escapeKotlinString()}\""

private fun String.escapeKotlinString(): String = buildString {
    this@escapeKotlinString.forEach { char ->
        when (char) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '$' -> append("\\$")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(char)
        }
    }
}

private fun Route.ReturnType.preferredModel(): Model =
    types.firstModelFor(ContentType.Application.Json)
        ?: types.firstModelFor(ContentType.Application.Xml)
        ?: types.firstModelFor(ContentType.Text.Plain)
        ?: types.firstModelFor(ContentType.Application.OctetStream)
        ?: types.values.firstOrNull()
        ?: Model.Primitive.Unit(null, false, null)

private fun Route.usesSealedReturnType(): Boolean =
    returns.responses.size > 1 || returns.default != null

private fun Route.sealedResultTypeName(): String =
    "${operationId.toPascalCase()}Result"

private fun Route.ReturnType.isEmptyBody(): Boolean =
    types.isEmpty() || preferredModel() is Model.Primitive.Unit

private fun io.ktor.http.HttpStatusCode.sealedCaseName(): String = when (value) {
    204 -> "NoContent"
    else -> description
        .replace(Regex("[^A-Za-z0-9]+"), " ")
        .trim()
        .toPascalCase()
}

private fun io.ktor.http.HttpStatusCode.asWhenCondition(): String =
    when (value) {
        100 -> "HttpStatusCode.Continue"
        101 -> "HttpStatusCode.SwitchingProtocols"
        102 -> "HttpStatusCode.Processing"
        103 -> "HttpStatusCode.EarlyHints"
        200 -> "HttpStatusCode.OK"
        201 -> "HttpStatusCode.Created"
        202 -> "HttpStatusCode.Accepted"
        203 -> "HttpStatusCode.NonAuthoritativeInformation"
        204 -> "HttpStatusCode.NoContent"
        205 -> "HttpStatusCode.ResetContent"
        206 -> "HttpStatusCode.PartialContent"
        207 -> "HttpStatusCode.MultiStatus"
        208 -> "HttpStatusCode.AlreadyReported"
        226 -> "HttpStatusCode.IMUsed"
        300 -> "HttpStatusCode.MultipleChoices"
        301 -> "HttpStatusCode.MovedPermanently"
        302 -> "HttpStatusCode.Found"
        303 -> "HttpStatusCode.SeeOther"
        304 -> "HttpStatusCode.NotModified"
        305 -> "HttpStatusCode.UseProxy"
        307 -> "HttpStatusCode.TemporaryRedirect"
        308 -> "HttpStatusCode.PermanentRedirect"
        400 -> "HttpStatusCode.BadRequest"
        401 -> "HttpStatusCode.Unauthorized"
        402 -> "HttpStatusCode.PaymentRequired"
        403 -> "HttpStatusCode.Forbidden"
        404 -> "HttpStatusCode.NotFound"
        405 -> "HttpStatusCode.MethodNotAllowed"
        406 -> "HttpStatusCode.NotAcceptable"
        407 -> "HttpStatusCode.ProxyAuthenticationRequired"
        408 -> "HttpStatusCode.RequestTimeout"
        409 -> "HttpStatusCode.Conflict"
        410 -> "HttpStatusCode.Gone"
        411 -> "HttpStatusCode.LengthRequired"
        412 -> "HttpStatusCode.PreconditionFailed"
        413 -> "HttpStatusCode.PayloadTooLarge"
        414 -> "HttpStatusCode.UriTooLong"
        415 -> "HttpStatusCode.UnsupportedMediaType"
        416 -> "HttpStatusCode.RangeNotSatisfiable"
        417 -> "HttpStatusCode.ExpectationFailed"
        421 -> "HttpStatusCode.MisdirectedRequest"
        422 -> "HttpStatusCode.UnprocessableEntity"
        423 -> "HttpStatusCode.Locked"
        424 -> "HttpStatusCode.FailedDependency"
        425 -> "HttpStatusCode.TooEarly"
        426 -> "HttpStatusCode.UpgradeRequired"
        428 -> "HttpStatusCode.PreconditionRequired"
        429 -> "HttpStatusCode.TooManyRequests"
        431 -> "HttpStatusCode.RequestHeaderFieldsTooLarge"
        451 -> "HttpStatusCode.UnavailableForLegalReasons"
        500 -> "HttpStatusCode.InternalServerError"
        501 -> "HttpStatusCode.NotImplemented"
        502 -> "HttpStatusCode.BadGateway"
        503 -> "HttpStatusCode.ServiceUnavailable"
        504 -> "HttpStatusCode.GatewayTimeout"
        505 -> "HttpStatusCode.HttpVersionNotSupported"
        506 -> "HttpStatusCode.VariantAlsoNegotiates"
        507 -> "HttpStatusCode.InsufficientStorage"
        508 -> "HttpStatusCode.LoopDetected"
        510 -> "HttpStatusCode.NotExtended"
        511 -> "HttpStatusCode.NetworkAuthenticationRequired"
        else -> "HttpStatusCode($value, ${description.stringValue()})"
    }

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
context(ctx: Renderer)
private fun Model.renderDefault(): String = when (this) {
    is Model.Enum -> when (val enumDefault = default) {
        is Model.Default.Value<*> -> {
            val rawValue = enumDefault.value.toString()
            val enumValues = valueNames()
            val enumEntry = enumValues[rawValue] ?: enumValues.values.firstOrNull()
            if (enumEntry == null) "null" else "${toTypeName().type()}.$enumEntry"
        }
        else -> "null"
    }
    is Model.Primitive.Int -> (default as Model.Default.Value<*>).value.toString()
    is Model.Primitive.Long -> "${(default as Model.Default.Value<*>).value}L"
    is Model.Primitive.Float -> "${(default as Model.Default.Value<*>).value}f"
    is Model.Primitive.Double -> (default as Model.Default.Value<*>).value.toString()
    is Model.Primitive.Boolean -> (default as Model.Default.Value<*>).value.toString()
    is Model.Primitive.String -> "\"${(default as Model.Default.Value<*>).value}\""
    else -> "null" // Fallback for types where defaults aren't supported yet
}

/**
 * Returns whether this type needs `.toString()` when used in parameter building.
 * String and ByteArray types don't need conversion.
 */
private fun Model.needsToString(): Boolean = when (this) {
    is Model.Primitive.String, is Model.ByteArray -> false
    else -> true
}
