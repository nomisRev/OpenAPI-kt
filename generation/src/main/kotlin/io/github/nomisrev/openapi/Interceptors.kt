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
        api: API,
        typeSpec: TypeSpec.Builder
      ): TypeSpec.Builder = typeSpec
    }
    val OpenAIStreaming = object : APIInterceptor {
      private val ServerSentEvent = ClassName("com.xebia.functional.openai", "ServerSentEvent")
      val streamingOps: Map<String, ClassName> = mapOf(
        "createThreadAndRun" to ServerSentEvent,
        "createRun" to ServerSentEvent,
        "submitToolOuputsToRun" to ServerSentEvent,
        "createChatCompletion" to ClassName(
          "com.xebia.functional.openai.generated.model",
          "CreateChatCompletionStreamResponse"
        )
      )

      override fun OpenAPIContext.intercept(api: API): API = api

      override fun OpenAPIContext.modifyInterface(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder =
        modify(api, typeSpec, implemented = false)

      override fun OpenAPIContext.modifyImplementation(api: API, typeSpec: TypeSpec.Builder): TypeSpec.Builder =
        modify(api, typeSpec, implemented = true)

      fun OpenAPIContext.modify(api: API, typeSpec: TypeSpec.Builder, implemented: Boolean): TypeSpec.Builder {
        val functions = api.routes.associateBy { it.operation.operationId!! }
          .mapNotNull { (key, route) ->
            if (streamingOps.containsKey(key)) route.toStreamingFun(
              implemented = implemented,
              streamingOps[key]!!
            )
            else null
          }
        return typeSpec.addFunctions(functions)
      }

      context(OpenAPIContext)
      private fun Route.toStreamingFun(
        implemented: Boolean,
        returnType: ClassName
      ): FunSpec =
        FunSpec.builder(toParamName(Named(operation.operationId!! + "Stream")))
          .addModifiers(KModifier.SUSPEND, if (implemented) KModifier.OVERRIDE else KModifier.ABSTRACT)
          .addParameters(params(defaults = !implemented))
          .addParameters(requestBody(defaults = !implemented))
          .addParameter(configure(defaults = !implemented))
          .returns(ClassName("kotlinx.coroutines", "Flow").parameterizedBy(returnType))
          .apply {
            if (implemented) {
              addCode(
                CodeBlock.builder()
                  .addStatement(
                    "val response = client.%M {",
                    MemberName("io.ktor.client.request", "prepareRequest", isExtension = true)
                  )
                  .withIndent {
                    addStatement("timeout {")
                      .withIndent {
                        addStatement(
                          "requestTimeoutMillis = 60.seconds.toLong(%T.MILLISECONDS)",
                          ClassName("kotlin.time", "DurationUnit")
                        )
                        addStatement(
                          "socketTimeoutMillis = 60.seconds.toLong(%T.MILLISECONDS)",
                          ClassName("kotlin.time", "DurationUnit")
                        )
                      }
                    addStatement("configure()")
                    addStatement("method = %T.%L", ClassName("io.ktor.http", "HttpMethod"), method.value)
                    addStatement("accept(%T.Text.EventStream)", ClassName("io.ktor.http", "ContentType"))
                    addStatement("header(%T.CacheControl, %S)", ClassName("io.ktor.http", "HttpHeaders"), "no-cache")
                    addStatement("header(%T.Connection, %S)", ClassName("io.ktor.http", "HttpHeaders"), "keep-alive")
                    val replace =
                      input
                        .mapNotNull {
                          if (it.input == Parameter.Input.Path)
                            ".replace(\"{${it.name}}\", ${toParamName(Named(it.name))})"
                          else null
                        }
                        .joinToString(separator = "")
                    addStatement(
                      "url { %M(%S$replace) }",
                      MemberName("io.ktor.http", "path", isExtension = true),
                      path
                    )
                    addContentType(body)
                    addStatement(
                      "val element = %T.encodeToJsonElement(%T.serializer(), %L)",
                      ClassName("kotlinx.serialization.json", "Json"),
                      requireNotNull(body.jsonOrNull()).type.value.toTypeName(),
                      toParamName(Named("body"))
                    )
                    addStatement(
                      "val jsObject = %T(element.jsonObject + %T(%S to %T(true)))",
                      ClassName("kotlinx.serialization.json", "JsonObject"),
                      ClassName("kotlin", "Pair"),
                      "stream",
                      ClassName("kotlinx.serialization.json", "JsonPrimitive")
                    )
                    addStatement("setBody(jsObject)")
                  }
                  .addStatement("}")
                  .addStatement(
                    "return response.%M { streamEvents(it) }",
                    MemberName("io.ktor.client.statement", "execute", isExtension = true)
                  )
                  .build()
              )
            }
          }
          .build()
    }
  }
}
