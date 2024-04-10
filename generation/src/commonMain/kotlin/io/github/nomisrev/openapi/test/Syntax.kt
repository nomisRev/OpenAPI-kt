package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.AdditionalProperties
import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.OpenAPI
import io.github.nomisrev.openapi.ReferenceOr
import io.github.nomisrev.openapi.RequestBody
import io.github.nomisrev.openapi.Responses
import io.github.nomisrev.openapi.Schema
import io.github.nomisrev.openapi.Schema.Type
import io.github.nomisrev.openapi.applicationJson
import io.github.nomisrev.openapi.defaultArgument
import io.github.nomisrev.openapi.isEmpty
import io.github.nomisrev.openapi.multipartData
import io.github.nomisrev.openapi.schemaRef
import io.github.nomisrev.openapi.test.KModel.BuiltIns
import io.github.nomisrev.openapi.test.KModel.Object.Property
import io.github.nomisrev.openapi.test.KModel.Primitive
import io.github.nomisrev.openapi.test.KModel.Union.TypeArray
import io.github.nomisrev.openapi.toPascalCase

/**
 * A type name is decided by the context it belongs to,
 * there are  possible states.
 *   - inline bodies (Request), and inline responses (Response)
 *   - inline operation parameters
 *   - (inline | top-level) Object param `foo` inline schema => Type.Foo (nested)
 *   - (inline | top-level) Object param `foo` with top-level schema => top-level name
 *   - (inline | top-level) Object param `foo` with primitive => Primitive | List | Set | Map | JsonObject
 */
public sealed interface Context {
  public val content: String

  public data class ClassName(override val content: String) : Context

  public data class OperationParam(
    override val content: String,
    val operationId: String?
  ) : Context

  public sealed interface Param : Context {
    public val outer: Context
  }

  public data class Inline(override val content: String, override val outer: Context) : Param
  public data class Ref(override val content: String, override val outer: Context) : Param
}

public fun OpenAPI.models(): List<KModel> =
  with(Syntax(this)) {
    operationModels() + schemas()
  }

// TODO KRoute
public enum class HttpMethod {
  Get, Put, Post, Delete, Head, Options, Trace, Patch;
}

public interface OpenAPISyntax {
  public fun ReferenceOr<Schema>.get(): Schema
  public fun Schema.toModel(context: Context): KModel
}

public interface ModelInterceptor {
  public fun OpenAPISyntax.toObject(
    context: Context,
    schema: Schema,
    required: List<String>,
    properties: Map<String, ReferenceOr<Schema>>
  ): KModel = KModel.Object(
    context.content,
    schema.description,
    properties.map { (paramName, ref) ->
      val pContext = ref.getOrNull()?.let {
        Context.Inline(paramName, context)
      } ?: Context.Ref(paramName, context)
      val schema = ref.get()
      Property(
        paramName,
        schema.toModel(pContext),
        required.contains(paramName),
        schema.nullable ?: true,
        schema.description,
        schema.defaultArgument(paramName)
      )
    },
    properties.mapNotNull { (paramName, ref) ->
      ref.getOrNull()?.toModel(Context.Inline(paramName, context))
    }.filterNot { it is Primitive }
  )

  public fun OpenAPISyntax.toMap(
    context: Context,
    pSchema: AdditionalProperties.PSchema
  ): KModel =
    BuiltIns.Map(pSchema.value.get().toModel(context))

  public fun OpenAPISyntax.rawJson(allowed: Allowed): KModel =
    if (allowed.value) KModel.Import("kotlinx.serialization.json", "JsonObject")
    else throw IllegalStateException("Illegal State: No additional properties allowed on empty object.")

  public fun OpenAPISyntax.primitive(context: Context, schema: Schema, basic: Type.Basic): KModel =
    when (basic) {
      Type.Basic.Object -> rawJson(Allowed(true))
      Type.Basic.Boolean -> Primitive.Boolean
      Type.Basic.Integer -> Primitive.Int
      Type.Basic.Number -> Primitive.Double
      Type.Basic.String -> Primitive.String
      Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
      Type.Basic.Array -> {
        val inner = requireNotNull(schema.items) { "Array type requires items to be defined." }
          .get().toModel(context)
        if (schema.uniqueItems == true) BuiltIns.Set(inner) else BuiltIns.List(inner)
      }
    }

  public fun OpenAPISyntax.type(
    context: Context,
    schema: Schema,
    type: Type
  ): KModel = when (type) {
    is Type.Array -> when {
      type.types.size == 1 -> type(context, schema.copy(type = type.types.single()), type.types.single())
      else -> TypeArray(toUnionName(context), type.types.sorted().map {
        primitive(context, Schema(type = it), it)
      })
    }

    is Type.Basic -> primitive(context, schema, type)
  }

  public fun toEnumName(context: Context): String =
    when (context) {
      is Context.Inline -> context.content.toPascalCase()
      is Context.Ref -> context.outer.content
      is Context.ClassName -> context.content
      is Context.OperationParam -> {
        requireNotNull(context.operationId) { "Need operationId to generate enum name" }
        "${context.operationId.toPascalCase()}Request${context.content.toPascalCase()}"
      }
    }

  public fun OpenAPISyntax.enum(
    context: Context,
    schema: Schema,
    type: Type,
    enum: List<String>
  ): KModel {
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value" }
    val enumName = toEnumName(context)
    return KModel.Enum(
      enumName,
      /* To resolve the inner type, we erase the enum values.
      * Since the schema is still on the same level - we keep the topLevelName */
      schema.copy(enum = null).toModel(context),
      enum
    )
  }

  public fun toUnionName(context: Context): String =
    when (context) {
      is Context.Ref -> throw IllegalStateException("OneOf is never called for Param!")
      is Context.Inline -> context.content.toPascalCase()
      is Context.ClassName -> context.content.toPascalCase()
      is Context.OperationParam -> context.content.toPascalCase()
    }

  public fun OpenAPISyntax.anyOf(
    context: Context,
    schema: Schema,
    anyOf: List<ReferenceOr<Schema>>,
  ): KModel =
    union(context, schema, anyOf, KModel.Union::AnyOf)

  public fun OpenAPISyntax.oneOf(
    context: Context,
    schema: Schema,
    oneOf: List<ReferenceOr<Schema>>,
  ): KModel =
    union(context, schema, oneOf, KModel.Union::OneOf)

  public fun OpenAPISyntax.generateUnionCaseName(
    context: Context,
    unionSchema: Schema,
    caseSchema: Schema
  ): Context =
    throw IllegalStateException("Cannot generate OneOf name for $this.")

  private fun OpenAPISyntax.union(
    context: Context,
    schema: Schema,
    subtypes: List<ReferenceOr<Schema>>,
    transform: (String, List<KModel>) -> KModel
  ): KModel {
    val name = toUnionName(context)
    return transform(name, subtypes.map { ref: ReferenceOr<Schema> ->
      // If we drill down into a deeper class, then the className needs to match the new top-level className
      val ctx = when (ref) {
        is ReferenceOr.Reference -> Context.ClassName(ref.ref.drop(schemaRef.length))
        is ReferenceOr.Value -> when (context) {
          is Context.Inline ->
            if (ref.value.type != null && ref.value.type !is Type.Array) context
            else generateUnionCaseName(context, schema, ref.value)

          else -> Context.ClassName(name)
        }
      }
      ref.get().toModel(ctx)
    })
  }

  /** Body currently supports JSON, and multipart/form-data. */
  public fun OpenAPISyntax.fromRequestBody(requestBody: RequestBody, operationId: String?): List<KModel> {
    require(requestBody.content.entries.size == 1) {
      "Only a single content type is supported. Found ${requestBody.content.keys}. Please open a feature request."
    }
    val jsonContent = requestBody.content[applicationJson]
    val multipartContent = requestBody.content[multipartData]
    return when {
      jsonContent != null ->
        listOfNotNull(jsonContent.schema?.getOrNull()?.toModel(
          requireNotNull(
            operationId?.let { Context.ClassName("${operationId.toPascalCase()}Request") }
          ) { "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name" }
        ))

      multipartContent != null ->
        multipartContent.schema?.getOrNull()?.properties?.mapNotNull { (name, ref) ->
          ref.getOrNull()?.toModel(Context.OperationParam(name, operationId))
        }.orEmpty()

      else -> throw IllegalStateException("RequestBody content type: $this not yet supported.")
    }
  }

  public fun OpenAPISyntax.fromResponses(responses: Responses, operationId: String?): KModel? =
    when (responses.responses.size) {
      1 -> {
        val response = responses.responses.values.first().getOrNull()
        when {
          response?.content?.contains("application/octet-stream") == true ->
            KModel.Import("io.ktor.client.statement", "HttpStatement")

          response?.content?.contains(applicationJson) == true -> {
            val mediaType = response.content.getValue(applicationJson)
            mediaType.schema
              ?.getOrNull()
              ?.toModel(
                requireNotNull(operationId?.let { Context.ClassName("${it.toPascalCase()}Request") }) {
                  "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                }) ?: rawJson(Allowed(true))
          }

          response?.isEmpty() == true -> Primitive.Unit
          else -> null
        }
      }

      else -> throw IllegalStateException("We don't support multiple formats yet: $responses")
    }

  public companion object Default : ModelInterceptor
}

private class Syntax(
  private val openAPI: OpenAPI,
  interceptor: ModelInterceptor
) : OpenAPISyntax, ModelInterceptor by interceptor {

  /**
   * Gets all the **inline** schemas for operations,
   * and gathers them in the nesting that they occur within the document.
   * So they can be generated whilst maintaining their order of nesting.
   */
  fun operationModels(): List<KModel> =
    openAPI.operations().flatMap { operation ->
      operation.requestBody?.getOrNull()?.let { fromRequestBody(it, operation.operationId) }.orEmpty() +
        listOfNotNull(fromResponses(operation.responses, operation.operationId)) +
        operation.parameters.mapNotNull { refOrParam ->
          refOrParam.getOrNull()?.let { param ->
            param.schema?.getOrNull()?.toModel(Context.OperationParam(param.name, operation.operationId))
          }
        }
    }

  /**
   * Gathers all "top-level", or components schemas.
   */
  fun schemas(): List<KModel> =
    openAPI.components.schemas.entries.map { (schemaName, refOrSchema) ->
      refOrSchema.getOrNull()?.toModel(Context.ClassName(schemaName))
        ?: throw IllegalStateException("Remote schemas not supported yet.")
    }

  override fun Schema.toModel(context: Context): KModel =
    when {
      anyOf != null -> anyOf(context, this, anyOf ?: emptyList())
      oneOf != null && oneOf?.size == 1 -> asObject(context)
      oneOf != null -> oneOf(context, this, oneOf ?: emptyList())
      allOf != null -> TODO("allOf")
      enum != null -> enum(context, this, requireNotNull(type) { "Enum requires an inner type" }, enum.orEmpty())
      properties != null -> asObject(context)
      type != null -> type(context, this, type!!)
      else -> TODO("Schema: $this not yet supported. Please report to issue tracker.")
    }

  private fun Schema.asObject(context: Context): KModel =
    when {
      properties != null -> toObject(context, this, required ?: emptyList(), properties!!)
      additionalProperties != null -> when (val aProps = additionalProperties!!) {
        is AdditionalProperties.PSchema -> toMap(context, aProps)
        is Allowed -> rawJson(aProps)
      }

      else -> rawJson(Allowed(true))
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
