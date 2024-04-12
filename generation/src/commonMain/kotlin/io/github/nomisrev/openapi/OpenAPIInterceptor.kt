package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Schema.Type
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.Model.Union.TypeArray
import io.github.nomisrev.openapi.http.MediaType.Companion.ApplicationJson
import io.github.nomisrev.openapi.http.MediaType.Companion.ApplicationOctetStream
import io.github.nomisrev.openapi.http.MediaType.Companion.ApplicationXml
import io.github.nomisrev.openapi.http.MediaType.Companion.MultipartFormData
import io.github.nomisrev.openapi.http.StatusCode

internal const val schemaRef = "#/components/schemas/"
internal const val responsesRef = "#/components/responses/"
internal const val parametersRef = "#/components/parameters/"
internal const val requestBodiesRef = "#/components/requestBodies/"
internal const val pathItemsRef = "#/components/pathItems/"

private const val applicationJson = "application/json"
private const val applicationOctectStream = "application/octet-stream"

private fun Response.isEmpty(): Boolean =
  headers.isEmpty() && content.isEmpty() && links.isEmpty() && extensions.isEmpty()

/**
 * DSL you have available within the [OpenAPIInterceptor],
 * which allows you to retrieve references,
 * or access the top-level information.
 */
public interface OpenAPISyntax {
  public fun Schema.toModel(context: NamingContext): Model
  public fun Schema.topLevelNameOrNull(): String?

  public fun ReferenceOr.Reference.namedSchema(): Pair<String, Schema>

  public fun ReferenceOr<Schema>.get(): Schema
  public fun ReferenceOr<Response>.get(): Response
  public fun ReferenceOr<Parameter>.get(): Parameter
  public fun ReferenceOr<RequestBody>.get(): RequestBody
  public fun ReferenceOr<PathItem>.get(): PathItem
}

/**
 * This class exposes the entire behavior of the transformation,
 * it works in two phases.
 *
 * 1. Collecting phase:
 * OpenAPI is traversed,
 * and calls the [OpenAPISyntax] functions to transform the OpenAPI data into [Model].
 * [OpenAPISyntax] allows special syntax such as [ReferenceOr.get],
 * which allows resolving a reference everywhere in the DSL.
 * **IMPORTANT:** [Model] ADT is structured, and nested in the same way as the OpenAPI.
 * Meaning that if a class is _inline_ it should be generated _nested_ like [Model.Object.inline].
 *
 * Any of this functionality can be overwritten,
 * for example `toEnumName` to modify the `Enum` naming strategy.
 * This is how we'll make it configurable ourselves as well.
 *
 * 2. Interception phase:
 * This phase is purely transformational, it comes right after the phase 1 and allows you to apply a
 * transformation from `(KModel) -> KModel` with same data as step 1.
 *
 * This is more convenient to modify simple things like `required`, `isNullable`, etc.
 */
public interface OpenAPIInterceptor {
  public fun OpenAPISyntax.toObject(
    context: NamingContext,
    schema: Schema,
    required: List<String>,
    properties: Map<String, ReferenceOr<Schema>>
  ): Model

  public fun OpenAPISyntax.toMap(
    context: NamingContext,
    pSchema: AdditionalProperties.PSchema
  ): Model

  public fun OpenAPISyntax.toRawJson(allowed: Allowed): Model

  public fun OpenAPISyntax.toPrimitive(context: NamingContext, schema: Schema, basic: Type.Basic): Model

  public fun OpenAPISyntax.type(
    context: NamingContext,
    schema: Schema,
    type: Type
  ): Model

  public fun OpenAPISyntax.toEnum(
    context: NamingContext,
    schema: Schema,
    type: Type,
    enums: List<String>
  ): Model

  public fun OpenAPISyntax.toAnyOf(
    context: NamingContext,
    schema: Schema,
    anyOf: List<ReferenceOr<Schema>>,
  ): Model

  public fun OpenAPISyntax.toOneOf(
    context: NamingContext,
    schema: Schema,
    oneOf: List<ReferenceOr<Schema>>,
  ): Model

  public fun OpenAPISyntax.toRequestBody(operation: Operation, body: RequestBody?): Route.Bodies
  public fun OpenAPISyntax.toResponses(operation: Operation): Route.Returns

  // Recover from missing responses
  public fun OpenAPISyntax.responseNotSupported(
    operation: Operation,
    response: Response,
    code: StatusCode
  ): Route.ReturnType =
    throw IllegalStateException("OpenAPI requires at least 1 response")

  // model transformers
  public fun `object`(
    context: NamingContext,
    schema: Schema,
    required: List<String>,
    properties: Map<String, ReferenceOr<Schema>>,
    model: Model
  ): Model = model

  public fun map(
    context: NamingContext,
    pSchema: AdditionalProperties.PSchema,
    model: Model
  ): Model = model

  public fun rawJson(allowed: Allowed, model: Model): Model = model

  public fun primitive(context: NamingContext, schema: Schema, basic: Type.Basic, model: Model): Model =
    model

  public fun OpenAPISyntax.type(
    context: NamingContext,
    schema: Schema,
    type: Type,
    model: Model
  ): Model = model

  public fun enum(
    context: NamingContext,
    schema: Schema,
    type: Type,
    enum: List<String>,
    model: Model
  ): Model = model

  public fun anyOf(
    context: NamingContext,
    schema: Schema,
    anyOf: List<ReferenceOr<Schema>>,
    model: Model
  ): Model = model

  public fun oneOf(
    context: NamingContext,
    schema: Schema,
    oneOf: List<ReferenceOr<Schema>>,
    model: Model
  ): Model = model

  public fun requestBodies(
    operationId: String?,
    requestBody: RequestBody,
    models: List<Route.Body>
  ): List<Route.Body> = models

  public fun response(
    operation: Operation,
    statusCode: StatusCode,
    model: Route.ReturnType,
  ): Route.ReturnType = model

  public companion object Default : OpenAPIInterceptor {
    // TODO add interceptor
    public fun OpenAPISyntax.toPropertyContext(
      key: String,
      propSchema: Schema,
      parentSchema: Schema,
      original: NamingContext
    ): NamingContext.Param =
      propSchema.topLevelNameOrNull()?.let { name ->
        NamingContext.Ref(name, original)
      } ?: NamingContext.Inline(key, original)

    sealed interface DefaultArgument {
      data class Enum(val value: String) : DefaultArgument
      data class Union(val unionCaseName: Model, val value: String) : DefaultArgument
      data class Other(val value: String) : DefaultArgument
    }

    private fun Schema.singleDefaultOrNull(): String? =
      (default as? ExampleValue.Single)?.value

    fun OpenAPISyntax.defaultArgument(schema: Schema, context: NamingContext): DefaultArgument? =
      when {
        schema.enum != null -> schema.singleDefaultOrNull()?.let(DefaultArgument::Enum)
        schema.oneOf != null ->
          schema.oneOf!!.firstNotNullOfOrNull { refOfS ->
            val s = refOfS.get()
            s.takeIf { it.type == Schema.Type.Basic.String }
              ?.singleDefaultOrNull()
              ?.let { DefaultArgument.Union(s.toModel(context), it) }
          }

        else -> schema.default?.toString()?.let { DefaultArgument.Other(it) }
      }

    public override fun OpenAPISyntax.toObject(
      context: NamingContext,
      schema: Schema,
      required: List<String>,
      properties: Map<String, ReferenceOr<Schema>>
    ): Model = Model.Object(
      context,
      schema.description,
      properties.map { (name, ref) ->
        val pSchema = ref.get()
        val pContext = toPropertyContext(name, pSchema, schema, context)
        // TODO Property interceptor
        Property(
          name,
          pContext.content,
          pSchema.toModel(pContext),
          schema.required?.contains(name) == true,
          pSchema.nullable ?: true,
          pSchema.description,
          defaultArgument(pSchema, context)
        )
      },
      properties.mapNotNull { (name, ref) ->
        val pSchema = ref.get()
        pSchema.toModel(toPropertyContext(name, pSchema, schema, context))
      }.filterNot { it is Primitive }
    ).let { `object`(context, schema, required, properties, it) }

    public override fun OpenAPISyntax.toMap(
      context: NamingContext,
      pSchema: AdditionalProperties.PSchema
    ): Model =
      map(context, pSchema, Collection.Map(pSchema.value.get().toModel(context)))

    public override fun OpenAPISyntax.toRawJson(allowed: Allowed): Model =
      if (allowed.value) rawJson(allowed, Model.FreeFormJson)
      else throw IllegalStateException("Illegal State: No additional properties allowed on empty object.")

    public override fun OpenAPISyntax.toPrimitive(context: NamingContext, schema: Schema, basic: Type.Basic): Model =
      when (basic) {
        Type.Basic.Object -> toRawJson(Allowed(true))
        Type.Basic.Boolean -> Primitive.Boolean
        Type.Basic.Integer -> Primitive.Int
        Type.Basic.Number -> Primitive.Double
        Type.Basic.String -> if (schema.format == "binary") Model.Binary else Primitive.String
        Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
        Type.Basic.Array -> {
          val inner = requireNotNull(schema.items) { "Array type requires items to be defined." }
            .get().toModel(context)
          if (schema.uniqueItems == true) Collection.Set(inner) else Collection.List(inner)
        }
      }.let { primitive(context, schema, basic, it) }

    public override fun OpenAPISyntax.type(
      context: NamingContext,
      schema: Schema,
      type: Type
    ): Model = when (type) {
      is Type.Array -> when {
        type.types.size == 1 -> type(context, schema.copy(type = type.types.single()), type.types.single())
        else -> TypeArray(context, type.types.sorted().map {
          Model.Union.UnionEntry(context, Schema(type = it).toModel(context))
        })
      }

      is Type.Basic -> toPrimitive(context, schema, type)
    }.let { type(context, schema, type, it) }

    public override fun OpenAPISyntax.toEnum(
      context: NamingContext,
      schema: Schema,
      type: Type,
      enums: List<String>
    ): Model {
      require(enums.isNotEmpty()) { "Enum requires at least 1 possible value" }
      /* To resolve the inner type, we erase the enum values.
       * Since the schema is still on the same level - we keep the topLevelName */
      val inner = schema.copy(enum = null).toModel(context)
      val kenum = Model.Enum(context, inner, enums)
      return enum(context, schema, type, enums, kenum)
    }

    // Support AnyOf = null | A, should become A?
    public override fun OpenAPISyntax.toAnyOf(
      context: NamingContext,
      schema: Schema,
      anyOf: List<ReferenceOr<Schema>>,
    ): Model =
      anyOf(context, schema, anyOf, toUnion(context, schema, anyOf, Model.Union::AnyOf))

    public override fun OpenAPISyntax.toOneOf(
      context: NamingContext,
      schema: Schema,
      oneOf: List<ReferenceOr<Schema>>,
    ): Model =
      oneOf(context, schema, oneOf, toUnion(context, schema, oneOf, Model.Union::OneOf))

    public fun OpenAPISyntax.toUnionCaseContext(
      context: NamingContext,
      caseSchema: ReferenceOr<Schema>
    ): NamingContext = when (caseSchema) {
      // TODO, can we register ReferenceOr.Reference to implement unionName directly?
      is ReferenceOr.Reference -> NamingContext.ClassName(caseSchema.ref.drop(schemaRef.length))
      is ReferenceOr.Value -> when (context) {
        is NamingContext.Inline ->
          if (caseSchema.value.type != null && caseSchema.value.type !is Type.Array) context
          else throw IllegalStateException("Cannot generate OneOf name for $this.")

        else -> context
      }
    }

    private fun <A : Model> OpenAPISyntax.toUnion(
      context: NamingContext,
      schema: Schema,
      subtypes: List<ReferenceOr<Schema>>,
      transform: (NamingContext, List<Model.Union.UnionEntry>) -> A
    ): A {
      return transform(context, subtypes.map { ref ->
        val caseContext = toUnionCaseContext(context, ref)
        Model.Union.UnionEntry(caseContext, ref.get().toModel(caseContext))
      })
    }

    // TODO interceptor
    public override fun OpenAPISyntax.toRequestBody(operation: Operation, body: RequestBody?): Route.Bodies =
      Route.Bodies(
        body?.content?.entries?.associate { (contentType, mediaType) ->
          when {
            ApplicationXml.matches(contentType) -> TODO("Add support for XML.")
            ApplicationJson.matches(contentType) -> {
              val json = when (val s = mediaType.schema) {
                is ReferenceOr.Reference -> {
                  val (name, schema) = s.namedSchema()
                  Route.Body.Json(schema.toModel(NamingContext.ClassName(name)), mediaType.extensions)
                }

                is ReferenceOr.Value ->
                  Route.Body.Json(
                    s.value.toModel(
                      requireNotNull(
                        operation.operationId?.let { NamingContext.ClassName("${it}Request") }
                      ) { "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name" }
                    ), mediaType.extensions
                  )

                null -> Route.Body.Json(Model.FreeFormJson, mediaType.extensions)
              }
              Pair(ApplicationJson, json)
            }

            MultipartFormData.matches(contentType) -> {
              fun ctx(name: String): NamingContext = when (val s = mediaType.schema) {
                is ReferenceOr.Reference -> NamingContext.ClassName(s.namedSchema().first)
                is ReferenceOr.Value -> NamingContext.OperationParam(name, operation.operationId, "Request")
                null -> throw IllegalStateException("$mediaType without a schema. Generation doesn't know what to do, please open a ticket!")
              }

              val props = requireNotNull(mediaType.schema!!.get().properties) {
                "Generating multipart/form-data bodies without properties is not possible."
              }
              require(props.isNotEmpty()) { "Generating multipart/form-data bodies without properties is not possible." }
              Pair(MultipartFormData, Route.Body.Multipart(props.map { (name, ref) ->
                Route.Body.Multipart.FormData(
                  name,
                  ref.get().toModel(ctx(name))
                )
              }, mediaType.extensions))
            }

            ApplicationOctetStream.matches(contentType) ->
              Pair(ApplicationOctetStream, Route.Body.OctetStream(mediaType.extensions))

            else -> throw IllegalStateException("RequestBody content type: $this not yet supported.")
          }
        }.orEmpty(), body?.extensions.orEmpty()
      )

    private fun Response.isEmpty(): Boolean =
      headers.isEmpty() && content.isEmpty() && links.isEmpty() && extensions.isEmpty()

    // TODO interceptor
    public override fun OpenAPISyntax.toResponses(operation: Operation): Route.Returns =
      Route.Returns(
        operation.responses.responses.entries.associate { (code, refOrResponse) ->
          val statusCode = StatusCode.orThrow(code)
          val response = refOrResponse.get()
          when {
            response.content.contains(applicationOctectStream) ->
              Pair(statusCode, Route.ReturnType(Model.Binary, response.extensions))

            response.content.contains(applicationJson) -> {
              val mediaType = response.content.getValue(applicationJson)
              when (val s = mediaType.schema) {
                is ReferenceOr.Reference -> {
                  val (name, schema) = s.namedSchema()
                  Pair(
                    statusCode,
                    Route.ReturnType(schema.toModel(NamingContext.ClassName(name)), operation.responses.extensions)
                  )
                }

                is ReferenceOr.Value -> Pair(statusCode, Route.ReturnType(s.value.toModel(
                  requireNotNull(operation.operationId?.let { NamingContext.ClassName("${it}Response") }) {
                    "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                  }), response.extensions
                )
                )

                null -> Pair(statusCode, Route.ReturnType(toRawJson(Allowed(true)), response.extensions))
              }
            }

            response.isEmpty() -> Pair(statusCode, Route.ReturnType(Primitive.Unit, response.extensions))
            else -> Pair(statusCode, responseNotSupported(operation, response, statusCode))
          }.let { (code, response) -> Pair(code, response(operation, statusCode, response)) }
        },
        operation.responses.extensions
      )
  }
}