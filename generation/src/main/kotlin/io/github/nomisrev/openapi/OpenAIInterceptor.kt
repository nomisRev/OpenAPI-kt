package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.withIndent
import io.github.nomisrev.openapi.NamingContext.Named
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

fun APIInterceptor.Companion.openAIStreaming(`package`: String): APIInterceptor =
  object : APIInterceptor {
    private val ServerSentEvent = ClassName(`package`, "ServerSentEvent")
    val streamingOps: Map<String, ClassName> =
      mapOf(
        "createThreadAndRun" to ServerSentEvent,
        "createRun" to ServerSentEvent,
        "submitToolOuputsToRun" to ServerSentEvent,
        "createChatCompletion" to ClassName(`package`, "CreateChatCompletionStreamResponse"),
      )

    val nonRequiredFields =
      mapOf(
        ClassName(`package`, "ListAssistantFilesResponse") to listOf("first_id", "last_id"),
        ClassName(`package`, "ListAssistantsResponse") to listOf("first_id", "last_id"),
        ClassName(`package`, "ListMessageFilesResponse") to listOf("first_id", "last_id"),
        ClassName(`package`, "ListMessagesResponse") to listOf("first_id", "last_id"),
        ClassName(`package`, "ListRunsResponse") to listOf("first_id", "last_id"),
        ClassName(`package`, "ListRunStepsResponse") to listOf("first_id", "last_id"),
        ClassName(`package`, "ListThreadsResponse") to listOf("first_id", "last_id"),
        ClassName(`package`, "MessageObject") to listOf("status", "metadata"),
        ClassName(`package`, "MessageObject", "Content") to listOf("image_file", "text"),
        ClassName(`package`, "RunObject") to listOf("expires_at", "required_action"),
        ClassName(`package`, "RunStepDetailsToolCallsCodeObject", "CodeInterpreter", "Outputs") to
          listOf("logs", "image"),
        ClassName(`package`, "RunStepDetailsToolCallsFunctionObject") to listOf("id"),
        ClassName(`package`, "RunStepDetailsToolCallsFunctionObject", "Function") to
          listOf("name", "arguments", "output"),
        ClassName(`package`, "RunStepDetailsToolCallsObject", "ToolCalls") to
          listOf("code_interpreter", "retrieval", "function"),
        ClassName(`package`, "RunStepDetailsToolCallsRetrievalObject") to listOf("retrieval"),
        ClassName(`package`, "RunStepObject") to listOf("expiredAt", "metadata"),
        ClassName(`package`, "MessageContentTextObject.Text.Annotations") to
          listOf("file_path", "file_citation"),
      )

    override fun OpenAPIContext.intercept(model: Model): Model {
      val obj = (model as? Model.Object) ?: return model
      val name = toClassName(model.context)
      val hasNonRequired = nonRequiredFields[name] ?: return model
      return obj.copy(
        properties =
          obj.properties.map { prop ->
            if (hasNonRequired.contains(prop.baseName)) prop.copy(isRequired = false) else prop
          }
      )
    }

    override fun OpenAPIContext.modifyInterface(
      api: API,
      typeSpec: TypeSpec.Builder,
    ): TypeSpec.Builder = modify(api, typeSpec, implemented = false)

    override fun OpenAPIContext.modifyImplementation(
      api: API,
      typeSpec: TypeSpec.Builder,
    ): TypeSpec.Builder = modify(api, typeSpec, implemented = true)

    public var createPredef = false

    fun OpenAPIContext.modify(
      api: API,
      typeSpec: TypeSpec.Builder,
      implemented: Boolean,
    ): TypeSpec.Builder {
      val functions =
        api.routes
          .associateBy { it.operationId }
          .mapNotNull { (key, route) ->
            if (streamingOps.containsKey(key))
              route.toStreamingFun(implemented = implemented, streamingOps[key]!!)
            else null
          }
      if (functions.isNotEmpty() && !createPredef) {
        createPredef = true
        addAdditionalFileSpec(streamingPredef())
      }
      return typeSpec.addFunctions(functions)
    }

    context(OpenAPIContext)
    private fun Route.toStreamingFun(implemented: Boolean, returnType: ClassName): FunSpec =
      FunSpec.builder(toFunName() + "Stream")
        .addModifiers(
          KModifier.SUSPEND,
          if (implemented) KModifier.OVERRIDE else KModifier.ABSTRACT,
        )
        .addParameters(params(defaults = !implemented))
        .addParameters(requestBody(defaults = !implemented))
        .addParameter(configure(defaults = !implemented))
        .returns(ClassName("kotlinx.coroutines.flow", "Flow").parameterizedBy(returnType))
        .apply {
          if (implemented) {
            addCode(
              CodeBlock.builder()
                .addStatement(
                  "val response = client.%M {",
                  MemberName("io.ktor.client.request", "prepareRequest", isExtension = true),
                )
                .withIndent {
                  addStatement(
                      "%M {",
                      MemberName("io.ktor.client.plugins", "timeout", isExtension = true),
                    )
                    .withIndent {
                      addStatement(
                        "requestTimeoutMillis = 60.%M.toLong(%T.MILLISECONDS)",
                        seconds,
                        DurationUnit,
                      )
                      addStatement(
                        "socketTimeoutMillis = 60.%M.toLong(%T.MILLISECONDS)",
                        seconds,
                        DurationUnit,
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
                    requireNotNull(body.setBodyOrNull()) {
                        "Only OpenAI JSON Streaming supported right now."
                      }
                      .type
                      .toTypeName(),
                    toParamName(Named("body")),
                  )
                  addStatement(
                    "val jsObject = %T(element.%M + %T(%S, %T(true)))",
                    ClassName("kotlinx.serialization.json", "JsonObject"),
                    MemberName("kotlinx.serialization.json", "jsonObject", isExtension = true),
                    ClassName("kotlin", "Pair"),
                    "stream",
                    ClassName("kotlinx.serialization.json", "JsonPrimitive"),
                  )
                  addStatement("setBody(jsObject)")
                }
                .addStatement("}")
                .addStatement(
                  "return %M { response.execute { streamEvents(it) } }",
                  MemberName("kotlinx.coroutines.flow", "flow"),
                )
                .build()
            )
          }
        }
        .build()
  }

context(OpenAPIContext)
private fun streamingPredef(): FileSpec {
  val serverSentEvent =
    TypeSpec.dataClass(
      ClassName(`package`, "ServerSentEvent"),
      listOf(
        ParameterSpec("event", String::class.asTypeName().copy(nullable = true)) {
          defaultValue("null")
        },
        ParameterSpec("data", JsonElement::class.asTypeName().copy(nullable = true)) {
          defaultValue("null")
        },
      ),
    ) {
      addAnnotation(annotationSpec<Serializable>())
    }

  val streamEvents =
    FunSpec.builder("streamEvents")
      .addModifiers(KModifier.INTERNAL, KModifier.INLINE, KModifier.SUSPEND)
      .addTypeVariable(TypeVariableName("A").copy(reified = true))
      .receiver(
        ClassName("kotlinx.coroutines.flow", "FlowCollector").parameterizedBy(TypeVariableName("A"))
      )
      .addParameter("response", HttpResponse)
      .addCode(
        CodeBlock.builder()
          .addStatement("val prefix: String = %S", "data:")
          .addStatement("val end = %S", "data: [DONE]")
          .addStatement(
            "val channel: %T = response.%M()",
            ByteReadChannel,
            MemberName("io.ktor.client.statement", "bodyAsChannel"),
          )
          .addStatement("var nextEvent: String? = null")
          .beginControlFlow("while (!channel.isClosedForRead)")
          .addStatement(
            "val line = channel.%M() ?: continue",
            MemberName("io.ktor.utils.io", "readUTF8Line", isExtension = true),
          )
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
            MemberName("kotlinx.serialization", "serializer"),
          )
          .addStatement("emit(value)")
          .endControlFlow()
          .beginControlFlow("if (nextEvent != null)")
          .addStatement("val data = line.removePrefix(prefix).trim()")
          .beginControlFlow("if (data.isNotBlank())")
          .addStatement(
            "val eventData = %T.decodeFromString(%T.serializer(), data)",
            Json::class.asTypeName(),
            JsonObject::class.asTypeName(),
          )
          .addStatement(
            "val value: A = %T(event = nextEvent, data = eventData) as A",
            ClassName(`package`, "ServerSentEvent"),
          )
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
