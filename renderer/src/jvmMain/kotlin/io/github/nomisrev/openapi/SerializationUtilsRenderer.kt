package io.github.nomisrev.openapi

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

fun generateSerializationUtils(config: RenderConfig): FileSpec {
    val packageName = config.modelPackage
        .split('.').filter(String::isNotBlank).joinToString(".") { it.sanitize() }
    return FileSpec.builder(packageName, "AttemptDeserialize")
        .addType(buildUnionSerializationException())
        .addFunction(buildAttemptDeserialize())
        .addFunction(buildPutAll())
        .addFunction(buildValueClassSerializer())
        .build()
}

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
                "%S + payload + %S + errors.entries.joinToString(%S) { (type, error) -> type.toString() + %S + error.stackTraceToString() }",
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
                .beginControlFlow("block.forEach { (kclass, parse) ->")
                .beginControlFlow("try")
                .addStatement("return parse(json)")
                .nextControlFlow("catch (e: %T)", IllegalArgumentException::class)
                .addStatement("errors[kclass] = e")
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
