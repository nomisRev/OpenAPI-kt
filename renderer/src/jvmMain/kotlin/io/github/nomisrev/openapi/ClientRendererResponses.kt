@file:Suppress("TooManyFunctions")
package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.HttpStatusCode

private val HttpStatusCodeType = ClassName("io.ktor.http", "HttpStatusCode")

internal fun Route.Returns.needsSealedInterface(): Boolean {
    val totalCases = responses.size + (if (default != null) 1 else 0)
    return totalCases > 1
}

internal fun Route.buildResponseTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
): TypeSpec =
    when {
        returns.needsSealedInterface() -> buildSealedResponseTypeSpec(config, methodClassName, inlineModelScope)
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

private data class SealedResponseCaseRendering(
    val caseTypeSpec: TypeSpec,
    val inlineTypeSpec: TypeSpec?,
)

private data class SealedResponseDefaultRendering(
    val defaultTypeSpec: TypeSpec,
    val inlineTypeSpec: TypeSpec?,
)

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
    inlineModelScope: OperationInlineModelScope,
): TypeSpec {
    val responseClassName = methodClassName.nestedClass("Response")
    val hasMultipleStatuses = returns.responses.size + (if (returns.default != null) 1 else 0) > 1

    val builder = TypeSpec.interfaceBuilder("Response")
        .addModifiers(KModifier.SEALED)

    inlineModelScope.responseTypeSpecs(config).forEach(builder::addType)

    for ((statusCode, returnType) in returns.responses.entries.sortedBy { it.key.value }) {
        val rendering = buildSealedResponseCaseTypeSpec(
            config = config,
            responseClassName = responseClassName,
            statusCode = statusCode,
            returnType = returnType,
            hasMultipleStatuses = hasMultipleStatuses,
            inlineModelScope = inlineModelScope,
        )
        rendering.inlineTypeSpec?.let(builder::addType)
        builder.addType(rendering.caseTypeSpec)
    }

    returns.default?.let { defaultReturnType ->
        val rendering = buildSealedResponseDefaultTypeSpec(
            config = config,
            responseClassName = responseClassName,
            defaultReturnType = defaultReturnType,
            inlineModelScope = inlineModelScope,
        )
        rendering.inlineTypeSpec?.let(builder::addType)
        builder.addType(rendering.defaultTypeSpec)
    }

    return builder.build()
}

@Suppress("LongMethod")
private fun buildSealedResponseCaseTypeSpec(
    config: RenderConfig,
    responseClassName: ClassName,
    statusCode: HttpStatusCode,
    returnType: Route.ReturnType,
    hasMultipleStatuses: Boolean,
    inlineModelScope: OperationInlineModelScope,
): SealedResponseCaseRendering {
    val caseName = statusCode.toCaseName()
    val model = returnType.preferredModel()
    return if (model == null || model is Model.Primitive.Unit) {
        val caseBuilder = TypeSpec.objectBuilder(caseName)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(responseClassName)
        SealedResponseCaseRendering(
            caseTypeSpec = caseBuilder.build(),
            inlineTypeSpec = null,
        )
    } else {
        val hasMultipleContentTypes = returnType.types.size > 1
        if (model.isRouteInlineModel() && !hasMultipleContentTypes) {
            val caseTypeSpec = buildInlineResponseCaseTypeSpec(
                model = model,
                config = config,
                responseClassName = responseClassName,
                caseName = caseName,
                externalTypeNames = inlineModelScope.externalTypeNames(),
            ) ?: error("Expected inline response case type for $caseName")
            return SealedResponseCaseRendering(
                caseTypeSpec = caseTypeSpec,
                inlineTypeSpec = null,
            )
        }

        val bodyTypeName = bodyTypeName(
            statusCode = statusCode,
            contentType = null,
            hasMultipleStatuses = hasMultipleStatuses,
            hasMultipleContentTypes = false,
        )
        val inlineTypeSpec = if (model.isRouteInlineModel()) {
            buildInlineResponseBodyTypeSpec(
                model = model,
                config = config,
                ownerClassName = responseClassName,
                nameOverride = bodyTypeName,
                externalTypeNames = inlineModelScope.externalTypeNames(),
            )
        } else {
            null
        }
        val typeName = when {
            inlineTypeSpec != null -> responseClassName.nestedClass(
                bodyTypeName
            )
            else -> inlineModelScope.remap(model.toTypeName(config))
        }
        val caseBuilder = TypeSpec.classBuilder(caseName)
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
        SealedResponseCaseRendering(
            caseTypeSpec = caseBuilder.build(),
            inlineTypeSpec = inlineTypeSpec,
        )
    }
}

private fun buildInlineResponseCaseTypeSpec(
    model: Model,
    config: RenderConfig,
    responseClassName: ClassName,
    caseName: String,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec? =
    buildInlineResponseBodyTypeSpec(
        model = model,
        config = config,
        ownerClassName = responseClassName,
        nameOverride = caseName,
        externalTypeNames = externalTypeNames,
    )?.toBuilder()
        ?.addSuperinterface(responseClassName)
        ?.build()

private fun buildInlineResponseBodyTypeSpec(
    model: Model,
    config: RenderConfig,
    ownerClassName: ClassName,
    nameOverride: String,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec? =
    when (model) {
        is Model.Object -> model.toInlineOperationTypeSpecOrNull(
            config = config,
            ownerClassName = ownerClassName,
            nameOverride = nameOverride,
            externalTypeNames = externalTypeNames,
        )

        is Model.Enum -> model.toTypeSpec(config, nameOverride = nameOverride)

        is Model.Union -> model.toInlineOperationTypeSpecOrNull(
            config = config,
            ownerClassName = ownerClassName,
            nameOverride = nameOverride,
            externalTypeNames = externalTypeNames,
        )

        is Model.DiscriminatedObject -> model.toTypeSpec(
            config = config,
            classNameOverride = ownerClassName.nestedClass(nameOverride),
        )

        is Model.ByteArray,
        is Model.Collection,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Primitive,
        is Model.Reference,
        is Model.Uuid -> null
    }

private fun buildSealedResponseDefaultTypeSpec(
    config: RenderConfig,
    responseClassName: ClassName,
    defaultReturnType: Route.ReturnType,
    inlineModelScope: OperationInlineModelScope,
): SealedResponseDefaultRendering {
    val model = defaultReturnType.preferredModel()
    val bodyTypeName = defaultBodyTypeName(
        contentType = null,
        hasMultipleContentTypes = false,
    )
    val inlineTypeSpec = if (model != null && model.isRouteInlineModel()) {
        buildInlineResponseBodyTypeSpec(
            model = model,
            config = config,
            ownerClassName = responseClassName,
            nameOverride = bodyTypeName,
            externalTypeNames = inlineModelScope.externalTypeNames(),
        )
    } else {
        null
    }
    val valueType = when {
        inlineTypeSpec != null -> responseClassName.nestedClass(bodyTypeName)
        model != null -> inlineModelScope.remap(model.toTypeName(config))
        else -> null
    }

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
        val typeName = requireNotNull(valueType)
        constructorBuilder.addParameter("value", typeName)
        props.add(
            PropertySpec.builder("value", typeName)
                .initializer("value")
                .build()
        )
    }

    defaultBuilder.primaryConstructor(constructorBuilder.build())
    props.forEach { defaultBuilder.addProperty(it) }
    return SealedResponseDefaultRendering(
        defaultTypeSpec = defaultBuilder.build(),
        inlineTypeSpec = inlineTypeSpec,
    )
}

internal fun Route.Returns.singlePreferredModelOrNull(): Model? =
    (responses.values.firstOrNull() ?: default)?.preferredModel()
