package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import io.github.nomisrev.openapi.transformers.isTopLevel

internal data class RenderedUnionCase(
    val typeSpec: TypeSpec,
    val usesUuid: Boolean,
    val usesInstant: Boolean = false,
    val isInlined: Boolean = false,
    val caseSimpleName: String = typeSpec.name.orEmpty(),
    val isNestedUnion: Boolean = false,
)

internal fun Model.Union.Case.renderDiscriminatedCase(
    config: RenderConfig,
    originalClassName: ClassName,
    parentInterface: ClassName,
    discriminatorField: String,
    externalTypeNames: Map<ClassName, TypeName>,
): RenderedUnionCase {
    val serialName = serialNameOrNull(discriminatorField)
    val caseName = serialName?.toPascalCase() ?: caseSimpleName(config)
    return when (val caseModel = model) {
        is Model.Object ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(
                        config,
                        parentInterface,
                        serialName,
                        nameOverride = caseName,
                        classNameOverride = parentInterface.nestedClass(caseName),
                        externalTypeNames = externalTypeNames,
                    ),
                    usesUuid = false,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, caseName, serialName, externalTypeNames)
            }

        is Model.Enum ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(caseModel.toTypeSpec(config, parentInterface, serialName, nameOverride = caseName), usesUuid = false)
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, caseName, serialName, externalTypeNames)
            }

        is Model.Primitive.Unit -> {
            val typeSpec = TypeSpec.objectBuilder(caseName)
                .addModifiers(KModifier.DATA)
                .addSuperinterface(parentInterface)
                .addAnnotation(Serializable::class)
                .apply { serialName?.let(::addSerialNameAnnotation) }
                .build()
            RenderedUnionCase(typeSpec, usesUuid = false)
        }

        else -> renderWrappedTypeSpec(config, originalClassName, parentInterface, caseName, serialName, externalTypeNames)
    }
}

internal fun Model.Union.Case.renderNonDiscriminatedCase(
    config: RenderConfig,
    originalClassName: ClassName,
    parentInterface: ClassName,
    collidingSingleDiscriminatorTags: Set<String> = emptySet(),
    externalTypeNames: Map<ClassName, TypeName>,
): RenderedUnionCase {
    val simpleName = caseSimpleName(config, collidingSingleDiscriminatorTags)
    return when (val caseModel = model) {
        is Model.Object ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(
                        config,
                        parentInterface,
                        classNameOverride = parentInterface.nestedClass(simpleName),
                        externalTypeNames = externalTypeNames,
                    ),
                    usesUuid = false,
                    isInlined = true,
                    caseSimpleName = simpleName,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            }

        is Model.Enum ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(config, parentInterface),
                    usesUuid = false,
                    isInlined = true,
                    caseSimpleName = simpleName,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            }

        is Model.Primitive.Unit -> {
            val typeSpec = TypeSpec.objectBuilder(simpleName)
                .addModifiers(KModifier.DATA)
                .addSuperinterface(parentInterface)
                .addAnnotation(Serializable::class)
                .build()
            RenderedUnionCase(typeSpec, usesUuid = false, isInlined = true, caseSimpleName = simpleName)
        }

        is Model.Union ->
            if (!caseModel.context.isTopLevel()) {
                RenderedUnionCase(
                    caseModel.toTypeSpec(config, externalTypeNames = externalTypeNames),
                    usesUuid = false,
                    isInlined = false,
                    caseSimpleName = simpleName,
                    isNestedUnion = true,
                )
            } else {
                renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
            }

        is Model.DiscriminatedObject,
        is Model.ByteArray,
        is Model.Collection,
        is Model.Date,
        is Model.DateTime,
        is Model.FreeFormJson,
        is Model.Primitive,
        is Model.Reference,
        is Model.Uuid -> renderWrappedTypeSpec(config, originalClassName, parentInterface, simpleName, null, externalTypeNames)
    }
}

@Suppress("LongParameterList")
private fun Model.Union.Case.renderWrappedTypeSpec(
    config: RenderConfig,
    originalClassName: ClassName,
    parentInterface: ClassName,
    simpleName: String,
    serialName: String?,
    externalTypeNames: Map<ClassName, TypeName>,
): RenderedUnionCase {
    val valueType = model.toTypeName(config)
        .remapNestedClassName(originalClassName, parentInterface)
        .remapTypeNames(externalTypeNames)
    val typeSpec = TypeSpec.classBuilder(simpleName)
        .addModifiers(KModifier.VALUE)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(ParameterSpec.builder("value", valueType).build())
                .build()
        )
        .addProperty(
            PropertySpec.builder("value", valueType)
                .initializer("value")
                .build()
        )
        .addSuperinterface(parentInterface)
        .addAnnotation(Serializable::class)
        .apply {
            if (KmpTarget.JVM in config.targets) {
                addAnnotation(JvmInline::class)
            }
            serialName?.let(::addSerialNameAnnotation)
        }
        .build()
    return RenderedUnionCase(typeSpec, valueType.usesExperimentalUuid(), usesInstant = valueType.usesInstant(), caseSimpleName = simpleName)
}
