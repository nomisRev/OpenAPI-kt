package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.AdditionalProperties
import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.OpenAPI
import io.github.nomisrev.openapi.Operation
import io.github.nomisrev.openapi.Parameter
import io.github.nomisrev.openapi.PathItem
import io.github.nomisrev.openapi.ReferenceOr
import io.github.nomisrev.openapi.RequestBody
import io.github.nomisrev.openapi.Response
import io.github.nomisrev.openapi.Responses
import io.github.nomisrev.openapi.Schema
import io.github.nomisrev.openapi.applicationJson
import io.github.nomisrev.openapi.isEmpty
import io.github.nomisrev.openapi.multipartData
import io.github.nomisrev.openapi.parametersRef
import io.github.nomisrev.openapi.pathItemsRef
import io.github.nomisrev.openapi.requestBodiesRef
import io.github.nomisrev.openapi.responsesRef
import io.github.nomisrev.openapi.schemaRef
import io.github.nomisrev.openapi.test.KModel.Collection
import io.github.nomisrev.openapi.test.KModel.Primitive
import io.github.nomisrev.openapi.test.OpenAPIInterceptor.Default
import io.github.nomisrev.openapi.test.OpenAPIInterceptor.Default.toInlineResponsesOrNull
import io.github.nomisrev.openapi.test.OpenAPIInterceptor.Default.toRawJson
import io.github.nomisrev.openapi.test.OpenAPIInterceptor.Default.toRequestBodyOrNull
import io.github.nomisrev.openapi.test.OpenAPIInterceptor.Default.toResponses
import io.github.nomisrev.openapi.toPascalCase

public fun OpenAPI.models(): List<KModel> =
  with(OpenAPITransformer(this)) {
    operationModels() + schemas()
  }.map { model ->
    when (model) {
      is Collection -> model.value
      else -> model
    }
  }

public interface OpenAPISyntax {
  public fun Schema.toModel(context: NamingContext): KModel
  public fun ReferenceOr<Schema>.get(): Schema
  public fun ReferenceOr.Reference.namedSchema(): Pair<String, Schema>
  public fun ReferenceOr<Response>.get(): Response
  public fun ReferenceOr<Parameter>.get(): Parameter
  public fun ReferenceOr<RequestBody>.get(): RequestBody
  public fun ReferenceOr<PathItem>.get(): PathItem
}

/**
 * This class implements the traverser,
 * it goes through the [OpenAPI] file, and gathers all the information.
 * It calls the [OpenAPIInterceptor],
 * and invokes the relevant methods for the appropriate models and operations.
 *
 * It does the heavy lifting of figuring out what a `Schema` is,
 * a `String`, `enum=[alive, dead]`, object, etc.
 */
private class OpenAPITransformer(
  private val openAPI: OpenAPI,
  interceptor: OpenAPIInterceptor = OpenAPIInterceptor.Default
) : OpenAPISyntax, OpenAPIInterceptor by interceptor {

  public fun operations(): List<Triple<String, HttpMethod, Operation>> =
    openAPI.paths.entries.flatMap { (path, p) ->
      listOfNotNull(
        p.get?.let { Triple(path, HttpMethod.Get, it) },
        p.put?.let { Triple(path, HttpMethod.Put, it) },
        p.post?.let { Triple(path, HttpMethod.Post, it) },
        p.delete?.let { Triple(path, HttpMethod.Delete, it) },
        p.head?.let { Triple(path, HttpMethod.Head, it) },
        p.options?.let { Triple(path, HttpMethod.Options, it) },
        p.trace?.let { Triple(path, HttpMethod.Trace, it) },
        p.patch?.let { Triple(path, HttpMethod.Patch, it) },
      )
    }

  fun routes(): List<KRoute> =
    operations().map { (path, method, operation) ->
      KRoute(
        operation = operation,
        path = path,
        method = method,
        body = operation.requestBody?.let { toRequestBody(operation, it.get()) },
        input = operation.input(),
        returnType = toResponses(operation)?.let { KRoute.ReturnType(it) },
        extensions = operation.extensions
      )
    }

  // TODO can we re-share logic with models?
  fun Operation.input(): List<KRoute.Input> =
    parameters.map { p ->
      val param = p.get()
      val model = when (val s = param.schema) {
        is ReferenceOr.Reference -> {
          val (name, schema) = s.namedSchema()
          schema.toModel(NamingContext.ClassName(name))
        }

        is ReferenceOr.Value ->
          s.value.toModel(NamingContext.OperationParam(param.name, operationId, "Request"))

        null -> throw IllegalStateException("No Schema for Parameter. Fallback to JsonObject?")
      }
      when (param.input) {
        Parameter.Input.Query -> KRoute.Input.Query(param.name, model)
        Parameter.Input.Header -> KRoute.Input.Header(param.name, model)
        Parameter.Input.Path -> KRoute.Input.Path(param.name, model)
        Parameter.Input.Cookie -> KRoute.Input.Cookie(param.name, model)
      }
    }

  fun KRoute.Body.model(): List<KModel> =
    when (this) {
      is KRoute.Body.Json -> listOf(type)
      is KRoute.Body.Multipart -> parameters.map { it.type }
      KRoute.Body.OctetStream -> listOf(KModel.OctetStream)
    }

  /**
   * Gets all the **inline** schemas for operations,
   * and gathers them in the nesting that they occur within the document.
   * So they can be generated whilst maintaining their order of nesting.
   */
  fun operationModels(): List<KModel> =
    operations().flatMap { (_, _, operation) ->
      // TODO this can probably be removed in favor of `Set` to remove duplication
      // Gather the **inline** request bodies & returnType, ignore top-level
      operation.requestBody?.get()?.let { toRequestBodyOrNull(operation, it) }?.model().orEmpty() +
        listOfNotNull(toInlineResponsesOrNull(operation)) +
        operation.parameters.mapNotNull { refOrParam ->
          refOrParam.getOrNull()?.let { param ->
            param.schema?.getOrNull()
              ?.toModel(NamingContext.OperationParam(param.name, operation.operationId, "Request"))
          }
        }
    }

  /** Gathers all "top-level", or components schemas. */
  fun schemas(): List<KModel> =
    openAPI.components.schemas.entries.map { (schemaName, refOrSchema) ->
      refOrSchema.getOrNull()?.toModel(NamingContext.ClassName(schemaName))
        ?: throw IllegalStateException("Remote schemas not supported yet.")
    }

  override fun Schema.toModel(context: NamingContext): KModel =
    when {
      anyOf != null -> toAnyOf(context, this, anyOf ?: emptyList())
      oneOf != null && oneOf?.size == 1 -> asObject(context)
      oneOf != null -> toOneOf(context, this, oneOf ?: emptyList())
      allOf != null -> TODO("allOf")
      enum != null -> toEnum(
        context,
        this,
        requireNotNull(type) { "Enum requires an inner type" },
        enum.orEmpty()
      )

      properties != null -> asObject(context)
      type != null -> type(context, this, type!!)
      else -> TODO("Schema: $this not yet supported. Please report to issue tracker.")
    }

  private fun Schema.asObject(context: NamingContext): KModel =
    when {
      properties != null -> toObject(context, this, required ?: emptyList(), properties!!)
      additionalProperties != null -> when (val aProps = additionalProperties!!) {
        is AdditionalProperties.PSchema -> toMap(context, aProps)
        is Allowed -> toRawJson(aProps)
      }

      else -> toRawJson(Allowed(true))
    }

  override fun ReferenceOr<Schema>.get(): Schema =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> namedSchema().second
    }

  override fun ReferenceOr.Reference.namedSchema(): Pair<String, Schema> {
    val typeName = this.ref.drop(schemaRef.length)
    val schema = requireNotNull(openAPI.components.schemas[typeName]) {
      "Schema $typeName could not be found in ${openAPI.components.schemas}. Is it missing?"
    }.getOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")
    return Pair(typeName, schema)
  }

  override tailrec fun ReferenceOr<Response>.get()

    : Response =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(responsesRef.length)
        requireNotNull(openAPI.components.responses[typeName]) {
          "Response $typeName could not be found in ${openAPI.components.responses}. Is it missing?"
        }.get()
      }
    }

  override tailrec fun ReferenceOr<Parameter>.get(): Parameter =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(parametersRef.length)
        requireNotNull(openAPI.components.parameters[typeName]) {
          "Parameter $typeName could not be found in ${openAPI.components.parameters}. Is it missing?"
        }.get()
      }
    }

  override tailrec fun ReferenceOr<RequestBody>.get()

    : RequestBody =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(requestBodiesRef.length)
        requireNotNull(openAPI.components.requestBodies[typeName]) {
          "RequestBody $typeName could not be found in ${openAPI.components.requestBodies}. Is it missing?"
        }.get()
      }
    }

  override tailrec fun ReferenceOr<PathItem>.get(): PathItem =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(pathItemsRef.length)
        requireNotNull(openAPI.components.pathItems[typeName]) {
          "PathItem $typeName could not be found in ${openAPI.components.pathItems}. Is it missing?"
        }.get()
      }
    }
}
