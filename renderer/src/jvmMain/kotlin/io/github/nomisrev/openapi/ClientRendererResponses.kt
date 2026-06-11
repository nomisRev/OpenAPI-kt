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

internal fun Route.buildMultiContentTypeResponseSpecs(
    config: RenderConfig,
    methodClassName: ClassName,
    inlineModelScope: OperationInlineModelScope,
): List<TypeSpec> {
    val strategy = returns.contentTypeStrategy() as? ContentTypeStrategy.SeparateMethods ?: return emptyList()
    val responseInterfaces = strategy.successContentTypes.fold(linkedMapOf<ContentType, ClassName>()) { acc, contentType ->
        val simpleName = contentTypeResponseTypeName(contentType, acc.values.mapTo(mutableSetOf()) { it.simpleName })
        acc[contentType] = methodClassName.nestedClass(simpleName)
        acc
    }
    val hasMultipleStatuses = returns.responses.size + (if (returns.default != null) 1 else 0) > 1
    val allResponseInterfaces = responseInterfaces.values.toList()
    val specs = mutableListOf<TypeSpec>()

    for (contentType in strategy.successContentTypes) {
        val responseClassName = responseInterfaces.getValue(contentType)
        val builder = TypeSpec.interfaceBuilder(responseClassName.simpleName)
            .addModifiers(KModifier.SEALED)

        for ([statusCode, returnType] in returns.responses.entries.sortedBy { it.key.value }) {
            if (!statusCode.isSuccessStatusCode()) continue
            val model = returnType.types[contentType] ?: continue
            val rendering = buildMultiContentTypeResponseCaseTypeSpec(
                config = config,
                responseClassName = responseClassName,
                statusCode = statusCode,
                contentType = contentType,
                model = model,
                hasMultipleStatuses = hasMultipleStatuses,
                hasMultipleContentTypes = true,
                inlineModelScope = inlineModelScope,
            )
            rendering.inlineTypeSpec?.let(builder::addType)
            builder.addType(rendering.caseTypeSpec)
        }

        specs += builder.build()
    }

    for ([statusCode, returnType] in returns.responses.entries.sortedBy { it.key.value }) {
        if (statusCode.isSuccessStatusCode()) {
            if (returnType.types.isEmpty()) {
                specs += buildSharedNoContentCaseTypeSpec(
                    caseName = statusCode.toCaseName(),
                    superinterfaces = allResponseInterfaces,
                )
            }
            continue
        }

        when (val errorStrategy = classifyErrorStatus(statusCode, returnType)) {
            ErrorCaseStrategy.NoContent -> {
                specs += buildSharedNoContentCaseTypeSpec(
                    caseName = statusCode.toCaseName(),
                    superinterfaces = allResponseInterfaces,
                )
            }

            is ErrorCaseStrategy.SingleContentType -> {
                specs += buildSharedResponseCaseTypeSpec(
                    config = config,
                    methodClassName = methodClassName,
                    superinterfaces = allResponseInterfaces,
                    caseName = statusCode.toCaseName(),
                    statusCode = statusCode,
                    contentType = errorStrategy.contentType,
                    model = errorStrategy.model,
                    hasMultipleStatuses = hasMultipleStatuses,
                    hasMultipleContentTypes = false,
                    inlineModelScope = inlineModelScope,
                )
            }

            is ErrorCaseStrategy.MultipleContentTypes -> {
                errorStrategy.variants.forEach { [contentType, model] ->
                    val caseName = "${contentTypeToIdentifier(contentType)}${statusCode.toCaseName()}"
                    specs += buildSharedResponseCaseTypeSpec(
                        config = config,
                        methodClassName = methodClassName,
                        superinterfaces = allResponseInterfaces,
                        caseName = caseName,
                        statusCode = statusCode,
                        contentType = contentType,
                        model = model,
                        hasMultipleStatuses = hasMultipleStatuses,
                        hasMultipleContentTypes = true,
                        inlineModelScope = inlineModelScope,
                    )
                }
            }
        }
    }

    returns.default?.let { defaultReturnType ->
        val rendering = buildMultiContentTypeDefaultTypeSpec(
            config = config,
            methodClassName = methodClassName,
            defaultReturnType = defaultReturnType,
            superinterfaces = allResponseInterfaces,
            inlineModelScope = inlineModelScope,
        )
        specs += if (rendering.inlineTypeSpec != null) {
            rendering.defaultTypeSpec.toBuilder()
                .addType(rendering.inlineTypeSpec)
                .build()
        } else {
            rendering.defaultTypeSpec
        }
    }

    return specs
}

private fun buildDataResponseTypeSpec(
    model: Model,
    config: RenderConfig,
    inlineModelScope: OperationInlineModelScope,
): TypeSpec {
    val typeName = inlineModelScope.remapResponseType(model, config)
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
    contentType: ContentType? = null,
): TypeName =
    when {
        returns.isSingleUnitResponse() -> com.squareup.kotlinpoet.UNIT
        returns.isSingleDirectModelResponse() ->
            inlineModelScope.remapResponseType(requireNotNull(returns.singlePreferredModelOrNull(contentType)), config)

        contentType != null && returns.contentTypeStrategy() is ContentTypeStrategy.SeparateMethods ->
            methodClassName.nestedClass(contentTypeResponseTypeName(contentType))

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

    for ([statusCode, returnType] in returns.responses.entries.sortedBy { it.key.value }) {
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
            else -> inlineModelScope.remapResponseType(model, config)
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
        model != null -> inlineModelScope.remapResponseType(model, config)
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

@Suppress("LongMethod")
private fun buildMultiContentTypeResponseCaseTypeSpec(
    config: RenderConfig,
    responseClassName: ClassName,
    statusCode: HttpStatusCode,
    contentType: ContentType,
    model: Model,
    hasMultipleStatuses: Boolean,
    hasMultipleContentTypes: Boolean,
    inlineModelScope: OperationInlineModelScope,
): SealedResponseCaseRendering {
    val caseName = statusCode.toCaseName()
    val directInlineCase = if (model.isRouteInlineModel()) {
        buildInlineResponseCaseTypeSpec(
            model = model,
            config = config,
            responseClassName = responseClassName,
            caseName = caseName,
            externalTypeNames = inlineModelScope.externalTypeNames(),
        )
    } else {
        null
    }
    if (directInlineCase != null) {
        return SealedResponseCaseRendering(
            caseTypeSpec = directInlineCase,
            inlineTypeSpec = null,
        )
    }

    val bodyTypeName = bodyTypeName(
        statusCode = statusCode,
        contentType = contentType,
        hasMultipleStatuses = hasMultipleStatuses,
        hasMultipleContentTypes = hasMultipleContentTypes,
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
        inlineTypeSpec != null -> responseClassName.nestedClass(bodyTypeName)
        else -> inlineModelScope.remapResponseType(model, config)
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
    return SealedResponseCaseRendering(
        caseTypeSpec = caseBuilder.build(),
        inlineTypeSpec = inlineTypeSpec,
    )
}

@Suppress("LongMethod")
private fun buildSharedResponseCaseTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
    superinterfaces: List<TypeName>,
    caseName: String,
    statusCode: HttpStatusCode,
    contentType: ContentType,
    model: Model,
    hasMultipleStatuses: Boolean,
    hasMultipleContentTypes: Boolean,
    inlineModelScope: OperationInlineModelScope,
): TypeSpec {
    val firstSuperinterface = requireNotNull(superinterfaces.firstOrNull()) {
        "Shared response cases require at least one success interface"
    }
    val directInlineCase = if (model.isRouteInlineModel()) {
        buildInlineResponseCaseTypeSpec(
            model = model,
            config = config,
            responseClassName = firstSuperinterface as ClassName,
            caseName = caseName,
            externalTypeNames = inlineModelScope.externalTypeNames(),
        )
    } else {
        null
    }
    if (directInlineCase != null) {
        return directInlineCase.toBuilder().apply {
            superinterfaces.drop(1).forEach(::addSuperinterface)
        }.build()
    }

    val caseClassName = methodClassName.nestedClass(caseName)
    val bodyTypeName = bodyTypeName(
        statusCode = statusCode,
        contentType = contentType,
        hasMultipleStatuses = hasMultipleStatuses,
        hasMultipleContentTypes = hasMultipleContentTypes,
    )
    val inlineTypeSpec = if (model.isRouteInlineModel()) {
        buildInlineResponseBodyTypeSpec(
            model = model,
            config = config,
            ownerClassName = caseClassName,
            nameOverride = bodyTypeName,
            externalTypeNames = inlineModelScope.externalTypeNames(),
        )
    } else {
        null
    }
    val typeName = when {
        inlineTypeSpec != null -> caseClassName.nestedClass(bodyTypeName)
        else -> inlineModelScope.remapResponseType(model, config)
    }
    return TypeSpec.classBuilder(caseName)
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
            superinterfaces.forEach(::addSuperinterface)
            inlineTypeSpec?.let(::addType)
        }
        .build()
}

private fun buildSharedNoContentCaseTypeSpec(
    caseName: String,
    superinterfaces: List<TypeName>,
): TypeSpec =
    TypeSpec.objectBuilder(caseName)
        .addModifiers(KModifier.DATA)
        .apply {
            superinterfaces.forEach(::addSuperinterface)
        }
        .build()

private fun buildMultiContentTypeDefaultTypeSpec(
    config: RenderConfig,
    methodClassName: ClassName,
    defaultReturnType: Route.ReturnType,
    superinterfaces: List<TypeName>,
    inlineModelScope: OperationInlineModelScope,
): SealedResponseDefaultRendering {
    val model = defaultReturnType.preferredModel()
    val defaultClassName = methodClassName.nestedClass("Default")
    val bodyTypeName = defaultBodyTypeName(
        contentType = null,
        hasMultipleContentTypes = defaultReturnType.types.size > 1,
    )
    val inlineTypeSpec = if (model != null && model.isRouteInlineModel()) {
        buildInlineResponseBodyTypeSpec(
            model = model,
            config = config,
            ownerClassName = defaultClassName,
            nameOverride = bodyTypeName,
            externalTypeNames = inlineModelScope.externalTypeNames(),
        )
    } else {
        null
    }
    val valueType = when {
        inlineTypeSpec != null -> defaultClassName.nestedClass(bodyTypeName)
        model != null -> inlineModelScope.remapResponseType(model, config)
        else -> null
    }

    val defaultBuilder = TypeSpec.classBuilder("Default")
        .addModifiers(KModifier.DATA)
        .apply {
            superinterfaces.forEach(::addSuperinterface)
        }

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

private fun Route.Returns.singlePreferredModelOrNull(contentType: ContentType?): Model? {
    if (contentType == null || contentTypeStrategy() !is ContentTypeStrategy.SeparateMethods) {
        return singlePreferredModelOrNull()
    }
    return responses.entries
        .asSequence()
        .filter { it.key.isSuccessStatusCode() }
        .mapNotNull { [_, returnType] ->
            returnType.types.entries.firstOrNull { [candidate, _] ->
                candidate.match(contentType) || contentType.match(candidate)
            }?.value
        }
        .firstOrNull()
        ?: singlePreferredModelOrNull()
}
