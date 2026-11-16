package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName

private val JsonType = ClassName("kotlinx.serialization.json", "Json")
private val JsonElementType = ClassName("kotlinx.serialization.json", "JsonElement")
private val JsonObjectType = ClassName("kotlinx.serialization.json", "JsonObject")
private val JsonObjectBuilderType = ClassName("kotlinx.serialization.json", "JsonObjectBuilder")
private val SerializationExceptionType = ClassName("kotlinx.serialization", "SerializationException")
private val KSerializerType = ClassName("kotlinx.serialization", "KSerializer")
private val SerialDescriptorType = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
private val DecoderType = ClassName("kotlinx.serialization.encoding", "Decoder")
private val EncoderType = ClassName("kotlinx.serialization.encoding", "Encoder")
private val KClassType = ClassName("kotlin.reflect", "KClass")
private val PairType = ClassName("kotlin", "Pair")

/** The file of the union serialization helpers, in the model package and in the api package when the client needs them. */
internal const val SerializationUtilsFileName = "AttemptDeserialize"

/** [discriminated]: the models have a discriminated hierarchy, whose cases need [TaggedCaseSerializerName]. */
fun generateSerializationUtils(config: RenderConfig, discriminated: Boolean = false): FileSpec {
    val packageName = config.modelPackage
        .split('.').filter(String::isNotBlank).joinToString(".") { it.sanitize() }

    return FileSpec.builder(packageName, SerializationUtilsFileName)
        .addType(buildUnionSerializationException())
        .addFunction(buildAttemptDeserialize())
        .addFunction(buildPutAll())
        .addFunction(buildValueClassSerializer())
        .apply {
            if (discriminated) {
                addType(buildTaggedCaseSerializer())
                addFunction(buildDiscriminatorValue())
            }
        }
        .build()
}

/** The serializer of a case of a discriminated base: it writes the case's tag, and requires it when decoding. */
internal const val TaggedCaseSerializerName = "TaggedCaseSerializer"
internal const val DiscriminatorValueName = "discriminatorValue"

private val JsonTransformingSerializerType = ClassName("kotlinx.serialization.json", "JsonTransformingSerializer")
private val JsonPrimitiveType = ClassName("kotlinx.serialization.json", "JsonPrimitive")
private val JsonNullType = ClassName("kotlinx.serialization.json", "JsonNull")

/**
 * `@JsonClassDiscriminator` writes and reads the tag only when a value is serialized as its base, while an operation,
 * a property or a union branch typed as a case uses the case's own serializer: the case writes no tag and rejects it
 * as an unknown key. So each case's serializer carries its tag, and the base selects the case by it
 * (`DiscriminatedObjectRenderer`).
 */
private fun buildTaggedCaseSerializer(): TypeSpec {
    val t = TypeVariableName("T", ANY)
    return TypeSpec.classBuilder(TaggedCaseSerializerName)
        .addModifiers(KModifier.OPEN)
        .addTypeVariable(t)
        .addKdoc(
            "The serializer of a case of a discriminated base: [serializer], with the property [discriminator] set " +
                "to [tag]. A value of another case, or without its tag, is an error.\n",
        )
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("serializer", KSerializerType.parameterizedBy(t))
                .addParameter("discriminator", String::class)
                .addParameter("tag", String::class)
                .build()
        )
        .superclass(JsonTransformingSerializerType.parameterizedBy(t))
        .addSuperclassConstructorParameter("serializer")
        .addProperty(PropertySpec.builder("discriminator", String::class, KModifier.PRIVATE).initializer("discriminator").build())
        .addProperty(PropertySpec.builder("tag", String::class, KModifier.PRIVATE).initializer("tag").build())
        .addFunction(
            FunSpec.builder("transformSerialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("element", JsonElementType)
                .returns(JsonElementType)
                .addStatement(
                    "return %T(mapOf(discriminator to %T(tag)) + (element as %T))",
                    JsonObjectType,
                    JsonPrimitiveType,
                    JsonObjectType,
                )
                .build()
        )
        .addFunction(
            FunSpec.builder("transformDeserialize")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("element", JsonElementType)
                .returns(JsonElementType)
                .addStatement("val found = element.%L(discriminator)", DiscriminatorValueName)
                .beginControlFlow("if (found != tag)")
                .addStatement(
                    "throw %T(%P)",
                    SerializationExceptionType,
                    "Expected '\$discriminator' to be '\$tag', but it was \${found?.let { \"'\$it'\" } ?: \"absent\"}",
                )
                .endControlFlow()
                .addStatement("return %T(element as %T - discriminator)", JsonObjectType, JsonObjectType)
                .build()
        )
        .build()
}

/** The tag of a discriminated value: the [discriminator] property of a JSON object, `null` when it has none. */
private fun buildDiscriminatorValue(): FunSpec =
    FunSpec.builder(DiscriminatorValueName)
        .addModifiers(KModifier.INTERNAL)
        .receiver(JsonElementType)
        .addParameter("discriminator", String::class)
        .returns(String::class.asClassName().copy(nullable = true))
        .addStatement(
            "val value = (this as? %T ?: throw %T(%P))[discriminator]",
            JsonObjectType,
            SerializationExceptionType,
            "Expected a JSON object with '\$discriminator', but was \$this",
        )
        .addStatement("return (value as? %T)?.takeUnless { it is %T }?.content", JsonPrimitiveType, JsonNullType)
        .build()

private fun buildUnionSerializationException(): TypeSpec {
    val payloadProp = PropertySpec.builder("payload", JsonElementType)
        .initializer("payload")
        .build()
    val errorsProp = PropertySpec.builder(
        "errors",
        Map::class.asClassName().parameterizedBy(
            KClassType.parameterizedBy(STAR),
            IllegalArgumentException::class.asClassName()
        )
    )
        .initializer("errors")
        .build()

    return TypeSpec.classBuilder("UnionSerializationException")
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("payload", JsonElementType)
                .addParameter(
                    "errors",
                    Map::class.asClassName().parameterizedBy(
                        KClassType.parameterizedBy(STAR),
                        IllegalArgumentException::class.asClassName()
                    )
                )
                .build()
        )
        .superclass(SerializationExceptionType)
        .addSuperclassConstructorParameter(
            CodeBlock.of(
                "%S + payload + %S + errors.entries.joinToString(%S) { it.key.toString() + %S + it.value.stackTraceToString() }",
                "Failed to deserialize Json: ",
                ".\nErrors:\n",
                "\n",
                " - failed to deserialize: ",
            )
        )
        .addProperty(payloadProp)
        .addProperty(errorsProp)
        .build()
}

private fun buildAttemptDeserialize(): FunSpec {
    val typeA = TypeVariableName("A")
    val blockType = PairType.parameterizedBy(
        KClassType.parameterizedBy(STAR),
        LambdaTypeName.get(
            receiver = JsonType,
            parameters = listOf(ParameterSpec.unnamed(JsonElementType)),
            returnType = typeA
        )
    )

    return FunSpec.builder("attemptDeserialize")
        .addTypeVariable(typeA)
        .receiver(JsonType)
        .addParameter("json", JsonElementType)
        .addParameter(
            ParameterSpec.builder("block", blockType)
                .addModifiers(KModifier.VARARG)
                .build()
        )
        .returns(typeA)
        .addCode(
            CodeBlock.builder()
                .addStatement(
                    "val errors = linkedMapOf<%T, %T>()",
                    KClassType.parameterizedBy(STAR),
                    IllegalArgumentException::class.asClassName()
                )
                .beginControlFlow("block.forEach")
                .beginControlFlow("try")
                .addStatement("return it.second.invoke(this@attemptDeserialize, json)")
                .nextControlFlow("catch (e: %T)", IllegalArgumentException::class)
                .addStatement("errors[it.first] = e")
                .endControlFlow()
                .endControlFlow()
                .addStatement("throw UnionSerializationException(json, errors)")
                .build()
        )
        .build()
}

private fun buildPutAll(): FunSpec =
    FunSpec.builder("putAll")
        .receiver(JsonObjectBuilderType)
        .addParameter("jsonObject", JsonObjectType.copy(nullable = true))
        .addCode("jsonObject.orEmpty().forEach { (key, value) -> put(key, value) }\n")
        .build()

private fun buildValueClassSerializer(): FunSpec {
    val wrappedType = TypeVariableName("Wrapped")
    val valueType = TypeVariableName("Value")
    val serializerType = KSerializerType.parameterizedBy(wrappedType)
    val valueSerializerType = KSerializerType.parameterizedBy(valueType)

    return FunSpec.builder("ValueClassSerializer")
        .addTypeVariable(wrappedType)
        .addTypeVariable(valueType)
        .addParameter(
            "unwrap",
            LambdaTypeName.get(parameters = listOf(ParameterSpec.unnamed(wrappedType)), returnType = valueType)
        )
        .addParameter(
            "wrap",
            LambdaTypeName.get(parameters = listOf(ParameterSpec.unnamed(valueType)), returnType = wrappedType)
        )
        .addParameter("valueSerializer", valueSerializerType)
        .returns(serializerType)
        .addCode(
            CodeBlock.builder()
                .beginControlFlow("return object : %T", serializerType)
                .addStatement("override val descriptor: %T = valueSerializer.descriptor", SerialDescriptorType)
                .addStatement(
                    "override fun deserialize(decoder: %T): %T = wrap(decoder.decodeSerializableValue(valueSerializer))",
                    DecoderType,
                    wrappedType
                )
                .addStatement(
                    "override fun serialize(encoder: %T, value: %T) = encoder.encodeSerializableValue(valueSerializer, unwrap(value))",
                    EncoderType,
                    wrappedType
                )
                .endControlFlow()
                .build()
        )
        .build()
}
