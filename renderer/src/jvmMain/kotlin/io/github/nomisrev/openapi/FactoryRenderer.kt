@file:Suppress("TooManyFunctions")
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
import com.squareup.kotlinpoet.asTypeName
import io.github.nomisrev.openapi.parser.Server

internal val HttpClientType = ClassName("io.ktor.client", "HttpClient")
internal val HttpClientConfigType = ClassName("io.ktor.client", "HttpClientConfig")
internal val ContentNegotiationType =
    ClassName("io.ktor.client.plugins.contentnegotiation", "ContentNegotiation")
internal val JsonMember = MemberName("io.ktor.serialization.kotlinx.json", "json")
internal val DefaultRequestMember = MemberName("io.ktor.client.plugins", "defaultRequest")

/**
 * Describes the server configuration strategy derived from the server list:
 * - [NoServers]: no servers declared → use a `baseUrl: String` parameter.
 * - [SingleFixed]: exactly one server with no variables → inject the url directly as a string
 *   constant (no sealed interface generated).
 * - [SingleVariable]: exactly one server with variables → produce a standalone data class (no
 *   sealed interface; the data class exposes a `url` property).
 * - [Multiple]: more than one server → produce a sealed interface hierarchy.
 */
internal sealed interface ServerStrategy {
    data object NoServers : ServerStrategy
    data class SingleFixed(val url: String) : ServerStrategy
    data class SingleVariable(val server: Server, val serverClassName: ClassName) : ServerStrategy
    data class Multiple(val servers: List<Server>, val serverClassName: ClassName) : ServerStrategy
}

/** Determine the [ServerStrategy] for this [ApiTree]. */
internal fun ApiTree.serverStrategy(config: RenderConfig): ServerStrategy {
    val rootName = name.toPascalCase()
    return when {
        servers.isEmpty() -> ServerStrategy.NoServers
        servers.size == 1 && servers.first().variables.isNullOrEmpty() ->
            ServerStrategy.SingleFixed(servers.first().url)
        servers.size == 1 ->
            ServerStrategy.SingleVariable(
                server = servers.first(),
                serverClassName = ClassName(config.apiPackage, "${rootName}Server"),
            )
        else ->
            ServerStrategy.Multiple(
                servers = servers,
                serverClassName = ClassName(config.apiPackage, "${rootName}Server"),
            )
    }
}

/**
 * Generate the secondary constructors to be added to the root API class.
 *
 * Two constructors are produced for every strategy:
 * 1. A *bring-your-own-config* constructor — accepts a `block: HttpClientConfig<*>.() -> Unit`
 *    so the caller has full control; no plugins are installed implicitly.
 * 2. A *batteries-included* constructor — installs [ContentNegotiation] with JSON by default; no
 *    `block` parameter.
 */
internal fun ApiTree.generateSecondaryConstructors(config: RenderConfig): List<FunSpec> {
    if (children.isEmpty() && operations.isEmpty()) return emptyList()
    return buildSecondaryConstructors(serverStrategy(config))
}

private fun buildSecondaryConstructors(strategy: ServerStrategy): List<FunSpec> {
    return when (strategy) {
        is ServerStrategy.NoServers ->
            listOf(
                baseUrlConstructorWithBlock(),
                baseUrlConstructorNoBlock(),
            )

        is ServerStrategy.SingleFixed ->
            listOf(
                baseUrlConstructorWithBlock(),
                baseUrlConstructorNoBlock(),
            )

        is ServerStrategy.SingleVariable ->
            listOf(
                serverConstructorWithBlock(strategy.serverClassName),
                serverConstructorNoBlock(strategy.serverClassName),
            )

        is ServerStrategy.Multiple -> {
            val defaultServer = strategy.servers.first()
            val defaultCaseName = defaultServer.toServerCaseName()
            val hasVariables = !defaultServer.variables.isNullOrEmpty()
            val defaultExpr = CodeBlock.of(
                "%T.%L",
                strategy.serverClassName,
                if (hasVariables) "$defaultCaseName()" else defaultCaseName,
            )
            listOf(
                serverConstructorWithBlock(strategy.serverClassName, defaultExpr),
                serverConstructorNoBlock(strategy.serverClassName, defaultExpr),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Individual constructor builders
// ---------------------------------------------------------------------------

private fun baseUrlConstructorWithBlock(): FunSpec {
    val configLambdaType = LambdaTypeName.get(
        receiver = HttpClientConfigType.parameterizedBy(STAR),
        returnType = UNIT,
    )
    val httpClientArg = CodeBlock.builder()
        .beginControlFlow("%T", HttpClientType)
        .addStatement("%M { url(baseUrl) }", DefaultRequestMember)
        .addStatement("block()")
        .endControlFlow()
        .build()
    return FunSpec.constructorBuilder()
        .addParameter("baseUrl", String::class)
        .addParameter("block", configLambdaType)
        .callThisConstructor(httpClientArg)
        .build()
}

private fun baseUrlConstructorNoBlock(): FunSpec {
    val httpClientArg = CodeBlock.builder()
        .beginControlFlow("%T", HttpClientType)
        .addStatement("%M { url(baseUrl) }", DefaultRequestMember)
        .addStatement("install(%T) { %M() }", ContentNegotiationType, JsonMember)
        .endControlFlow()
        .build()
    return FunSpec.constructorBuilder()
        .addParameter("baseUrl", String::class)
        .callThisConstructor(httpClientArg)
        .build()
}

private fun serverConstructorWithBlock(
    serverClassName: ClassName,
    defaultExpr: CodeBlock? = null,
): FunSpec {
    val configLambdaType = LambdaTypeName.get(
        receiver = HttpClientConfigType.parameterizedBy(STAR),
        returnType = UNIT,
    )
    val paramBuilder = ParameterSpec.builder("server", serverClassName)
    if (defaultExpr != null) paramBuilder.defaultValue(defaultExpr)

    val httpClientArg = CodeBlock.builder()
        .beginControlFlow("%T", HttpClientType)
        .addStatement("%M { url(server.url) }", DefaultRequestMember)
        .addStatement("block()")
        .endControlFlow()
        .build()
    return FunSpec.constructorBuilder()
        .addParameter(paramBuilder.build())
        .addParameter("block", configLambdaType)
        .callThisConstructor(httpClientArg)
        .build()
}

private fun serverConstructorNoBlock(
    serverClassName: ClassName,
    defaultExpr: CodeBlock? = null,
): FunSpec {
    val paramBuilder = ParameterSpec.builder("server", serverClassName)
    if (defaultExpr != null) paramBuilder.defaultValue(defaultExpr)

    val httpClientArg = CodeBlock.builder()
        .beginControlFlow("%T", HttpClientType)
        .addStatement("%M { url(server.url) }", DefaultRequestMember)
        .addStatement("install(%T) { %M() }", ContentNegotiationType, JsonMember)
        .endControlFlow()
        .build()
    return FunSpec.constructorBuilder()
        .addParameter(paramBuilder.build())
        .callThisConstructor(httpClientArg)
        .build()
}

// ---------------------------------------------------------------------------
// Server type generation
// ---------------------------------------------------------------------------

/**
 * Generate the server type (sealed interface or standalone data class) if needed.
 *
 * Returns `null` for [ServerStrategy.NoServers] and [ServerStrategy.SingleFixed] (no type needed).
 */
internal fun ApiTree.generateServerType(config: RenderConfig): TypeSpec? {
    val strategy = serverStrategy(config)
    val rootName = name.toPascalCase()

    return when (strategy) {
        is ServerStrategy.NoServers, is ServerStrategy.SingleFixed -> null

        is ServerStrategy.SingleVariable -> {
            // Single server with variables → standalone data class (no sealed wrapper)
            strategy.server.buildServerCaseWithVariables(
                caseName = "${rootName}Server",
                serverClassName = strategy.serverClassName,
                asSealedCase = false,
            )
        }

        is ServerStrategy.Multiple -> {
            val serverInterfaceName = strategy.serverClassName.simpleName

            val builder = TypeSpec.interfaceBuilder(serverInterfaceName)
                .addModifiers(KModifier.SEALED)
                .addProperty(
                    PropertySpec.builder("url", String::class)
                        .build()
                )

            for (server in strategy.servers) {
                builder.addType(server.toServerCase(strategy.serverClassName))
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
                    .addSuperinterface(strategy.serverClassName)
                    .build()
            )

            builder.build()
        }
    }
}

/** Convert a Server to a sealed interface case (only called for [ServerStrategy.Multiple]). */
private fun Server.toServerCase(serverClassName: ClassName): TypeSpec {
    val caseName = toServerCaseName()
    return if (!variables.isNullOrEmpty()) {
        buildServerCaseWithVariables(caseName, serverClassName, asSealedCase = true)
    } else {
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

/**
 * Build a server case/class with variables.
 *
 * @param asSealedCase when `true`, the generated class implements [serverClassName] and `url` is
 *   `override`; when `false`, it is a standalone data class.
 */
private fun Server.buildServerCaseWithVariables(
    caseName: String,
    serverClassName: ClassName,
    asSealedCase: Boolean,
): TypeSpec {
    // For a sealed case, the enum lives inside the nested case class (e.g. ApiServer.Production.Environment).
    // For a standalone data class, the enum lives directly inside serverClassName (e.g. ApiServer.Environment).
    val caseClassName = if (asSealedCase) serverClassName.nestedClass(caseName) else serverClassName
    val builder = TypeSpec.classBuilder(caseName).addModifiers(KModifier.DATA)
    if (asSealedCase) builder.addSuperinterface(serverClassName)

    val ctorBuilder = FunSpec.constructorBuilder()

    for ((varName, variable) in variables!!) {
        val paramName = varName.toCamelCase()
        if (!variable.enum.isNullOrEmpty()) {
            val enumName = varName.toPascalCase()
            val enumClassName = caseClassName.nestedClass(enumName)
            builder.addType(buildVariableEnum(enumName, variable))
            ctorBuilder.addParameter(
                ParameterSpec.builder(paramName, enumClassName)
                    .defaultValue("%T.%L", enumClassName, toEnumValueName(variable.default))
                    .build()
            )
            builder.addProperty(
                PropertySpec.builder(paramName, enumClassName).initializer(paramName).build()
            )
        } else {
            ctorBuilder.addParameter(
                ParameterSpec.builder(paramName, String::class)
                    .defaultValue("%S", variable.default)
                    .build()
            )
            builder.addProperty(
                PropertySpec.builder(paramName, String::class).initializer(paramName).build()
            )
        }
    }

    builder.primaryConstructor(ctorBuilder.build())

    val urlTemplate = buildServerUrlTemplate(url, variables!!)
    val urlPropBuilder = PropertySpec.builder("url", String::class)
        .getter(FunSpec.getterBuilder().addStatement("return %L", urlTemplate).build())
    if (asSealedCase) urlPropBuilder.addModifiers(KModifier.OVERRIDE)
    builder.addProperty(urlPropBuilder.build())

    return builder.build()
}

private fun buildVariableEnum(name: String, variable: Server.Variable): TypeSpec {
    val builder = TypeSpec.enumBuilder(name)
    builder.primaryConstructor(
        FunSpec.constructorBuilder().addParameter("value", String::class).build()
    )
    builder.addProperty(
        PropertySpec.builder("value", String::class).initializer("value").build()
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

private fun buildServerUrlTemplate(
    urlTemplate: String,
    variables: Map<String, Server.Variable>,
): String {
    var result = urlTemplate
    for ((varName, variable) in variables) {
        val paramName = varName.toCamelCase()
        val hasEnum = !variable.enum.isNullOrEmpty()
        val replacement = if (hasEnum) "\${$paramName.value}" else "\$$paramName"
        result = result.replace("{$varName}", replacement)
    }
    return "\"$result\""
}

internal fun Server.toServerCaseName(): String {
    if (description != null) {
        return description!!
            .replace(Regex("\\s+[Ss]erver\\s*$"), "")
            .toPascalCase()
    }
    return url
        .removePrefix("https://")
        .removePrefix("http://")
        .split("/", ".", "-")
        .filter { it.isNotEmpty() && !it.startsWith("{") }
        .take(2)
        .joinToString("") { it.toPascalCase() }
        .ifEmpty { "Default" }
}
