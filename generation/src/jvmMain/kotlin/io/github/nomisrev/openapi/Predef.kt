package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement

// TODO include nicer message about expected format
internal val ModelPredef: String =
  """
import kotlin.reflect.KClass
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement

class OneOfSerializationException(
  val payload: JsonElement,
  val errors: Map<KClass<*>, SerializationException>,
  override val message: String =
    ${"\"\"\""}
    Failed to deserialize Json: ${'$'}payload.
    Errors: ${'$'}{
  errors.entries.joinToString(separator = "\n") { (type, error) ->
    "${'$'}type - failed to deserialize: ${'$'}{error.stackTraceToString()}"
  }
}
    ${"\"\"\""}.trimIndent()
) : SerializationException(message)

internal fun <A> attemptDeserialize(
  json: JsonElement,
  vararg block: Pair<KClass<*>, (json: JsonElement) -> A>
): A {
  val errors = linkedMapOf<KClass<*>, SerializationException>()
  block.forEach { (kclass, f) ->
    try {
      return f(json)
    } catch (e: SerializationException) {
      errors[kclass] = e
    }
  }
  throw OneOfSerializationException(json, errors)
}

internal fun <A: Any> attemptDeserialize(
  value: String,
  vararg block: Pair<KClass<*>, (value: String) -> A?>
): A {
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
}
"""
    .trimIndent()

private val ContentType = ClassName("io.ktor.http", "ContentType").copy(nullable = true)

private val UploadTypeSpec =
  TypeSpec.classBuilder("UploadFile")
    .addModifiers(KModifier.DATA)
    .primaryConstructor(
      FunSpec.constructorBuilder()
        .addParameter("filename", String::class)
        .addParameter(
          ParameterSpec.builder("contentType", ContentType).defaultValue("null").build()
        )
        .addParameter(
          ParameterSpec.builder("size", Long::class.asTypeName().copy(nullable = true))
            .defaultValue("null")
            .build()
        )
        .addParameter(
          ParameterSpec.builder(
              "bodyBuilder",
              LambdaTypeName.get(
                receiver = ClassName("io.ktor.utils.io.core", "BytePacketBuilder"),
                returnType = Unit::class.asTypeName()
              )
            )
            .build()
        )
        .build()
    )
    .addProperty(PropertySpec.builder("filename", String::class).initializer("filename").build())
    .addProperty(
      PropertySpec.builder("contentType", ContentType).initializer("contentType").build()
    )
    .addProperty(
      PropertySpec.builder("size", Long::class.asTypeName().copy(nullable = true))
        .initializer("size")
        .build()
    )
    .addProperty(
      PropertySpec.builder(
          "bodyBuilder",
          LambdaTypeName.get(
            receiver = ClassName("io.ktor.utils.io.core", "BytePacketBuilder"),
            returnType = Unit::class.asTypeName()
          )
        )
        .initializer("bodyBuilder")
        .build()
    )
    .build()

val errors =
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
                returnType = TypeVariableName("A").copy(nullable = true),
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
