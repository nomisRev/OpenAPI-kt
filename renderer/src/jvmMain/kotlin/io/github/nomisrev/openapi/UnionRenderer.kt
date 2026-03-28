package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import io.github.nomisrev.openapi.transformers.nestedOrNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

fun Model.Union.toTypeSpec(
    config: RenderConfig,
    classNameOverride: ClassName? = null,
    externalTypeNames: Map<ClassName, TypeName> = emptyMap(),
    parentInterface: ClassName? = null,
): TypeSpec {
    val originalClassName = context.toClassName(config)
    val className = classNameOverride ?: originalClassName
    return when (dispatch) {
        is UnionDispatch.NativeDiscriminator ->
            toNativeDiscriminatedTypeSpec(config, originalClassName, className, externalTypeNames, parentInterface)

        is UnionDispatch.TaggedCustom ->
            toTaggedCustomTypeSpec(config, originalClassName, className, externalTypeNames, parentInterface)

        UnionDispatch.Structural ->
            toStructuralTypeSpec(config, originalClassName, className, externalTypeNames, parentInterface)
    }
}

fun Model.Union.toFileSpec(config: RenderConfig): FileSpec {
    val className = context.toClassName(config)
    return FileSpec.builder(className.packageName, className.simpleName)
        .addType(toTypeSpec(config))
        .apply {
            if (this@toFileSpec.needsJsonObjectImport()) {
                addImport("kotlinx.serialization.json", "jsonObject")
            }
        }
        .build()
}

// ── Discriminated unions ────────────────────────────────────────────────────

private fun Model.Union.toNativeDiscriminatedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
    parentInterface: ClassName? = null,
): TypeSpec {
    val disc = (dispatch as? UnionDispatch.NativeDiscriminator)?.propertyName
        ?: error("Native union renderer requires NativeDiscriminator dispatch: $dispatch")
    val renderedCases = cases.map { it.renderDiscriminatedCase(config, originalClassName, className, disc, externalTypeNames) }

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(UnionOptInType)
                .addMember("%T::class", ExperimentalSerializationApi::class)
                .build()
        )
        .addAnnotation(
            AnnotationSpec.builder(UnionJsonClassDiscriminatorType)
                .addMember("%S", disc)
                .build()
        )
        .addAnnotation(Serializable::class)

    parentInterface?.let(builder::addSuperinterface)

    val optInClasses = buildList {
        if (renderedCases.any { it.usesUuid }) add(UnionExperimentalUuidApiType)
        if (renderedCases.any { it.usesInstant }) add(UnionExperimentalTimeType)
    }
    if (optInClasses.isNotEmpty()) {
        builder.addAnnotation(
            AnnotationSpec.builder(UnionOptInType)
                .apply { optInClasses.forEach { addMember("%T::class", it) } }
                .build()
        )
    }

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.unionEscapeForKdoc()) }

    renderedCases.forEach { builder.addType(it.typeSpec) }
    return builder.build()
}
// ── Custom serializer unions ────────────────────────────────────────────────

private fun Model.Union.toStructuralTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
    parentInterface: ClassName? = null,
): TypeSpec =
    toCustomSerializedTypeSpec(
        config = config,
        originalClassName = originalClassName,
        className = className,
        externalTypeNames = externalTypeNames,
        parentInterface = parentInterface,
        allowOpenEnum = true,
    )

private fun Model.Union.toTaggedCustomTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
    parentInterface: ClassName? = null,
): TypeSpec =
    toCustomSerializedTypeSpec(
        config = config,
        originalClassName = originalClassName,
        className = className,
        externalTypeNames = externalTypeNames,
        parentInterface = parentInterface,
        allowOpenEnum = false,
    )

private fun Model.Union.toCustomSerializedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
    parentInterface: ClassName? = null,
    allowOpenEnum: Boolean,
): TypeSpec {
    val openEnum = detectOpenEnum().takeIf { allowOpenEnum }
    if (openEnum != null) return buildOpenEnumTypeSpec(config, className, openEnum, parentInterface)

    val collidingSingleDiscriminatorTags = cases.collidingSingleDiscriminatorTags()
    val renderedCases = cases.map {
        it.renderNonDiscriminatedCase(
            config = config,
            originalClassName = originalClassName,
            parentInterface = className,
            collidingSingleDiscriminatorTags = collidingSingleDiscriminatorTags,
            externalTypeNames = externalTypeNames,
        )
    }

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T.Serializer::class", className)
                .build()
        )

    parentInterface?.let(builder::addSuperinterface)

    val optInClasses = buildList {
        if (renderedCases.any { it.usesUuid }) add(UnionExperimentalUuidApiType)
        if (renderedCases.any { it.usesInstant }) add(UnionExperimentalTimeType)
    }
    if (optInClasses.isNotEmpty()) {
        builder.addAnnotation(
            AnnotationSpec.builder(UnionOptInType)
                .apply { optInClasses.forEach { addMember("%T::class", it) } }
                .build()
        )
    }

    description
        ?.takeIf { it.isNotBlank() }
        ?.let { builder.addKdoc("%L\n", it.unionEscapeForKdoc()) }

    renderedCases.forEach { builder.addType(it.typeSpec) }
    collectionNestedTypeSpecs(config, originalClassName, className, externalTypeNames).forEach(builder::addType)
    builder.addType(buildSerializerObject(config, originalClassName, className, renderedCases, externalTypeNames))
    return builder.build()
}

private fun Model.Union.collectionNestedTypeSpecs(
    config: RenderConfig,
    originalClassName: ClassName,
    renderedClassName: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): List<TypeSpec> =
    cases.asSequence()
        .mapNotNull { (it.model as? Model.Collection)?.inner?.nestedOrNull() }
        .filterIsInstance<Model.ContextHolder>()
        .distinctBy { it.context.toClassName(config).canonicalName }
        .mapNotNull { model ->
            val modelClassName = model.context.toClassName(config)
            val externalTargetClassName =
                externalTypeNames[modelClassName.copy(nullable = false)] as? ClassName
            if (externalTargetClassName != null && externalTargetClassName.enclosingClassName() != renderedClassName) {
                return@mapNotNull null
            }
            val classNameOverride = when {
                externalTargetClassName != null && externalTargetClassName.enclosingClassName() == renderedClassName ->
                    externalTargetClassName

                renderedClassName != originalClassName ->
                    modelClassName.remapNestedClassName(originalClassName, renderedClassName) as? ClassName

                else -> null
            }
            when (model) {
                is Model.Enum -> model.toTypeSpec(config, nameOverride = classNameOverride?.simpleName)
                is Model.Object -> model.toTypeSpec(
                    config,
                    classNameOverride = classNameOverride,
                    externalTypeNames = externalTypeNames,
                )
                is Model.Union -> model.toTypeSpec(
                    config,
                    classNameOverride = classNameOverride,
                    externalTypeNames = externalTypeNames,
                )
                is Model.DiscriminatedObject -> model.toTypeSpec(config, classNameOverride = classNameOverride)
                is Model.Reference -> null
            }
        }
        .toList()
