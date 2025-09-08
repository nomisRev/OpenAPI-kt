package io.github.nomisrev.openapi

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
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement

context(OpenAPIContext)
private fun uploadTypeSpec(): TypeSpec =
  TypeSpec.dataClass(
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
            receiver = ClassName("kotlinx.io", "Sink"),
            returnType = Unit::class.asTypeName(),
          ),
        )
        .build(),
    ),
  )

private val errors: ParameterizedTypeName =
  ClassName("kotlin.collections", "Map")
    .parameterizedBy(
      ClassName("kotlin.reflect", "KClass").parameterizedBy(TypeVariableName("*")),
      ClassName("kotlinx.serialization", "SerializationException"),
    )

private val appendAll: FunSpec =
  FunSpec.builder("appendAll")
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
        else -> append(%T(key, value, headers))
      }
      """
        .trimIndent(),
      ClassName("kotlinx.io", "Source"),
      ClassName("io.ktor.client.request.forms", "InputProvider"),
      ClassName("io.ktor.client.request.forms", "ChannelProvider"),
      ClassName("io.ktor.client.request.forms", "FormPart"),
    )
    .build()

private val appendAllParameters: FunSpec =
  FunSpec.builder("appendAll")
    .addTypeVariable(TypeVariableName("T", Any::class))
    .receiver(ClassName("io.ktor.http", "ParametersBuilder"))
    .addParameter("key", String::class)
    .addParameter("value", TypeVariableName("T").nullable())
    .addCode(
      """
      if (value != null) append(key, value.toString())
      """
        .trimIndent()
    )
    .build()

context(OpenAPIContext)
private fun appendUploadedFile(): FunSpec =
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
      ContentType,
    )
    .build()

private val serialNameOrEnumValue: FunSpec =
  FunSpec.builder("serialNameOrEnumValue")
    .addModifiers(KModifier.PRIVATE)
    .addAnnotation(SerializationOptIn)
    .addTypeVariable(
      TypeVariableName("T", ClassName("kotlin", "Enum").parameterizedBy(TypeVariableName("T")))
    )
    .returns(String::class)
    .addParameter("enum", ClassName("kotlin", "Enum").parameterizedBy(TypeVariableName("T")))
    .addCode(
      "return enum::class.%M()?.descriptor?.getElementName(enum.ordinal) ?: enum.toString()",
      MemberName("kotlinx.serialization", "serializerOrNull", isExtension = true),
    )
    .build()

val bodyOrThrow: FunSpec =
  FunSpec.builder("bodyOrThrow")
    .addModifiers(KModifier.SUSPEND, KModifier.INLINE)
    .receiver(HttpResponse)
    .addTypeVariable(TypeVariableName("A").copy(reified = true))
    .returns(TypeVariableName("A"))
    .addStatement(
      """
      |return try {
      |  %M<A>()
      |    ?: throw SerializationException("Body of ${'$'}{A::class.simpleName} expected, but found null")
      |} catch (e: IllegalArgumentException) {
      |  val requestBody =
      |    when (val content = %M.content) {
      |      is %T.ByteArrayContent ->
      |        kotlin.runCatching { content.bytes().decodeToString() }
      |          .getOrElse { "ByteArrayContent (non-UTF8)" }
      |      is OutgoingContent.NoContent -> "NoContent"
      |      is OutgoingContent.ProtocolUpgrade -> "ProtocolUpgrade"
      |      is OutgoingContent.ReadChannelContent -> "ReadChannelContent"
      |      is OutgoingContent.WriteChannelContent -> "WriteChannelContent"
      |      else -> "UnknownContent"
      |    }
      |  val bodyAsText = kotlin.runCatching { %M() }.getOrNull()
      |  throw SerializationException(
      |    ${'"'}${'"'}${'"'}
      |    |Failed to serialize response body to ${'$'}{A::class.simpleName}
      |    |Request URL: ${'$'}{request.url}
      |    |Request Method: ${'$'}{request.method}
      |    |Request Body: ${'$'}requestBody
      |    |Response Status: ${'$'}status
      |    |Response Headers: ${'$'}headers
      |    |Response bodyAsText: ${'$'}bodyAsText
      |  ${'"'}${'"'}${'"'}
      |      .trimMargin(),
      |    e
      |  )
      |}
    """
        .trimMargin(),
      MemberName("io.ktor.client.call", "body", isExtension = true),
      MemberName("io.ktor.client.statement", "request", isExtension = true),
      ClassName("io.ktor.http.content", "OutgoingContent"),
      MemberName("io.ktor.client.statement", "bodyAsText", isExtension = true),
    )
    .build()

private val throwIfNeeded: FunSpec =
  FunSpec.builder("throwIfNeeded")
    .receiver(
      Iterable::class.asTypeName().parameterizedBy(Result::class.asTypeName().parameterizedBy(STAR))
    )
    .addCode(
      """
      |val throwables =
      |  mapNotNull(Result<*>::exceptionOrNull)
      |
      |if (throwables.isNotEmpty()) {
      |  val errors = throwables
      |    .mapNotNull { it.message }
      |    .joinToString("\n") { "  - ${'$'}it" }
      |
      |  val cause = throwables.reduce { acc, other ->
      |    acc.apply { other.let(::addSuppressed) }
      |  }
      |
      |  throw IllegalArgumentException("Requirements not met:\n${'$'}errors", cause)
      |}
      """
        .trimMargin()
    )
    .build()

private val requireAll: FunSpec =
  FunSpec.builder("requireAll")
    .addParameter(
      "requires",
      LambdaTypeName.get(receiver = null, returnType = Unit::class.asTypeName()),
      KModifier.VARARG,
    )
    .addCode(
      """
      |requires.map { require ->
      |  runCatching { require() }
      |}.throwIfNeeded()
      """
        .trimMargin()
    )
    .build()

context(OpenAPIContext)
fun predef(): FileSpec =
  FileSpec.builder(`package`, "Predef")
    .addFunction(bodyOrThrow)
    .addFunction(requireAll)
    .addFunction(throwIfNeeded)
    .addType(
      TypeSpec.classBuilder("UnionSerializationException")
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
                  ),
              ),
            ),
          KModifier.VARARG,
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
          throw UnionSerializationException(json, errors)
          """
            .trimIndent()
        )
        .build()
    )
    .addFunction(
      FunSpec.builder("deserializeOpenEnum")
        .addTypeVariable(TypeVariableName("A"))
        .returns(TypeVariableName("A"))
        .addParameter("value", String::class)
        .addParameter(
          "open",
          LambdaTypeName.get(
            parameters = listOf(ParameterSpec.unnamed(String::class)),
            returnType = TypeVariableName("A"),
          ),
        )
        .addParameter(
          "block",
          ClassName("kotlin", "Pair")
            .parameterizedBy(
              ClassName("kotlin.reflect", "KClass").parameterizedBy(TypeVariableName("*")),
              LambdaTypeName.get(
                parameters = listOf(ParameterSpec.unnamed(String::class)),
                returnType = TypeVariableName("A").nullable(),
              ),
            ),
          KModifier.VARARG,
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
          return open(value)
          """
            .trimIndent()
        )
        .build()
    )
    .addType(uploadTypeSpec())
    .addFunctions(
      listOf(appendAll, appendAllParameters, appendUploadedFile(), serialNameOrEnumValue)
    )
    .build()
