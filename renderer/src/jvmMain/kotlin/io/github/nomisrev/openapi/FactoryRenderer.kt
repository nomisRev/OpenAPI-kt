package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import io.github.nomisrev.openapi.parser.Server

private val HttpClientType = ClassName("io.ktor.client", "HttpClient")
private val HttpClientConfigType = ClassName("io.ktor.client", "HttpClientConfig")
private val ContentNegotiationType =
    ClassName("io.ktor.client.plugins.contentnegotiation", "ContentNegotiation")
private val JsonMember = MemberName("io.ktor.serialization.kotlinx.json", "json")
private val DefaultRequestMember = MemberName("io.ktor.client.plugins", "defaultRequest")

/** Generate the factory function(s) for creating the client. */
fun ApiTree.generateFactory(config: RenderConfig): List<FunSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()

    val rootName = name.toPascalCase()
    val rootClassName = ClassName(config.apiPackage, rootName)
    val factoryName = "${rootName}Client"
    val implName = "Ktor$rootName"
    val configLambdaType = LambdaTypeName.get(
        receiver = HttpClientConfigType.parameterizedBy(STAR),
        returnType = UNIT,
    )

    val result = mutableListOf<FunSpec>()

    if (servers.isEmpty()) {
        // Simple factory with baseUrl: String
        result.add(
            FunSpec.builder(factoryName)
                .addParameter("baseUrl", String::class)
                .addParameter(
                    ParameterSpec.builder("block", configLambdaType)
                        .defaultValue("{}")
                        .build()
                )
                .returns(rootClassName)
                .addCode(buildFactoryBody(implName))
                .build()
        )
    } else {
        // Server sealed interface exists: factory with server parameter
        val serverClassName = ClassName(config.apiPackage, "${rootName}Server")
        val defaultServer = servers.first()
        val defaultServerName = defaultServer.toServerCaseName()
        val hasVariables = !defaultServer.variables.isNullOrEmpty()
        val defaultExpr = if (hasVariables) "$defaultServerName()" else defaultServerName

        // Factory with server parameter
        result.add(
            FunSpec.builder(factoryName)
                .addParameter(
                    ParameterSpec.builder("server", serverClassName)
                        .defaultValue("%T.%L", serverClassName, defaultExpr)
                        .build()
                )
                .addParameter(
                    ParameterSpec.builder("block", configLambdaType)
                        .defaultValue("{}")
                        .build()
                )
                .returns(rootClassName)
                .addCode(buildFactoryBodyWithServer(implName))
                .build()
        )

        // Also provide baseUrl overload
        result.add(
            FunSpec.builder(factoryName)
                .addParameter("baseUrl", String::class)
                .addParameter(
                    ParameterSpec.builder("block", configLambdaType)
                        .defaultValue("{}")
                        .build()
                )
                .returns(rootClassName)
                .addCode(buildFactoryBody(implName))
                .build()
        )
    }

    return result
}

/** Generate the server sealed interface if servers are defined. */
fun ApiTree.generateServerInterface(config: RenderConfig): TypeSpec? {
    if (servers.isEmpty()) return null

    val rootName = name.toPascalCase()
    val serverInterfaceName = "${rootName}Server"
    val serverClassName = ClassName(config.apiPackage, serverInterfaceName)

    val builder = TypeSpec.interfaceBuilder(serverInterfaceName)
        .addModifiers(KModifier.SEALED)
        .addProperty(
            PropertySpec.builder("url", String::class)
                .build()
        )

    // Each server becomes a case
    for (server in servers) {
        builder.addType(server.toServerCase(config, serverClassName))
    }

    // Always add Custom case
    builder.addType(
        TypeSpec.classBuilder("Custom")
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("url", String::class)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("url", String::class)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer("url")
                    .build()
            )
            .addSuperinterface(serverClassName)
            .build()
    )

    return builder.build()
}

/** Convert a Server to a sealed interface case. */
private fun Server.toServerCase(config: RenderConfig, serverClassName: ClassName): TypeSpec {
    val caseName = toServerCaseName()
    val hasVariables = !variables.isNullOrEmpty()

    return if (hasVariables) {
        buildServerCaseWithVariables(caseName, serverClassName, config)
    } else {
        // data object with fixed url
        TypeSpec.objectBuilder(caseName)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(serverClassName)
            .addProperty(
                PropertySpec.builder("url", String::class)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer("%S", url)
                    .build()
            )
            .build()
    }
}

/** Build a server case with variables (data class with constructor params). */
private fun Server.buildServerCaseWithVariables(
    caseName: String,
    serverClassName: ClassName,
    config: RenderConfig,
): TypeSpec {
    val caseClassName = serverClassName.nestedClass(caseName)
    val builder = TypeSpec.classBuilder(caseName)
        .addModifiers(KModifier.DATA)
        .addSuperinterface(serverClassName)

    val ctorBuilder = FunSpec.constructorBuilder()

    for ((varName, variable) in variables!!) {
        val paramName = varName.toCamelCase()
        if (!variable.enum.isNullOrEmpty()) {
            // Generate enum type for variable
            val enumName = varName.toPascalCase()
            val enumClassName = caseClassName.nestedClass(enumName)
            builder.addType(buildVariableEnum(enumName, variable))

            ctorBuilder.addParameter(
                ParameterSpec.builder(paramName, enumClassName)
                    .defaultValue(
                        "%T.%L",
                        enumClassName,
                        toEnumValueName(variable.default),
                    )
                    .build()
            )
            builder.addProperty(
                PropertySpec.builder(paramName, enumClassName)
                    .initializer(paramName)
                    .build()
            )
        } else {
            ctorBuilder.addParameter(
                ParameterSpec.builder(paramName, String::class)
                    .defaultValue("%S", variable.default)
                    .build()
            )
            builder.addProperty(
                PropertySpec.builder(paramName, String::class)
                    .initializer(paramName)
                    .build()
            )
        }
    }

    builder.primaryConstructor(ctorBuilder.build())

    // Build url property with string template
    val urlTemplate = buildServerUrlTemplate(url, variables!!)
    builder.addProperty(
        PropertySpec.builder("url", String::class)
            .addModifiers(KModifier.OVERRIDE)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return %L", urlTemplate)
                    .build()
            )
            .build()
    )

    return builder.build()
}

/** Build an enum for a server variable. */
private fun buildVariableEnum(name: String, variable: Server.Variable): TypeSpec {
    val builder = TypeSpec.enumBuilder(name)
    builder.primaryConstructor(
        FunSpec.constructorBuilder()
            .addParameter("value", String::class)
            .build()
    )
    builder.addProperty(
        PropertySpec.builder("value", String::class)
            .initializer("value")
            .build()
    )

    for (enumValue in variable.enum!!) {
        builder.addEnumConstant(
            toEnumValueName(enumValue),
            TypeSpec.anonymousClassBuilder()
                .addSuperclassConstructorParameter("%S", enumValue)
                .build()
        )
    }

    return builder.build()
}

/** Build the url string template for a server with variables. */
private fun buildServerUrlTemplate(urlTemplate: String, variables: Map<String, Server.Variable>): String {
    var result = urlTemplate
    for ((varName, variable) in variables) {
        val paramName = varName.toCamelCase()
        val hasEnum = !variable.enum.isNullOrEmpty()
        val replacement = if (hasEnum) "\${$paramName.value}" else "\$$paramName"
        result = result.replace("{$varName}", replacement)
    }
    return "\"$result\""
}

/** Derive server case name from description or URL. */
private fun Server.toServerCaseName(): String {
    if (description != null) {
        return description!!
            .replace(Regex("\\s+[Ss]erver\\s*$"), "")
            .toPascalCase()
    }
    // Fallback: derive from URL
    return url
        .removePrefix("https://")
        .removePrefix("http://")
        .split("/", ".", "-")
        .filter { it.isNotEmpty() && !it.startsWith("{") }
        .take(2)
        .joinToString("") { it.toPascalCase() }
        .ifEmpty { "Default" }
}

/** Build factory function body with baseUrl parameter. */
private fun buildFactoryBody(implName: String): CodeBlock {
    val code = CodeBlock.builder()
    code.beginControlFlow("val client = %T", HttpClientType)
    code.addStatement("install(%T) { %M() }", ContentNegotiationType, JsonMember)
    code.addStatement("%M { url(baseUrl) }", DefaultRequestMember)
    code.addStatement("block()")
    code.endControlFlow()
    code.addStatement("return %L(client)", implName)
    return code.build()
}

/** Build factory function body with server parameter. */
private fun buildFactoryBodyWithServer(implName: String): CodeBlock {
    val code = CodeBlock.builder()
    code.beginControlFlow("val client = %T", HttpClientType)
    code.addStatement("install(%T) { %M() }", ContentNegotiationType, JsonMember)
    code.addStatement("%M { url(server.url) }", DefaultRequestMember)
    code.addStatement("block()")
    code.endControlFlow()
    code.addStatement("return %L(client)", implName)
    return code.build()
}
