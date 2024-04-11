package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.BuiltIns
import io.github.nomisrev.openapi.Model.Import
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.Model.Product
import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.Route.Body
import io.github.nomisrev.openapi.Route.Param
import io.github.nomisrev.openapi.Route.ReturnType
import io.github.nomisrev.openapi.Schema.Type
import kotlin.collections.List
import kotlin.jvm.JvmInline

internal const val schemaRef = "#/components/schemas/"
internal const val responsesRef = "#/components/responses/"
internal const val parametersRef = "#/components/parameters/"
internal const val requestBodiesRef = "#/components/requestBodies/"
internal const val pathItemsRef = "#/components/pathItems/"

internal const val applicationJson = "application/json"
internal const val applicationOctectStream = "application/octet-stream"
internal const val multipartFormData = "multipart/form-data"

/**
 * Returns a structured presentation of the OpenAPI operations,
 * specifically meant for code generation.
 * So it gives access to a typed representation of the OpenAPI document.
 */
public fun OpenAPI.routes(): Routes =
  GenerationSyntax(this).routes()

/**
 * Returns a structured presentation of the OpenAPI models,
 * it gives access to a typed representation of the OpenAPI document.
 *
 * TODO review structure. How should code be generated in terms of nested.
 *  => Only logical idea seems to follow the OpenAPI spec, and generate code based on that.
 *  So, a reference is never generated, instead its component will be generated as top-level.
 */
public fun OpenAPI.models(): Models =
  GenerationSyntax(this).models()

/**
 * Small wrapper to make `OpenAPI` scoped available, while processing the OpenAPI document tree.
 * This can in the future include:
 *   - Naming strategy
 *   - Interceptors, or extensions (x-).
 *   - Our own transformer will exist out of these components, and they'll be overridable.
 *     => This will allow you to transform types,
 *     or add additional information before it goes to the code generation phase.
 */
@JvmInline
private value class GenerationSyntax(private val openAPI: OpenAPI) {
  fun models(): Models =
    Models(
      routes().flatMap { (_, routes) ->
        routes.flatMap {
          when (it.body) {
            is Body.Json -> listOf(it.body.param.type)
            is Body.Multipart -> it.body.parameters.map(Param::type)
            null -> emptyList()
          }
        } + routes.flatMap { r -> r.input.map { it.parameter.type } } +
          routes.map { it.returnType.type } +
          openAPI.components.schemas.entries.mapNotNull { (name, s) ->
            s.getOrNull()?.toModel(null, null, name)
          }
      }.flattenStdTypes().toSet()
    )

  private fun List<Model>.flattenStdTypes(): List<Model> =
    map { model ->
      when(model) {
        is BuiltIns.List -> model.inner
        is BuiltIns.Set -> model.inner
        is BuiltIns.Map -> model.value
        else -> model
      }
    }.filterNot { it is Primitive || it is Import }

  fun Operation.toRoute(name: String): Route =
    Route(name, description, bodyParam(), parameters(), returnType())

  fun routes(): Routes =
    Routes(openAPI.operationsByTag().mapValues { (_, operation) ->
      operation.map {
        val operationId = requireNotNull(it.operationId) { "operationId required atm." }
        it.toRoute(operationId)
      }
    } + openAPI.webhooks.mapValues { (key, referenceOrPathItem) ->
      val pathItem = referenceOrPathItem.get()
      listOfNotNull(
        pathItem.get,
        pathItem.put,
        pathItem.post,
        pathItem.delete,
        pathItem.head,
        pathItem.options,
        pathItem.trace,
        pathItem.patch
      ).map {
        // Setup operationId for webhook operations
        it.copy(operationId = key).toRoute(key)
      }
    })

  /* Class name is either defined in one of the referenced schemas,
   * or we generate a unique name for inline schemas. */
  private fun Schema.className(): String? =
    topLevel()?.key

  private fun Schema.topLevel(): Map.Entry<String, ReferenceOr<Schema>>? =
    openAPI.components.schemas.entries.find { (_, r) -> r.get() == this }

  private fun Schema.isTopLevel(): Boolean =
    topLevel() != null

  private fun Schema.className(operationId: String?): String =
    className() ?: "${operationId}Response"

  /* Map a `Schema` into a `Model`.
   * - anyOf: can support one or more schema
   * - oneOf support: can support exactly one schema
   * - allOf support: union of all schemas ?? */
  private fun Schema.toModel(
    operationId: String?,
    paramName: String?,
    className: String?
  ): Model =
    when {
      anyOf != null -> anyOf(operationId, paramName, className)
      oneOf != null && oneOf?.size == 1 ->
        asObject(operationId, paramName, className)

      oneOf != null -> oneOf(operationId, paramName, className)
      allOf != null -> TODO("allOf")
      enum != null -> enum(operationId, paramName, className)
      type != null -> toModel(type!!, operationId, paramName, className)
      // Attempt object in case type was missing
      properties != null -> asObject(operationId, paramName, className)
      else -> TODO("Schema: $this not yet supported. Please report to issue tracker.")
    }

  private tailrec fun Schema.toModel(
    type: Type,
    operationId: String?,
    paramName: String?,
    className: String?
  ): Model = when (type) {
    is Type.Array -> when {
      // Flatten Single type array
      type.types.size == 1 -> toModel(type.types.single(), operationId, paramName, className)
      else -> arrayTypes(type.types, operationId, paramName, className)
    }

    Type.Basic.Boolean -> Primitive.Boolean
    Type.Basic.Integer -> Primitive.Int
    Type.Basic.Number -> Primitive.Double
    Type.Basic.String -> Primitive.String
    Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
    Type.Basic.Object -> asObject(operationId, paramName, className)
    Type.Basic.Array -> {
      val inner = requireNotNull(items) { "Array type requires items to be defined." }
        .get().toModel(operationId, paramName, className)
      if (uniqueItems == true) BuiltIns.Set(inner) else BuiltIns.List(inner)
    }

    @Suppress("SENSELESS_NULL_IN_WHEN")
    null -> throw IllegalStateException("analysis loses smartcast between null-check & nested when.")
  }

  private fun Schema.arrayTypes(
    types: List<Type.Basic>,
    operationId: String?,
    paramName: String?,
    className: String?
  ): Union.TypeArray {
    requireNotNull(paramName) { "Currently arrayTypes is only supported for OpenAPI specs with operationId specified. $operationId: $paramName $types" }
    return Union.TypeArray(paramName.toPascalCase(), types.sorted().map { type ->
      when (type) {
        Type.Basic.Boolean -> Primitive.Boolean
        Type.Basic.Integer -> Primitive.Int
        Type.Basic.Number -> Primitive.Double
        Type.Basic.String -> Primitive.String
        Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
        Type.Basic.Object -> asObject(operationId, paramName, className)
        Type.Basic.Array ->
          if (uniqueItems == true) BuiltIns.Set(items!!.get().toModel(operationId, paramName, className))
          else BuiltIns.List(items!!.get().toModel(operationId, paramName, className))
      }
    })
  }

  /**
   * The OpenAPI `object` type can have many shapes, so we need to handle the different cases.
   * - An `object` with properties defined is a `Product` type (a data class in Kotlin).
   * - An `object` with [AdditionalProperties] defined, so two cases.
   *   - [AdditionalProperties.PSchema] defines a schema for the `value` part of Map<String, PSchema>.
   *   - [AdditionalProperties.Allowed] any additional properties are allowed, so we use KotlinX JsonObject.
   * - If nothing is known about the object, we default to a `Map` type.
   */
  private fun Schema.asObject(
    operationId: String?,
    paramName: String?,
    className: String?
  ): Model {
    val props = properties
    val additionalProps = additionalProperties
    val topLevel = className()
    val paramName =
      if (topLevel != null && paramName != null) "$topLevel.${paramName.toPascalCase()}"
      else paramName
    val cn = when {
      topLevel != null && paramName != null -> paramName.toPascalCase()
      else -> className(operationId)
    }
    return when {
      props != null -> Product(
        cn,
        description,
        props.mapValues { (paramName, ref) ->
          Property(operationId, paramName, ref.get(), cn)
        },
        props.mapNotNull { (paramName, ref) -> ref.getOrNull()?.toModel(operationId, paramName, cn) }
          .flattenStdTypes()
      )

      additionalProps != null -> when (additionalProps) {
        is AdditionalProperties.PSchema ->
          BuiltIns.Map(additionalProps.value.get().toModel(operationId, paramName, className))

        is AdditionalProperties.Allowed ->
          if (additionalProps.value) Import("kotlinx.serialization.json", "JsonObject")
          else throw IllegalStateException("Illegal State: No additional properties allowed on empty object.")
      }

      else -> BuiltIns.Map(Primitive.String)
    }
  }

  private fun Schema.enum(operationId: String?, paramName: String?, className: String?): Model.Enum {
    requireNotNull(type) { "Enum requires an inner type" }
    val enum = requireNotNull(enum)
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value" }
    // TODO Enum Naming Strategy
    val topLevel = className()
    val typeName = when {
      topLevel != null -> topLevel
      paramName != null && className != null -> "$className.${paramName.toPascalCase()}"
      className != null -> className
      paramName != null && operationId != null -> "${operationId.toPascalCase()}${paramName.toPascalCase()}"
      else -> throw IllegalStateException("Could not generate enum class name for operationId: $operationId, param: $paramName, or $className")
    }
    return Model.Enum(
      typeName,
      // We erase the enum values, so that we can resolve the inner type
      copy(enum = null).toModel(operationId, paramName, className),
      enum
    )
  }

  private fun Schema.oneOf(
    operationId: String?,
    paramName: String?,
    className: String?
  ): Union.OneOf {
    val simpleName = when {
      paramName != null && className != null -> "$className${paramName.toPascalCase()}"
      paramName == null && className != null -> className
      operationId != null -> operationId.toPascalCase()
      else -> throw IllegalStateException("Currently oneOf is only supported for OpenAPI specs with operationId($operationId), or if defined as top-level schema($className) or combination ($paramName) specified.")
    }
    val oneOf = requireNotNull(oneOf) { "Impossible, oneOf being generated for $this" }
    return Union.OneOf(simpleName, oneOf.map { ref: ReferenceOr<Schema> ->
      val schema = ref.get()
      // If we drill down into a deeper class, then the className needs to match the new top-level className
      ref.get().toModel(operationId, paramName, schema.className() ?: className)
    })
  }

  /* In OpenAI this is for example used for open enumeration. */
  private fun Schema.anyOf(operationId: String?, paramName: String?, className: String?): Union.AnyOf {
    val simpleName = requireNotNull(operationId ?: paramName) {
      "Currently oneOf is only supported for OpenAPI specs with operationId specified."
    }
    val anyOf = requireNotNull(anyOf) { "Impossible, oneOf being generated for $this" }
    return Union.AnyOf(simpleName.toPascalCase(), anyOf.map { it.get().toModel(operationId, paramName, className) })
  }

  // TODO support multiple responses, or formats??
  private fun Operation.returnType(): ReturnType =
    when (responses.responses.size) {
      1 -> {
        val response = responses.responses.values.first().get()
        when {
          response.content.contains("application/octet-stream") ->
            ReturnType(Import("io.ktor.client.statement", "HttpStatement"), false)

          response.content.contains(applicationJson) -> {
            val mediaType = response.content.getValue(applicationJson)
            val schema = requireNotNull(mediaType.schema) { "Schema is required for response: $response" }.get()
            ReturnType(schema.toModel(operationId, null, null), schema.nullable ?: false)
          }

          response.isEmpty() -> ReturnType(BuiltIns.Unit, true)
          else -> throw IllegalStateException("Response: $response")
        }
      }

      else -> throw IllegalStateException("We don't support multiple formats yet: $responses")
    }

  private fun Operation.parameters(): List<Route.Input> =
    parameters.map { referenceOrParameter ->
      val parameter = referenceOrParameter.get()
      val schema = requireNotNull(parameter.schema) {
        "Schema is required for parameter: $referenceOrParameter"
      }.get()
      val param = Param(operationId, schema, parameter.name, null)
      when (parameter.input) {
        Parameter.Input.Query -> Route.Input.Query(param)
        Parameter.Input.Header -> Route.Input.Header(param)
        Parameter.Input.Path -> Route.Input.Path(param)
        Parameter.Input.Cookie -> Route.Input.Cookie(param)
      }
    }

  /** Body currently supports JSON, and multipart/form-data. */
  private fun Operation.bodyParam(): Body? = when (val requestBody = requestBody?.get()) {
    null -> null
    else -> {
      // TODO support XML through KotlinX Serialization XML, Yaml, Others???
      require(requestBody.content.entries.size == 1) {
        "Only a single content type is supported. Found ${requestBody.content.keys}. Please open a feature request."
      }
      val jsonContent = requestBody.content[applicationJson]
      val multipartContent = requestBody.content[multipartFormData]
      when {
        jsonContent != null -> {
          val jsonSchema = requireNotNull(jsonContent.schema) {
            "application/json is required, but no schema was found for $operationId."
          }.get()
          Body.Json(Param(operationId, jsonSchema, paramName = null, className = null))
        }

        multipartContent != null -> {
          val schema = requireNotNull(multipartContent.schema) {
            "multipart/form-data schema is required, but no schema was found for $operationId."
          }.get()
          Body.Multipart(schema.properties!!.entries.map { (key, value) ->
            Param(operationId, value.get(), key, null)
          })
        }

        else -> TODO("RequestBody content type: $requestBody not yet supported.")
      }
    }
  }

  private tailrec fun ReferenceOr<Schema>.get(): Schema =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(schemaRef.length)
        openAPI.components.schemas.getValue(typeName).get()
      }
    }

  private tailrec fun ReferenceOr<Response>.get(): Response =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(responsesRef.length)
        openAPI.components.responses.getValue(typeName).get()
      }
    }

  private tailrec fun ReferenceOr<Parameter>.get(): Parameter =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(parametersRef.length)
        openAPI.components.parameters.getValue(typeName).get()
      }
    }

  private tailrec fun ReferenceOr<RequestBody>.get(): RequestBody =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(requestBodiesRef.length)
        openAPI.components.requestBodies.getValue(typeName).get()
      }
    }

  private tailrec fun ReferenceOr<PathItem>.get(): PathItem =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop(pathItemsRef.length)
        openAPI.components.pathItems.getValue(typeName).get()
      }
    }

  private fun Param(operationId: String?, schema: Schema, paramName: String?, className: String?): Param {
    val paramName = (paramName ?: schema.className(operationId)).toCamelCase()
    return Param(
      paramName,
      schema.toModel(operationId, paramName, className),
      schema.nullable ?: (schema.required?.contains(paramName) == false),
      schema.description,
      schema.defaultArgument(paramName)
    )
  }

  private fun Schema.Property(
    operationId: String?,
    paramName: String,
    schema: Schema,
    className: String?
  ): Product.Property =
    Product.Property(
      paramName.sanitize().toCamelCase(),
      schema.toModel(operationId, paramName, className),
      schema.nullable ?: (schema.required?.contains(paramName) == false),
      schema.description,
      schema.defaultArgument(paramName)
    )
}

internal fun Schema.defaultArgument(
  paramName: String
): String? =
  when {
    enum != null -> {
      val defaultValue = (default as? ExampleValue.Single)?.value
      if (defaultValue != null) "${paramName.toPascalCase()}.${(default as? ExampleValue.Single)?.value}"
      else null
    }
    default?.toString() == "[]" -> "emptyList()"
    else -> default?.toString()
  }

internal fun Response.isEmpty(): Boolean =
  headers.isEmpty() && content.isEmpty() && links.isEmpty() && extensions.isEmpty()
