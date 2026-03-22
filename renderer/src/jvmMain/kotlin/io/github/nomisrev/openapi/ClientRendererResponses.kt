@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlin.contracts.contract

private val HttpStatusCodeType = ClassName("io.ktor.http", "HttpStatusCode")

private fun Route.ReturnType.preferredModel(): Model? {
    if (types.isEmpty()) return null
    val jsonEntry = types.entries.firstOrNull { ContentType.Application.Json.match(it.key) }
    return jsonEntry?.value ?: types.values.first()
}

internal fun Route.Returns.needsSealedInterface(): Boolean {
    val totalCases = responses.size + (if (default != null) 1 else 0)
    return totalCases > 1
}

private fun HttpStatusCode.toCaseName(): String =
    description.split(" ").joinToString("") { word ->
        word.lowercase().replaceFirstChar { it.uppercase() }
    }

internal fun Route.buildResponseTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
): TypeSpec =
    when {
        returns.needsSealedInterface() -> buildSealedResponseTypeSpec(config, methodClassName)
        else -> {
            val model = returns.singlePreferredModelOrNull()
            when {
                model != null && model.isRouteInlineModel() ->
                    model.toInlineOperationTypeSpecOrNull(
                        config = config,
                        ownerClassName = methodClassName,
                        nameOverride = "Response",
                        externalTypeNames = inlineModelScope.externalTypeNames(),
                    ) ?: buildDataResponseTypeSpec(model, config, inlineModelScope)

                model == null || model is Model.Primitive.Unit ->
                    TypeSpec.objectBuilder("Response")
                        .addModifiers(KModifier.DATA)
                        .build()

                else -> buildDataResponseTypeSpec(model, config, inlineModelScope)
            }
        }
    }

private fun buildDataResponseTypeSpec(
    model: Model,
    config: RenderConfig,
    inlineModelScope: OperationInlineModelScope,
): TypeSpec {
    val typeName = inlineModelScope.remap(model.toTypeName(config))
    return TypeSpec.classBuilder("Response")
        .addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("value", typeName)
                .build()
        )
        .addProperty(
            PropertySpec.builder("value", typeName)
                .initializer("value")
                .build()
        )
        .apply {
            inlineModelScope.responseTypeSpecs(config).forEach(::addType)
        }
        .build()
}

@Suppress("UnsafeCallOnNullableType")
internal fun Route.invokeReturnType(
    config: RenderConfig,
    methodClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
): TypeName =
    when {
        returns.isSingleUnitResponse() -> com.squareup.kotlinpoet.UNIT
        returns.isSingleDirectModelResponse() ->
            inlineModelScope.remap(returns.singlePreferredModelOrNull()!!.toTypeName(config))

        else -> methodClassName.nestedClass("Response")
    }

internal fun Route.Returns.isSingleUnitResponse(): Boolean {
    if (needsSealedInterface()) return false
    val model = singlePreferredModelOrNull()
    return model == null || model is Model.Primitive.Unit
}

internal fun Route.Returns.isSingleDirectModelResponse(): Boolean {
    if (needsSealedInterface()) return false
    val model = singlePreferredModelOrNull()
    return model != null && model !is Model.Primitive.Unit && !model.isRouteInlineModel()
}

private fun Route.buildSealedResponseTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
): TypeSpec {
    val responseClassName = methodClassName.nestedClass("Response")

    val builder = TypeSpec.interfaceBuilder("Response")
        .addModifiers(KModifier.SEALED)

    for ((statusCode, returnType) in returns.responses.entries.sortedBy { it.key.value }) {
        builder.addType(buildSealedResponseCaseTypeSpec(config, responseClassName, statusCode, returnType))
    }

    returns.default?.let { defaultReturnType ->
        builder.addType(buildSealedResponseDefaultTypeSpec(config, responseClassName, defaultReturnType))
    }

    return builder.build()
}

private fun buildSealedResponseCaseTypeSpec(
    config: RenderConfig,
    responseClassName: ClassName,
    statusCode: HttpStatusCode,
    returnType: Route.ReturnType,
): TypeSpec {
    val caseName = statusCode.toCaseName()
    val model = returnType.preferredModel()
    return if (model == null || model is Model.Primitive.Unit) {
        TypeSpec.objectBuilder(caseName)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(responseClassName)
            .build()
    } else {
        val typeName = model.toTypeName(config)
        TypeSpec.classBuilder(caseName)
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("value", typeName)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("value", typeName)
                    .initializer("value")
                    .build()
            )
            .addSuperinterface(responseClassName)
            .build()
    }
}

private fun buildSealedResponseDefaultTypeSpec(
    config: RenderConfig,
    responseClassName: ClassName,
    defaultReturnType: Route.ReturnType,
): TypeSpec {
    val model = defaultReturnType.preferredModel()
    val defaultBuilder = TypeSpec.classBuilder("Default")
        .addModifiers(KModifier.DATA)
        .addSuperinterface(responseClassName)

    val constructorBuilder = FunSpec.constructorBuilder()
        .addParameter("status", HttpStatusCodeType)

    val props = mutableListOf(
        PropertySpec.builder("status", HttpStatusCodeType)
            .initializer("status")
            .build()
    )

    if (model != null && model !is Model.Primitive.Unit) {
        val typeName = model.toTypeName(config)
        constructorBuilder.addParameter("value", typeName)
        props.add(
            PropertySpec.builder("value", typeName)
                .initializer("value")
                .build()
        )
    }

    defaultBuilder.primaryConstructor(constructorBuilder.build())
    props.forEach { defaultBuilder.addProperty(it) }
    return defaultBuilder.build()
}

internal fun Route.Returns.singlePreferredModelOrNull(): Model? =
    (responses.values.firstOrNull() ?: default)?.preferredModel()
