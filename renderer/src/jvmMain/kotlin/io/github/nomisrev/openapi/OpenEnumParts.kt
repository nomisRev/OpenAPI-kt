package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

internal data class OpenEnumParts(
    val enumCase: Model.Union.Case,
    val enumModel: Model.Enum,
    val stringCase: Model.Union.Case,
)

internal fun Model.Union.detectOpenEnum(): OpenEnumParts? {
    if (cases.size != 2) return null
    val (a, b) = cases
    val aEnum = a.model as? Model.Enum
    val bEnum = b.model as? Model.Enum
    val aString = a.model as? Model.Primitive.String
    val bString = b.model as? Model.Primitive.String

    return when {
        aEnum != null && bString != null -> OpenEnumParts(a, aEnum, b)
        bEnum != null && aString != null -> OpenEnumParts(b, bEnum, a)
        else -> null
    }
}

internal fun Model.Union.buildOpenEnumTypeSpec(
    config: RenderConfig,
    className: ClassName,
    parts: OpenEnumParts,
): TypeSpec {
    val enumSimpleName = parts.enumCase.caseSimpleName(config)
    val stringSimpleName = parts.stringCase.caseSimpleName(config)

    val builder = TypeSpec.interfaceBuilder(className.simpleName)
        .addModifiers(KModifier.SEALED)
        .addAnnotation(
            com.squareup.kotlinpoet.AnnotationSpec.builder(Serializable::class)
                .addMember("with = %T.Serializer::class", className)
                .build()
        )
        .addProperty(PropertySpec.builder("value", STRING).build())

    val stringTypeSpec = TypeSpec.classBuilder(stringSimpleName)
        .addModifiers(KModifier.VALUE)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("value", STRING)
                .build()
        )
        .addProperty(
            PropertySpec.builder("value", STRING)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("value")
                .build()
        )
        .addSuperinterface(className)
        .addAnnotation(Serializable::class)
        .apply {
            if (KmpTarget.JVM in config.targets) addAnnotation(JvmInline::class)
        }
        .build()

    val enumTypeSpec = parts.enumModel.toTypeSpec(config, className, overrideValueProperty = true)

    builder.addType(stringTypeSpec)
    builder.addType(enumTypeSpec)
    builder.addType(buildOpenEnumSerializer(className, parts, enumSimpleName, stringSimpleName))

    return builder.build()
}

private fun buildOpenEnumSerializer(
    className: ClassName,
    parts: OpenEnumParts,
    enumSimpleName: String,
    stringSimpleName: String,
): TypeSpec {
    val enumClassName = className.nestedClass(enumSimpleName)
    val stringClassName = className.nestedClass(stringSimpleName)

    val serializeCode = CodeBlock.builder()
        .beginControlFlow("when(value)")
        .apply {
            parts.enumModel.values.forEach { rawValue ->
                val v = rawValue ?: "null"
                val entryName = toEnumValueName(v)
                addStatement("%T.%L -> encoder.encodeString(%S)", enumClassName, entryName, v)
            }
            addStatement("is %T -> encoder.encodeString(value.value)", stringClassName)
        }
        .endControlFlow()
        .build()

    val deserializeCode = CodeBlock.builder()
        .add("return ")
        .beginControlFlow("when(val value = decoder.decodeString())")
        .apply {
            parts.enumModel.values.forEach { rawValue ->
                val v = rawValue ?: "null"
                val entryName = toEnumValueName(v)
                addStatement("%S -> %T.%L", v, enumClassName, entryName)
            }
            addStatement("else -> %T(value)", stringClassName)
        }
        .endControlFlow()
        .build()

    return TypeSpec.objectBuilder("Serializer")
        .addSuperinterface(UnionKSerializerType.parameterizedBy(className))
        .addProperty(
            PropertySpec.builder("descriptor", UnionSerialDescriptorType)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%T.%M().descriptor", kotlin.String::class, SerializerMember)
                .build()
        )
        .addFunction(
            FunSpec.builder("serialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("encoder", UnionEncoderType)
                .addParameter("value", className)
                .addCode(serializeCode)
                .build()
        )
        .addFunction(
            FunSpec.builder("deserialize")
                .addModifiers(KModifier.OVERRIDE)
                .returns(className)
                .addParameter("decoder", UnionDecoderType)
                .addCode(deserializeCode)
                .build()
        )
        .build()
}
