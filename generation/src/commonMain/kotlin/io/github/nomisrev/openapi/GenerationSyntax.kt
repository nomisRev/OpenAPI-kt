package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Import
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.Model.Product
import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.Route.Body
import io.github.nomisrev.openapi.Route.Param
import io.github.nomisrev.openapi.Route.ReturnType
import io.github.nomisrev.openapi.Schema.Type
import kotlin.jvm.JvmInline

private const val schemaRef = "#/components/schemas/"
private const val responsesRef = "#/components/responses/"
private const val parametersRef = "#/components/parameters/"
private const val requestBodiesRef = "#/components/requestBodies/"
private const val pathItemsRef = "#/components/pathItems/"

private const val applicationJson = "application/json"
private const val multipartData = "multipart/form-data"

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
          openAPI.components.schemas.entries.mapNotNull { (key, s) -> s.getOrNull()?.toModel(key, key) }
      }.filterNotStdModel().toSet()
    )

  private fun List<Model>.filterNotStdModel(): List<Model> =
    filterNot { model ->
      model is Primitive || model is Model.List || model is Import || model is Model.Unit
    }

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
  private fun Schema.className(operationId: String?): String =
    openAPI.components.schemas.entries.find { (_, r) -> r.get() == this }?.key ?: "${operationId}Response"

  /* Map a `Schema` into a `Model`.
   * - anyOf: can support one or more schema
   * - oneOf support: can support exactly one schema
   * - allOf support: union of all schemas ?? */
  private fun Schema.toModel(operationId: String?, paramName: String? = null): Model =
    when {
      anyOf != null -> anyOf(operationId, paramName)
      oneOf != null -> oneOf(operationId, paramName)
      allOf != null -> TODO("allOf")
      enum != null -> enum(operationId, paramName)
      type != null -> when (type) {
        is Type.Array -> arrayTypes((type as Type.Array).value, operationId, paramName)
        Type.Basic.Boolean -> Primitive.Boolean
        Type.Basic.Integer -> Primitive.Int
        Type.Basic.Number -> Primitive.Double
        Type.Basic.String -> Primitive.String
        Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
        Type.Basic.Object -> asObject(operationId, paramName)
        Type.Basic.Array -> Model.List(items!!.get().toModel(operationId, paramName))
        null -> throw IllegalStateException("analysis loses smartcast between null-check & nested when.")
      }
      // Attempt object in case type was missing
      properties != null -> asObject(operationId, paramName)
      else -> TODO("Schema: $this not yet supported. Please report to issue tracker.")
    }

  fun ReferenceOr<Schema>.getOrNull(): Schema? =
    when (this) {
      is ReferenceOr.Reference -> null
      is ReferenceOr.Value -> value
    }

  private fun Schema.arrayTypes(types: List<Type.Basic>, operationId: String?, paramName: String?): Union.TypeArray {
    requireNotNull(paramName) { "Currently arrayTypes is only supported for OpenAPI specs with operationId specified. $operationId: $paramName $types" }
    return Union.TypeArray(paramName.toPascalCase(), types.sorted().map { type ->
      when (type) {
        Type.Basic.Boolean -> Primitive.Boolean
        Type.Basic.Integer -> Primitive.Int
        Type.Basic.Number -> Primitive.Double
        Type.Basic.String -> Primitive.String
        Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
        Type.Basic.Object -> asObject(operationId, paramName)
        Type.Basic.Array -> Model.List(items!!.get().toModel(operationId, paramName))
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
    paramName: String?
  ): Model {
    val props = properties
    val additionalProps = additionalProperties
    return when {
      props != null -> Product(
        className(operationId),
        description,
        props.mapValues { (paramName, ref) ->
          Property(operationId, paramName, ref.get())
        },
        props.mapNotNull { (paramName, ref) -> ref.getOrNull()?.toModel(operationId, paramName) }
          .filterNotStdModel()
      )

      additionalProps != null -> when (additionalProps) {
        is AdditionalProperties.PSchema ->
          Model.Map(additionalProps.value.get().toModel(operationId, paramName))

        is AdditionalProperties.Allowed -> if (additionalProps.value) {
          Import("kotlinx.serialization.json", "JsonObject")
        } else throw IllegalStateException("Illegal State: No additional properties allowed on empty object.")
      }
      // TODO what if Allowed(false)???
      else -> Model.Map(Primitive.String)
    }
  }

  private fun Schema.enum(operationId: String?, paramName: String?): Model.Enum {
    requireNotNull(type) { "Enum requires an inner type" }
    val enum = requireNotNull(enum)
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value" }
    val typeName =
      requireNotNull(paramName?.let { className(it) }?.toPascalCase()) { "ParamName cannot be null for enum" }
    return Model.Enum(
      typeName,
      // We erase the enum values, so that we can resolve the inner type
      copy(enum = null).toModel(operationId, paramName),
      enum
    )
  }

  private fun Schema.oneOf(operationId: String?, paramName: String?): Union.OneOf {
    requireNotNull(operationId) { "Currently oneOf is only supported for OpenAPI specs with operationId specified." }
    val oneOf = requireNotNull(oneOf) { "Impossible, oneOf being generated for $this" }
    return Union.OneOf(operationId.toPascalCase(), oneOf.map { it.get().toModel(operationId, paramName) })
  }

  private fun Schema.anyOf(operationId: String?, paramName: String?): Union.AnyOf {
    requireNotNull(operationId) { "Currently oneOf is only supported for OpenAPI specs with operationId specified." }
    val anyOf = requireNotNull(anyOf) { "Impossible, oneOf being generated for $this" }
    return Union.AnyOf(operationId.toPascalCase(), anyOf.map { it.get().toModel(operationId, paramName) })
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
            ReturnType(schema.toModel(operationId), schema.isNullable ?: false)
          }

          response.isEmpty() -> ReturnType(Model.Unit, true)
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
      val param = Param(operationId, schema, parameter.name)
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
      val multipartContent = requestBody.content[multipartData]
      when {
        jsonContent != null -> {
          val jsonSchema = requireNotNull(jsonContent.schema) {
            "application/json is required, but no schema was found for $operationId."
          }.get()
          Body.Json(Param(operationId, jsonSchema, paramNameOrType = null))
        }

        multipartContent != null -> {
          val schema = requireNotNull(multipartContent.schema) {
            "multipart/form-data schema is required, but no schema was found for $operationId."
          }.get()
          Body.Multipart(schema.multipartParameters(operationId))
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

  private fun Schema.multipartParameters(operationId: String?): List<Param> =
    properties!!.entries.map { (key, value) ->
      Param(operationId, value.get(), key)
    }

  private fun Param(operationId: String?, schema: Schema, paramNameOrType: String?): Param {
    val paramName = (paramNameOrType ?: schema.className(operationId)).toCamelCase()
    return Param(
      paramName,
      schema.toModel(operationId, paramName),
      schema.isNullable ?: !schema.required.contains(paramName),
      schema.description,
      schema.defaultArgument(paramName)
    )
  }

  private fun Schema.Property(
    operationId: String?,
    paramName: String,
    schema: Schema
  ): Product.Property =
    Product.Property(
      paramName.toCamelCase(),
      schema.toModel(operationId, paramName),
      schema.isNullable ?: !required.contains(paramName),
      schema.description,
      schema.defaultArgument(paramName)
    )

  private fun Schema.defaultArgument(paramName: String): String? =
    if (enum != null) {
      val defaultValue = (default as? ExampleValue.Single)?.value
      if (defaultValue != null) "${paramName.toPascalCase()}.${(default as? ExampleValue.Single)?.value}"
      else null
    } else default?.toString()

  private fun Response.isEmpty(): Boolean =
    headers.isEmpty() && content.isEmpty() && links.isEmpty() && extensions.isEmpty()
}
