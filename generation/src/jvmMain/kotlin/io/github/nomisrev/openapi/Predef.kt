package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
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
    .build()
