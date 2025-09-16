package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.NamingContext.Nested
import io.github.nomisrev.openapi.NamingContext.RouteBody
import io.github.nomisrev.openapi.NamingContext.RouteParam
import io.github.nomisrev.openapi.ReferenceOr.Reference
import io.github.nomisrev.openapi.ReferenceOr.Value
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

fun OpenAPI.routes(): List<Route> =
  TypedApiContext(this).routes()

fun TypedApiContext.routes(): List<Route> =
  openAPI.operations().map { (path, method, operation) ->
    val parts = path.pathSegments()
    val operationId = operation.getOrCreateOperationId(path, method)
    val nested = mutableListOf<Model>()

    fun context(context: NamingContext): NamingContext =
      when (parts.size) {
        0 -> context
        1 -> Nested(context, Named(parts[0]))
        else ->
          Nested(
            context,
            parts.drop(1).fold<String, NamingContext>(Named(parts[0])) { acc, part ->
              Nested(Named(part), acc)
            },
          )
      }

    fun toRequestBody(body: RequestBody?): Route.Bodies =
      Route.Bodies(
        body?.required ?: false,
        body
          ?.content
          ?.mapValues { (contentType, mediaType) ->
            val model = when (val schema = mediaType.schema) {
              null -> null
              is Reference -> {
                val resolved = schema.resolve(SchemaResolutionStrategy.ForInline)
                resolved.toModel(ModelResolutionContext(
                  resolved.namedOr { 
                    context(
                      RouteParam(
                        "body",
                        operationId,
                        "Request"
                      )
                    )
                  }, 
                  SchemaResolutionStrategy.ForInline
                )).value.also { nested += it }
              }
              is Value<Schema> -> schema.value.toModel(
                ModelResolutionContext(
                  context(
                    RouteParam(
                      "body",
                      operationId,
                      "Request"
                    )
                  ),
                  SchemaResolutionStrategy.ForInline
                )
              ).also { nested += it }
            }


            when {
              ContentType.MultiPart.FormData.match(contentType) -> {
                requireNotNull(model) { "Multipart without a schema." }

                when (model) {
                  is Model.Object -> {
                    val params = model.properties.map { p ->
                      Route.Body.Multipart.FormData(p.baseName, p.model)
                    }
                    Route.Body.Multipart.Value(params, body.description, mediaType.extensions)
                  }

                  else -> Route.Body.Multipart.Ref(
                    when (val schema = mediaType.schema) {
                      is Reference -> schema.ref.takeLastWhile { it != '/' }
                      is Value<Schema> -> "body"
                      null -> throw IllegalStateException("Multipart without a schema. Generation doesn't know what to do, please open a ticket!")
                    },
                    model,
                    body.description,
                    mediaType.extensions,
                  )
                }
              }

              ContentType.Application.FormUrlEncoded.match(contentType) -> {
                requireNotNull(model) { "Multipart without a schema." }
                require(model is Model.Object) { "FormUrlEncoded only supports object schemas" }
                Route.Body.FormUrlEncoded(
                  model.properties.map { p -> Route.Body.Multipart.FormData(p.baseName, p.model) },
                  body.description,
                  mediaType.extensions
                )
              }

              // default to `setBody(any: Any?)` + contentType
              else -> Route.Body.SetBody(
                model ?: Model.FreeFormJson(body.description, null),
                body.description,
                mediaType.extensions
              )
            }
          }.orEmpty(),
        body?.extensions.orEmpty(),
      )

    fun toResponses(): Route.Returns {
      fun Response.allContentModels(): Map<String, Model> =
        content.mapValues { (_, mediaType) ->
          when (val schema = mediaType.schema) {
            is Reference -> {
              val resolved = schema.resolve(SchemaResolutionStrategy.ForInline)
              resolved.toModel(ModelResolutionContext(
                resolved.namedOr { context(RouteBody(operationId, "Response")) }, 
                SchemaResolutionStrategy.ForInline
              )).value.also { nested += it }
            }
            is Value<Schema> -> schema.value.toModel(
              ModelResolutionContext(
                context(RouteBody(operationId, "Response")),
                SchemaResolutionStrategy.ForInline
              )
            ).also { nested += it }

            null -> Model.FreeFormJson(description, null)
          }
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
      when (val schema = param.schema) {
        is Reference -> {
          val resolved = schema.resolve(SchemaResolutionStrategy.ForInline)
          resolved.toModel(ModelResolutionContext(
            resolved.namedOr { context(RouteParam(param.name, operationId, "Request")) }, 
            SchemaResolutionStrategy.ForInline
          )).value.also { nested += it }
        }
        is Value<Schema> -> schema.value.toModel(
          ModelResolutionContext(
            context(RouteParam(param.name, operationId, "Request")),
            SchemaResolutionStrategy.ForInline
          )
        ).also { nested += it }

        null -> throw IllegalStateException("Parameter without a schema. Generation doesn't know what to do, please open a ticket!")
      }
    }

    Route(
      operationId = operationId,
      summary = operation.summary,
      path = path,
      method = method,
      body = toRequestBody(operation.requestBody?.get()),
      input =
        inputs.zip(operation.parameters) { model, p ->
          val param = p.get()
          Route.Input(param.name, model, param.required, param.input, param.description)
        },
      returnType = toResponses(),
      extensions = operation.extensions,
      nested = nested,
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

context(ctx: TypedApiContext)
private tailrec fun ReferenceOr<Response>.get(): Response =
  when (this) {
    is Value -> value
    is Reference -> {
      val typeName = ref.drop("#/components/responses/".length)
      requireNotNull(ctx.openAPI.components.responses[typeName]) {
        "Response $typeName could not be found in ${ctx.openAPI.components.responses}. Is it missing?"
      }
        .get()
    }
  }

context(ctx: TypedApiContext)
private tailrec fun ReferenceOr<Parameter>.get(): Parameter =
  when (this) {
    is Value -> value
    is Reference -> {
      val typeName = ref.drop("#/components/parameters/".length)
      requireNotNull(ctx.openAPI.components.parameters[typeName]) {
        "Parameter $typeName could not be found in ${ctx.openAPI.components.parameters}. Is it missing?"
      }
        .get()
    }
  }

context(ctx: TypedApiContext)
private tailrec fun ReferenceOr<RequestBody>.get(): RequestBody =
  when (this) {
    is Value -> value
    is Reference -> {
      val typeName = ref.drop("#/components/requestBodies/".length)
      requireNotNull(ctx.openAPI.components.requestBodies[typeName]) {
        "RequestBody $typeName could not be found in ${ctx.openAPI.components.requestBodies}. Is it missing?"
      }
        .get()
    }
  }