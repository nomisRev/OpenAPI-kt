package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.AdditionalProperties
import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.OpenAPI
import io.github.nomisrev.openapi.ReferenceOr
import io.github.nomisrev.openapi.Schema
import io.github.nomisrev.openapi.schemaRef
import io.github.nomisrev.openapi.test.KModel.Collection

public fun OpenAPI.models(): List<KModel> =
  with(OpenAPITransformer(this)) {
    operationModels() + schemas()
  }.map { model ->
    when(model) {
      is Collection -> model.value
      else -> model
    }
  }

public interface OpenAPISyntax {
  public fun ReferenceOr<Schema>.get(): Schema
  public fun Schema.toModel(context: NamingContext): KModel
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

  /**
   * Gets all the **inline** schemas for operations,
   * and gathers them in the nesting that they occur within the document.
   * So they can be generated whilst maintaining their order of nesting.
   */
  fun operationModels(): List<KModel> =
    openAPI.operations().flatMap { operation ->
      operation.requestBody?.getOrNull()?.let { toRequestBody(operation.operationId, it) }.orEmpty() +
        listOfNotNull(toResponses(operation.operationId, operation.responses)) +
        operation.parameters.mapNotNull { refOrParam ->
          refOrParam.getOrNull()?.let { param ->
            param.schema?.getOrNull()?.toModel(NamingContext.OperationParam(param.name, operation.operationId, "Request"))
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
      enum != null -> toEnum(context, this, requireNotNull(type) { "Enum requires an inner type" }, enum.orEmpty())
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

  override tailrec fun ReferenceOr<Schema>.get(): Schema =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(schemaRef.length)
        requireNotNull(openAPI.components.schemas[typeName]) {
          "Reference for $typeName could not be found in ${openAPI.components.schemas}. Is this schema missing?"
        }.get()
      }
    }
}
