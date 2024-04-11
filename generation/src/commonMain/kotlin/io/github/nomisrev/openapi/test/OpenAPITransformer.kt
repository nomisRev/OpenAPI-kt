package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.AdditionalProperties
import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.OpenAPI
import io.github.nomisrev.openapi.Operation
import io.github.nomisrev.openapi.Parameter
import io.github.nomisrev.openapi.PathItem
import io.github.nomisrev.openapi.ReferenceOr
import io.github.nomisrev.openapi.RequestBody
import io.github.nomisrev.openapi.Response
import io.github.nomisrev.openapi.Schema
import io.github.nomisrev.openapi.parametersRef
import io.github.nomisrev.openapi.pathItemsRef
import io.github.nomisrev.openapi.requestBodiesRef
import io.github.nomisrev.openapi.responsesRef
import io.github.nomisrev.openapi.schemaRef
import io.github.nomisrev.openapi.test.KModel.Collection

public fun OpenAPI.models(): Set<KModel> =
  with(OpenAPITransformer(this)) {
    operationModels() + schemas()
  }.map { model ->
    when (model) {
      is Collection -> model.value
      else -> model
    }
  }.toSet()

public interface OpenAPISyntax {
  public fun Schema.toModel(context: NamingContext): KModel

  public fun ReferenceOr.Reference.namedSchema(): Pair<String, Schema>
  public fun Schema.topLevelNameOrNull(): String?
  public fun Schema.isTopLevel(): Boolean =
    topLevelNameOrNull() != null

  public fun ReferenceOr<Schema>.get(): Schema
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

  public fun operations(): List<Triple<String, Method, Operation>> =
    openAPI.paths.entries.flatMap { (path, p) ->
      listOfNotNull(
        p.get?.let { Triple(path, Method.GET, it) },
        p.put?.let { Triple(path, Method.PUT, it) },
        p.post?.let { Triple(path, Method.POST, it) },
        p.delete?.let { Triple(path, Method.DELETE, it) },
        p.head?.let { Triple(path, Method.HEAD, it) },
        p.options?.let { Triple(path, Method.OPTIONS, it) },
        p.trace?.let { Triple(path, Method.TRACE, it) },
        p.patch?.let { Triple(path, Method.PATCH, it) },
      )
    }

  fun routes(): List<KRoute> =
    operations().map { (path, method, operation) ->
      KRoute(
        operation = operation,
        path = path,
        method = method,
        body = toRequestBody(operation, operation.requestBody?.get()),
        input = operation.input(),
        returnType = toResponses(operation),
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

  private fun KRoute.Body.model(): List<KModel> =
    when (this) {
      is KRoute.Body.Json -> listOf(type)
      is KRoute.Body.Multipart -> parameters.map { it.type }
      is KRoute.Body.OctetStream -> listOf(KModel.Binary)
      is KRoute.Body.Xml -> TODO("We don't support XML yet")
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
      val bodies = operation.requestBody?.get()
        ?.let { toRequestBody(operation, it).types.values }
        ?.flatMap { it.model() }
        .orEmpty()

      val responses = toResponses(operation).map { it.value.type }

      val parameters = operation.parameters.mapNotNull { refOrParam ->
        refOrParam.getOrNull()?.let { param ->
          param.schema?.getOrNull()
            ?.toModel(NamingContext.OperationParam(param.name, operation.operationId, "Request"))
        }
      }
      bodies + responses + parameters
    }

  /** Gathers all "top-level", or components schemas. */
  fun schemas(): List<KModel> {
    val schemas = openAPI.components.schemas.entries.map { (schemaName, refOrSchema) ->
      refOrSchema.getOrNull()?.toModel(NamingContext.ClassName(schemaName))
        ?: throw IllegalStateException("Remote schemas not supported yet.")
    }
    return schemas
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

  override fun Schema.topLevelNameOrNull(): String? =
    openAPI.components.schemas.entries.find { (_, ref) ->
      ref.get() == this
    }?.key

  override fun ReferenceOr.Reference.namedSchema(): Pair<String, Schema> {
    val typeName = this.ref.drop(schemaRef.length)
    val schema = requireNotNull(openAPI.components.schemas[typeName]) {
      "Schema $typeName could not be found in ${openAPI.components.schemas}. Is it missing?"
    }.getOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")
    return Pair(typeName, schema)
  }

  override tailrec fun ReferenceOr<Response>.get(): Response =
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

  override tailrec fun ReferenceOr<RequestBody>.get(): RequestBody =
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
