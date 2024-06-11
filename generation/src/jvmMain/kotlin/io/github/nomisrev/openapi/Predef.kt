package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement

private val UploadTypeSpec: TypeSpec =
  TypeSpec.dataClassBuilder(
      ClassName(`package`, "UploadFile"),
      listOf(
        ParameterSpec.builder("filename", String::class).build(),
        ParameterSpec.builder("contentType", ContentType.nullable()).defaultValue("null").build(),
        ParameterSpec.builder("size", Long::class.asTypeName().nullable())
          .defaultValue("null")
          .build(),
        ParameterSpec.builder(
            "bodyBuilder",
            LambdaTypeName.get(
              receiver = ClassName("io.ktor.utils.io.core", "BytePacketBuilder"),
              returnType = Unit::class.asTypeName()
            )
          )
          .build()
      )
    )
    .build()

private val errors: ParameterizedTypeName =
  ClassName("kotlin.collections", "Map")
    .parameterizedBy(
      ClassName("kotlin.reflect", "KClass").parameterizedBy(TypeVariableName("*")),
      ClassName("kotlinx.serialization", "SerializationException")
    )

private val appendAll: FunSpec =
  FunSpec.builder("appendAll")
    .addAnnotation(
      AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
        .addMember("%L::class", "io.ktor.util.InternalAPI")
        .build()
    )
    .addTypeVariable(TypeVariableName("T", Any::class))
    .receiver(ClassName("io.ktor.client.request.forms", "FormBuilder"))
    .addParameter("key", String::class)
    .addParameter("value", TypeVariableName("T").nullable())
    .addParameter(
      ParameterSpec.builder("headers", ClassName("io.ktor.http", "Headers"))
        .defaultValue("%T.Empty", ClassName("io.ktor.http", "Headers"))
        .build()
    )
    .addCode(
      """
      when (value) {
        is String -> append(key, value, headers)
        is Number -> append(key, value, headers)
        is Boolean -> append(key, value, headers)
        is ByteArray -> append(key, value, headers)
        is %T -> append(key, value, headers)
        is %T -> append(key, value, headers)
        is %T -> append(key, value, headers)
        is UploadFile -> appendUploadedFile(key, value)
        is Enum<*> -> append(key, serialNameOrEnumValue(value), headers)
        null -> Unit
        else -> append(key, value, headers)
      }
      """
        .trimIndent(),
      ClassName("io.ktor.utils.io.core", "ByteReadPacket"),
      ClassName("io.ktor.client.request.forms", "InputProvider"),
      ClassName("io.ktor.client.request.forms", "ChannelProvider")
    )
    .build()

private val appendUploadedFile: FunSpec =
  FunSpec.builder("appendUploadedFile")
    .receiver(ClassName("io.ktor.client.request.forms", "FormBuilder"))
    .addModifiers(KModifier.PRIVATE)
    .addParameter("key", String::class)
    .addParameter("file", ClassName(`package`, "UploadFile"))
    .addCode(
      """
      %M(
        key = key,
        filename = file.filename,
        contentType = file.contentType ?: %T.Application.OctetStream,
        size = file.size,
        bodyBuilder = file.bodyBuilder
      )
      """
        .trimIndent(),
      MemberName("io.ktor.client.request.forms", "append", isExtension = true),
      ContentType
    )
    .build()

private val serialNameOrEnumValue: FunSpec =
  FunSpec.builder("serialNameOrEnumValue")
    .addModifiers(KModifier.PRIVATE)
    .addAnnotation(
      AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
        .addMember("%L::class", "kotlinx.serialization.ExperimentalSerializationApi")
        .addMember("%L::class", "kotlinx.serialization.InternalSerializationApi")
        .build()
    )
    .addTypeVariable(
      TypeVariableName("T", ClassName("kotlin", "Enum").parameterizedBy(TypeVariableName("T")))
    )
    .returns(String::class)
    .addParameter("enum", ClassName("kotlin", "Enum").parameterizedBy(TypeVariableName("T")))
    .addCode(
      "return enum::class.%M()?.descriptor?.getElementName(enum.ordinal) ?: enum.toString()",
      MemberName("kotlinx.serialization", "serializerOrNull", isExtension = true)
    )
    .build()

val predef: FileSpec =
  FileSpec.builder("io.github.nomisrev.openapi", "Predef")
    .addType(
      TypeSpec.classBuilder("OneOfSerializationException")
        .primaryConstructor(
          FunSpec.constructorBuilder()
            .addParameter("payload", JsonElement::class)
            .addParameter("errors", errors)
            .build()
        )
        .superclass(SerializationException::class)
        .addProperty(
          PropertySpec.builder("payload", JsonElement::class).initializer("payload").build()
        )
        .addProperty(PropertySpec.builder("errors", errors).initializer("errors").build())
        .addProperty(
          PropertySpec.builder("message", String::class, KModifier.OVERRIDE)
            .initializer(
              """
              ${'"'}${'"'}${'"'}
                Failed to deserialize Json: ${'$'}payload.
                Errors: ${'$'}{
                  errors.entries.joinToString(separator = "\n") { (type, error) ->
                    "${'$'}type - failed to deserialize: ${'$'}{error.stackTraceToString()}"
                  }
                }
                ${'"'}${'"'}${'"'}.trimIndent()
                """
                .trimIndent()
            )
            .build()
        )
        .build()
    )
    .addFunction(
      FunSpec.builder("attemptDeserialize")
        .addTypeVariable(TypeVariableName("A"))
        .returns(TypeVariableName("A"))
        .addParameter("json", JsonElement::class)
        .addParameter(
          "block",
          ClassName("kotlin", "Pair")
            .parameterizedBy(
              ClassName("kotlin.reflect", "KClass").parameterizedBy(TypeVariableName("*")),
              LambdaTypeName.get(
                receiver = null,
                returnType = TypeVariableName("A"),
                parameters =
                  listOf(
                    ParameterSpec.unnamed(ClassName("kotlinx.serialization.json", "JsonElement"))
                  )
              )
            ),
          KModifier.VARARG
        )
        .addCode(
          """
          val errors = linkedMapOf<KClass<*>, SerializationException>()
          block.forEach { (kclass, f) ->
            try {
              return f(json)
            } catch (e: SerializationException) {
              errors[kclass] = e
            }
          }
          throw OneOfSerializationException(json, errors)
          """
            .trimIndent()
        )
        .build()
    )
    .addFunction(
      FunSpec.builder("attemptDeserialize")
        .addTypeVariable(TypeVariableName("A"))
        .returns(TypeVariableName("A"))
        .addParameter("value", String::class)
        .addParameter(
          "block",
          ClassName("kotlin", "Pair")
            .parameterizedBy(
              ClassName("kotlin.reflect", "KClass").parameterizedBy(TypeVariableName("*")),
              LambdaTypeName.get(
                receiver = null,
                returnType = TypeVariableName("A").nullable(),
                parameters = listOf(ParameterSpec.unnamed(String::class))
              )
            ),
          KModifier.VARARG
        )
        .addCode(
          """
          val errors = linkedMapOf<KClass<*>, SerializationException>()
          block.forEach { (kclass, f) ->
            try {
              f(value)?.let { res -> return res }
            } catch (e: SerializationException) {
              errors[kclass] = e
            }
          }
          // TODO Improve this error message
          throw RuntimeException("BOOM! Improve this error message")
          """
            .trimIndent()
        )
        .build()
    )
    .addType(UploadTypeSpec)
    .addFunctions(listOf(appendAll, appendUploadedFile, serialNameOrEnumValue))
    .build()
