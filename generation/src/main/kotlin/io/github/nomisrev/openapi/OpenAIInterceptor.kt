package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.withIndent
import io.github.nomisrev.openapi.NamingContext.Named
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

fun APIInterceptor.Companion.openAIStreaming(`package`: String): APIInterceptor =
  object : APIInterceptor {
    private val ServerSentEvent = ClassName(`package`, "ServerSentEvent")
    val streamingOps: Map<String, ClassName> = mapOf(
      "createThreadAndRun" to ServerSentEvent,
      "createRun" to ServerSentEvent,
      "submitToolOuputsToRun" to ServerSentEvent,
      "createChatCompletion" to ClassName(`package`, "CreateChatCompletionStreamResponse")
    )

    override fun OpenAPIContext.intercept(api: API): API = api

    override fun OpenAPIContext.modifyInterface(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder =
      modify(api, typeSpec, implemented = false)

    override fun OpenAPIContext.modifyImplementation(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder =
      modify(api, typeSpec, implemented = true)

    public var createPredef = false

    fun OpenAPIContext.modify(api: API, typeSpec: TypeSpec.Builder, implemented: Boolean): TypeSpec.Builder {
      val functions = api.routes.associateBy { it.operation.operationId!! }.mapNotNull { (key, route) ->
        if (streamingOps.containsKey(key)) route.toStreamingFun(
          implemented = implemented, streamingOps[key]!!
        )
        else null
      }
      if (functions.isNotEmpty() && !createPredef) {
        createPredef = true
        addAdditionalFileSpec(streamingPredef())
      }
      return typeSpec.addFunctions(functions)
    }

    context(OpenAPIContext)
    private fun Route.toStreamingFun(
      implemented: Boolean, returnType: ClassName
    ): FunSpec = FunSpec.builder(toParamName(Named(operation.operationId!! + "Stream")))
      .addModifiers(KModifier.SUSPEND, if (implemented) KModifier.OVERRIDE else KModifier.ABSTRACT)
      .addParameters(params(defaults = !implemented)).addParameters(requestBody(defaults = !implemented))
      .addParameter(configure(defaults = !implemented))
      .returns(ClassName("kotlinx.coroutines.flow", "Flow").parameterizedBy(returnType))
      .apply {
        if (implemented) {
          addCode(
            CodeBlock.builder().addStatement(
              "val response = client.%M {",
              MemberName("io.ktor.client.request", "prepareRequest", isExtension = true)
            ).withIndent {
              addStatement(
                "%M {",
                MemberName("io.ktor.client.plugins", "timeout", isExtension = true)
              ).withIndent {
                addStatement(
                  "requestTimeoutMillis = 60.%M.toLong(%T.MILLISECONDS)",
                  seconds,
                  DurationUnit
                )
                addStatement(
                  "socketTimeoutMillis = 60.%M.toLong(%T.MILLISECONDS)",
                  seconds,
                  DurationUnit
                )
              }
              addStatement("}")
              addStatement("configure()")
              addStatement("method = %T.%L", HttpMethod, method.name())
              addStatement("%M(%T.Text.EventStream)", accept, ContentType)
              addStatement("%M(%T.CacheControl, %S)", header, HttpHeaders, "no-cache")
              addStatement("%M(%T.Connection, %S)", header, HttpHeaders, "keep-alive")
              addPathAndContent()
              addStatement(
                "val element = %T.encodeToJsonElement(%T.serializer(), %L)",
                ClassName("kotlinx.serialization.json", "Json"),
                // TODO turn into warning!?
                requireNotNull(body.jsonOrNull()) {
                  "Only OpenAI JSON Streaming supported right now."
                }.type.toTypeName(),
                toParamName(Named("body"))
              )
              addStatement(
                "val jsObject = %T(element.%M + %T(%S, %T(true)))",
                ClassName("kotlinx.serialization.json", "JsonObject"),
                MemberName("kotlinx.serialization.json", "jsonObject", isExtension = true),
                ClassName("kotlin", "Pair"),
                "stream",
                ClassName("kotlinx.serialization.json", "JsonPrimitive")
              )
              addStatement("setBody(jsObject)")
            }.addStatement("}")
              .addStatement(
                "return %M { response.execute { streamEvents(it) } }",
                MemberName("kotlinx.coroutines.flow", "flow")
              )
              .build()
          )
        }
      }.build()
  }

context(OpenAPIContext)
private fun streamingPredef(): FileSpec {
  val ServerSentEvent = ClassName(`package`, "ServerSentEvent")
  val serverSentEvent = TypeSpec.dataClassBuilder(
    ClassName(`package`, "ServerSentEvent"),
    listOf(
      ParameterSpec.builder("event", String::class.asTypeName().copy(nullable = true))
        .defaultValue("null")
        .build(),
      ParameterSpec.builder("data", JsonElement::class.asTypeName().copy(nullable = true))
        .defaultValue("null")
        .build()
    )
  ).addAnnotation(annotationSpec<Serializable>())
    .build()
  val streamEvents = FunSpec.builder("streamEvents")
    .addModifiers(KModifier.INTERNAL, KModifier.INLINE, KModifier.SUSPEND)
    .addTypeVariable(TypeVariableName("A").copy(reified = true))
    .receiver(ClassName("kotlinx.coroutines.flow", "FlowCollector").parameterizedBy(TypeVariableName("A")))
    .addParameter("response", HttpResponse)
    .addCode(
      CodeBlock.builder()
        .addStatement("val prefix: String = %S", "data:")
        .addStatement("val end = %S", "data: [DONE]")
        .addStatement("val channel: %T = response.%M()", ByteReadChannel, MemberName("io.ktor.client.statement", "bodyAsChannel"))
        .addStatement("var nextEvent: String? = null")
        .beginControlFlow("while (!channel.isClosedForRead)")
        .addStatement("val line = channel.%M() ?: continue", MemberName("io.ktor.utils.io", "readUTF8Line", isExtension = true))
        .beginControlFlow("if (line.startsWith(end))")
        .addStatement("break")
        .endControlFlow()
        .beginControlFlow("if (line.startsWith(%S))", "event:")
        .addStatement("nextEvent = line.removePrefix(%S).trim()", "event:")
        .addStatement("continue")
        .endControlFlow()
        .beginControlFlow("else if (line.startsWith(prefix) && nextEvent == null)")
        .addStatement("val data = line.removePrefix(prefix).trim()")
        .addStatement(
          "val value: A = %T.decodeFromString(%M(), data)",
          ClassName("kotlinx.serialization.json", "Json"),
          MemberName("kotlinx.serialization", "serializer")
        )
        .addStatement("emit(value)")
        .endControlFlow()
        .beginControlFlow("if (nextEvent != null)")
        .addStatement("val data = line.removePrefix(prefix).trim()")
        .beginControlFlow("if (data.isNotBlank())")
        .addStatement(
          "val eventData = %T.decodeFromString(%T.serializer(), data)",
          Json::class.asTypeName(),
          JsonObject::class.asTypeName()
        )
        .addStatement("val value: A = %T(event = nextEvent, data = eventData) as A", ServerSentEvent)
        .addStatement("emit(value)")
        .endControlFlow()
        .endControlFlow()
        .endControlFlow()
        .build()
    )
    .build()
  return FileSpec.builder(`package`, "StreamingOps")
    .addType(serverSentEvent)
    .addFunction(streamEvents)
    .build()
}