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
): TypeSpec {
    val originalClassName = context.toClassName(config)
    val className = classNameOverride ?: originalClassName
    return if (discriminator != null) toDiscriminatedTypeSpec(config, originalClassName, className, externalTypeNames)
    else toNonDiscriminatedTypeSpec(config, originalClassName, className, externalTypeNames)
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

private fun Model.Union.toDiscriminatedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec {
    val disc = requireNotNull(discriminator)
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
// ── Non-discriminated unions ────────────────────────────────────────────────

private fun Model.Union.toNonDiscriminatedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    className: ClassName,
    externalTypeNames: Map<ClassName, TypeName>,
): TypeSpec {
    val openEnum = detectOpenEnum()
    if (openEnum != null) return buildOpenEnumTypeSpec(config, className, openEnum)

    val renderedCases = cases.map { it.renderNonDiscriminatedCase(config, originalClassName, className, externalTypeNames) }

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T.Serializer::class", className)
                .build()
        )

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
    collectionNestedTypeSpecs(config, externalTypeNames).forEach(builder::addType)
    builder.addType(buildSerializerObject(config, originalClassName, className, renderedCases, externalTypeNames))
    return builder.build()
}

private fun Model.Union.collectionNestedTypeSpecs(
    config: RenderConfig,
    externalTypeNames: Map<ClassName, TypeName>,
): List<TypeSpec> =
    cases.asSequence()
        .mapNotNull { (it.model as? Model.Collection)?.inner?.nestedOrNull() }
        .filterIsInstance<Model.ContextHolder>()
        .distinctBy { it.context.toClassName(config).canonicalName }
        .mapNotNull { model ->
            when (model) {
                is Model.Enum -> model.toTypeSpec(config)
                is Model.Object -> model.toTypeSpec(config, externalTypeNames = externalTypeNames)
                is Model.Union -> model.toTypeSpec(config, externalTypeNames = externalTypeNames)
                is Model.DiscriminatedObject -> model.toTypeSpec(config)
                is Model.Reference -> null
            }
        }
        .toList()
