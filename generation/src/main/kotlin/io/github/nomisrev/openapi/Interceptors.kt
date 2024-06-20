package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.withIndent
import io.github.nomisrev.openapi.NamingContext.Named
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Head
import io.ktor.http.HttpMethod.Companion.Options
import io.ktor.http.HttpMethod.Companion.Patch
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpMethod.Companion.Put

interface APIInterceptor {
  fun OpenAPIContext.intercept(api: API): API

  fun OpenAPIContext.modifyInterface(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder

  /**
   * It's valid to discard the original typeSpec, and produce a new one.
   */
  fun OpenAPIContext.modifyImplementation(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder

  companion object {
    val NoOp: APIInterceptor = object : APIInterceptor {
      override fun OpenAPIContext.intercept(api: API): API = api
      override fun OpenAPIContext.modifyInterface(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder = typeSpec
      override fun OpenAPIContext.modifyImplementation(
        api: API, typeSpec: TypeSpec.Builder
      ): TypeSpec.Builder = typeSpec
    }

    fun openAIStreaming(`package`: String): APIInterceptor = object : APIInterceptor {
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
//          addAdditionalFileSpec()
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
        .returns(ClassName("kotlinx.coroutines.flow", "Flow").parameterizedBy(returnType)).apply {
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
                  }.type.value.toTypeName(),
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
  }
}

fun HttpMethod.name(): String =
  when (value) {
    Get.value -> "Get"
    Post.value -> "Post"
    Put.value -> "Put"
    Patch.value -> "Patch"
    Delete.value -> "Delete"
    Head.value -> "Head"
    Options.value -> "Options"
    else -> TODO("Custom HttpMethod not yet supported")
  }