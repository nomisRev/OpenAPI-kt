package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Named
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

fun OpenAPI.routes(): List<Route> = TypedApiContext(this).routes()

private fun TypedApiContext.routes(): List<Route> =
  openAPI.operations().map { (path, method, operation) ->
    val parts = path.pathSegments()
    val operationId = operation.getOrCreateOperationId(path, method)

    fun context(context: NamingContext): NamingContext =
      when (parts.size) {
        0 -> context
        1 -> NamingContext.Nested(context, Named(parts[0]))
        else ->
          NamingContext.Nested(
            context,
            parts.drop(1).fold<String, NamingContext>(Named(parts[0])) { acc, part ->
              NamingContext.Nested(Named(part), acc)
            },
          )
      }

    fun toRequestBody(body: RequestBody?): Route.Bodies =
      Route.Bodies(
        body?.required ?: false,
        body
          ?.content
          ?.entries
          ?.associate { (contentType, mediaType) ->
            when {
              ContentType.MultiPart.FormData.match(contentType) -> {
                val resolved =
                  requireNotNull(mediaType.schema?.resolve()) { "$mediaType without a schema. Generation doesn't know what to do, please open a ticket!" }

                val multipart =
                  when (resolved) {
                    is Resolved.Ref -> {
                      val modelResolved = resolved.toModel(Named(resolved.name))
                      when (val m = modelResolved.value) {
                        is Model.Object -> {
                          val params = m.properties.map { p ->
                            Route.Body.Multipart.FormData(p.baseName, p.model)
                          }
                          Route.Body.Multipart.Value(params, body.description, mediaType.extensions)
                        }

                        else -> Route.Body.Multipart.Ref(
                          resolved.name,
                          m,
                          body.description,
                          mediaType.extensions,
                        )
                      }
                    }

                    is Resolved.Value -> {
                      val modelResolved = resolved.toModel(
                        resolved.namedOr { context(NamingContext.RouteParam("body", operationId, "Request")) }
                      )
                      when (val m = modelResolved.value) {
                        is Model.Object -> {
                          val params = m.properties.map { p ->
                            Route.Body.Multipart.FormData(p.baseName, p.model)
                          }
                          Route.Body.Multipart.Value(params, body.description, mediaType.extensions)
                        }

                        else -> Route.Body.Multipart.Ref(
                          resolved.value.title ?: "body",
                          m,
                          body.description,
                          mediaType.extensions,
                        )
                      }
                    }
                  }

                Pair(contentType, multipart)
              }

              ContentType.Application.FormUrlEncoded.match(contentType) -> {
                val resolved =
                  mediaType.schema?.resolve()
                    ?: throw IllegalStateException(
                      "$mediaType without a schema. Generation doesn't know what to do, please open a ticket!"
                    )

                val params: List<Route.Body.Multipart.FormData> =
                  when (resolved) {
                    is Resolved.Ref -> {
                      val model = resolved.toModel(Named(resolved.name)) as Resolved.Ref
                      val obj =
                        model.value as? Model.Object
                          ?: throw IllegalStateException(
                            "FormUrlEncoded only supports object schemas"
                          )
                      obj.properties.map { p -> Route.Body.Multipart.FormData(p.baseName, p.model) }
                    }

                    is Resolved.Value -> {
                      val obj = resolved.value
                      val props = obj.properties ?: emptyMap()
                      props.map { (name, ref) ->
                        val context =
                          resolved.namedOr { context(NamingContext.RouteParam(name, operationId, "Request")) }
                        Route.Body.Multipart.FormData(name, ref.resolve().toModel(context).value)
                      }
                    }
                  }
                Pair(
                  contentType,
                  Route.Body.FormUrlEncoded(params, body.description, mediaType.extensions),
                )
              }

              // default to `setBody(any: Any?)` + contentType
              else -> {
                val model =
                  mediaType.schema?.resolve()?.let { resolved ->
                    val context = resolved.namedOr {
                      context(NamingContext.RouteParam("body", operationId, "Request"))
                    }

                    resolved.toModel(context).value
                  } ?: Model.FreeFormJson(body.description, null)
                Pair(contentType, Route.Body.SetBody(model, body.description, mediaType.extensions))
              }
            }
          }
          .orEmpty(),
        body?.extensions.orEmpty(),
      )

    fun toResponses(): Route.Returns {
      fun Response.allContentModels(): Map<String, Model> =
        content.entries.associate { (contentType, mediaType) ->
          val resolved = mediaType.schema?.resolve()
          val context = resolved?.namedOr {
            context(NamingContext.RouteBody(operationId, "Response"))
          }
          val model =
            when (resolved) {
              is Resolved -> resolved.toModel(context!!).value
              null -> Model.FreeFormJson(description, null)
            }
          val cleaned =
            when (model) {
              is Model.Object -> model.copy(inline = emptyList())
              is Model.Union -> model.copy(inline = emptyList())
              else -> model
            }
          contentType to cleaned
        }

      fun Response.toReturnType(): Route.ReturnType =
        Route.ReturnType(types = this.allContentModels(), extensions = extensions)

      val entries: Map<HttpStatusCode, Route.ReturnType> =
        operation.responses.responses.entries.associate { (code, refOrResponse) ->
          val statusCode = HttpStatusCode.fromValue(code)
          val response = refOrResponse.get()
          statusCode to response.toReturnType()
        }

      val success: Route.ReturnType? =
        entries.entries.filter { it.key.isSuccess() }.minByOrNull { it.key.value }?.value

      val default: Route.ReturnType? = operation.responses.default?.get()?.toReturnType()

      return Route.Returns(
        success = success,
        default = default,
        entries = entries,
        extensions = operation.responses.extensions,
      )
    }

    val inputs = operation.parameters.map { p ->
      val param = p.get()
      val resolved = requireNotNull(param.schema?.resolve()) { "No Schema for Parameter." }
      val context = resolved.namedOr {
        context(NamingContext.RouteParam(param.name, operationId, "Request"))
      }
      resolved.toModel(context)
    }
    val nestedInput = inputs.mapNotNull { (it as? Resolved.Value)?.value }
    val nestedResponses =
      operation.responses.responses.mapNotNull { (_, refOrResponse) ->
        when (refOrResponse) {
          is ReferenceOr.Reference -> null
          is ReferenceOr.Value -> {
            val resolved =
              refOrResponse.value.content
                .getOrElse("application/json") { null }
                ?.schema
                ?.resolve() ?: return@mapNotNull null

            val context =
              resolved.namedOr { context(NamingContext.RouteBody(operationId, "Response")) }

            (resolved.toModel(context) as? Resolved.Value)?.value
          }
        }
      }

    val json =
      operation.requestBody
        ?.valueOrNull()
        ?.content
        ?.getOrElse("application/json") { null }
        ?.schema
        ?.resolve()

    val nestedBody =
      json
        ?.namedOr { context(Named("${operationId}Request")) }
        ?.let { (json.toModel(it) as? Resolved.Value)?.value }
        .let(::listOfNotNull)

    Route(
      operationId = operationId,
      summary = operation.summary,
      path = path,
      method = method,
      body = toRequestBody(operation.requestBody?.get()),
      input =
        inputs.zip(operation.parameters) { model, p ->
          val param = p.get()
          Route.Input(param.name, model.value, param.required, param.input, param.description)
        },
      returnType = toResponses(),
      extensions = operation.extensions,
      nested = nestedInput + nestedResponses + nestedBody,
    )
  }

private fun Operation.getOrCreateOperationId(path: String, method: HttpMethod): String =
  operationId ?: generateSyntheticOperationId(path, method)

private fun generateSyntheticOperationId(path: String, method: HttpMethod): String {
  val params = path.split("/")
    .takeLastWhile { it.startsWith("{") && it.endsWith("}") }
    .map { it.substring(1, it.length - 1) }

  return if (params.isEmpty()) method.value.lowercase()
  else params.joinToString(
    prefix = "${method.value.lowercase()}By",
    separator = "And"
  ) { it.replaceFirstChar { it.uppercase() } }
}

private fun OpenAPI.operations(): List<Triple<String, HttpMethod, Operation>> =
  paths.entries.flatMap { (path, p) ->
    listOfNotNull(
      p.get?.let { Triple(path, HttpMethod.Get, it) },
      p.put?.let { Triple(path, HttpMethod.Put, it) },
      p.post?.let { Triple(path, HttpMethod.Post, it) },
      p.delete?.let { Triple(path, HttpMethod.Delete, it) },
      p.head?.let { Triple(path, HttpMethod.Head, it) },
      p.options?.let { Triple(path, HttpMethod.Options, it) },
      p.trace?.let { Triple(path, HttpMethod.parse("Trace"), it) },
      p.patch?.let { Triple(path, HttpMethod.Patch, it) },
    )
  }