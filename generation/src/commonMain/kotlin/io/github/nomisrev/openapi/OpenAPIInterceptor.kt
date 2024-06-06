package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Schema.Type
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Object.Property.DefaultArgument
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
interface OpenAPISyntax {
  fun Schema.toModel(context: NamingContext): Model
  fun Schema.topLevelNameOrNull(): String?
  fun Schema.isTopLevel(): Boolean =
    topLevelNameOrNull() != null

  fun ReferenceOr.Reference.namedSchema(): Pair<String, Schema>

  fun ReferenceOr<Schema>.get(): Schema
  fun ReferenceOr<Response>.get(): Response
  fun ReferenceOr<Parameter>.get(): Parameter
  fun ReferenceOr<RequestBody>.get(): RequestBody
  fun ReferenceOr<PathItem>.get(): PathItem
}

/**
 * This class exposes the entire behavior of the transformation,
 * it works in two phases.
 *
 * 1. Collecting phase:
 * OpenAPI is traversed,
 * and calls the [OpenAPISyntax] functions to transform the OpenAPI data into [Model].
 * [OpenAPISyntax] allows special syntax such as [ReferenceOr.valueOrNull],
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
interface OpenAPIInterceptor {
  fun OpenAPISyntax.defaultArgument(model: Model, pSchema: Schema, context: NamingContext): DefaultArgument?

  fun OpenAPISyntax.toPropertyContext(
    key: String,
    propSchema: Schema,
    parentSchema: Schema,
    original: NamingContext
  ): NamingContext.Param

  fun OpenAPISyntax.toObject(
    context: NamingContext,
    schema: Schema,
    required: List<String>,
    properties: Map<String, ReferenceOr<Schema>>
  ): Model

  fun OpenAPISyntax.toMap(
    context: NamingContext,
    schema: Schema,
    additionalSchema: AdditionalProperties.PSchema
  ): Model

  fun OpenAPISyntax.toRawJson(allowed: Allowed): Model

  fun OpenAPISyntax.toPrimitive(context: NamingContext, schema: Schema, basic: Type.Basic): Model

  fun OpenAPISyntax.type(
    context: NamingContext,
    schema: Schema,
    type: Type
  ): Model

  fun OpenAPISyntax.toEnum(
    context: NamingContext,
    schema: Schema,
    type: Type,
    enums: List<String>
  ): Model

  fun OpenAPISyntax.toAnyOf(
    context: NamingContext,
    schema: Schema,
    anyOf: List<ReferenceOr<Schema>>,
  ): Model

  fun OpenAPISyntax.toOneOf(
    context: NamingContext,
    schema: Schema,
    oneOf: List<ReferenceOr<Schema>>,
  ): Model

  fun OpenAPISyntax.toUnionCaseContext(
    context: NamingContext,
    caseSchema: ReferenceOr<Schema>
  ): NamingContext

  fun OpenAPISyntax.toRequestBody(operation: Operation, body: RequestBody?): Route.Bodies
  fun OpenAPISyntax.toResponses(operation: Operation): Route.Returns

  // Recover from missing responses
  fun OpenAPISyntax.responseNotSupported(
    operation: Operation,
    response: Response,
    code: StatusCode
  ): Route.ReturnType =
    throw IllegalStateException("OpenAPI requires at least 1 response")

  // model transformers
  fun `object`(
    context: NamingContext,
    schema: Schema,
    required: List<String>,
    properties: Map<String, ReferenceOr<Schema>>,
    model: Model
  ): Model = model

  fun map(
    context: NamingContext,
    pSchema: AdditionalProperties.PSchema,
    model: Model
  ): Model = model

  fun rawJson(allowed: Allowed, model: Model): Model = model

  fun primitive(context: NamingContext, schema: Schema, basic: Type.Basic, model: Model): Model =
    model

  fun OpenAPISyntax.type(
    context: NamingContext,
    schema: Schema,
    type: Type,
    model: Model
  ): Model = model

  fun enum(
    context: NamingContext,
    schema: Schema,
    type: Type,
    enum: List<String>,
    model: Model
  ): Model = model

  fun anyOf(
    context: NamingContext,
    schema: Schema,
    anyOf: List<ReferenceOr<Schema>>,
    model: Model
  ): Model = model

  fun oneOf(
    context: NamingContext,
    schema: Schema,
    oneOf: List<ReferenceOr<Schema>>,
    model: Model
  ): Model = model

  fun requestBodies(
    operationId: String?,
    requestBody: RequestBody,
    models: List<Route.Body>
  ): List<Route.Body> = models

  fun response(
    operation: Operation,
    statusCode: StatusCode,
    model: Route.ReturnType,
  ): Route.ReturnType = model

  companion object Default : OpenAPIInterceptor {
    override fun OpenAPISyntax.toPropertyContext(
      key: String,
      propSchema: Schema,
      parentSchema: Schema,
      original: NamingContext
    ): NamingContext.Param =
      propSchema.topLevelNameOrNull()?.let { name ->
        NamingContext.Ref(name, original)
      } ?: NamingContext.Inline(key, original)

    private fun Schema.singleDefaultOrNull(): String? =
      (default as? ExampleValue.Single)?.value

    // text-moderation-latest
    // CreateModerationRequestModel.CaseModelEnum(CreateModerationRequestModel.ModelEnum.TextModerationLatest)
    override fun OpenAPISyntax.defaultArgument(
      model: Model,
      pSchema: Schema,
      pContext: NamingContext
    ): DefaultArgument? =
      when {
//                pSchema.type is Type.Array ->
//                    DefaultArgument.List(
//                        (pSchema.type as Type.Array).types.mapNotNull {
//                            defaultArgument(Schema(type = it).toModel(pContext), pSchema, pContext)
//                        })

        else -> pSchema.default?.toString()?.let { s ->
          DefaultArgument.Other(s)
        }
      }

    override fun OpenAPISyntax.toObject(
      context: NamingContext,
      schema: Schema,
      required: List<String>,
      properties: Map<String, ReferenceOr<Schema>>
    ): Model = Model.Object(
      schema,
      context,
      schema.description,
      properties.map { (name, ref) ->
        val pSchema = ref.get()
        val pContext = toPropertyContext(name, pSchema, schema, context)
        val model = pSchema.toModel(pContext)
        // TODO Property interceptor
        Property(
          pSchema,
          name,
          pContext.name,
          model,
          schema.required?.contains(name) == true,
          pSchema.nullable ?: schema.required?.contains(name)?.not() ?: true,
          pSchema.description
        )
      },
      properties.mapNotNull { (name, ref) ->
        val pSchema = ref.get()
        pSchema.toModel(toPropertyContext(name, pSchema, schema, context))
      }.filterNot { it is Primitive }
    ).let { `object`(context, schema, required, properties, it) }

    override fun OpenAPISyntax.toMap(
      context: NamingContext,
      schema: Schema,
      additionalSchema: AdditionalProperties.PSchema
    ): Model =
      map(context, additionalSchema, Collection.Map(schema, additionalSchema.value.get().toModel(context)))

    override fun OpenAPISyntax.toRawJson(allowed: Allowed): Model =
      if (allowed.value) rawJson(allowed, Model.FreeFormJson)
      else throw IllegalStateException("Illegal State: No additional properties allowed on empty object.")

    override fun OpenAPISyntax.toPrimitive(
      context: NamingContext,
      schema: Schema,
      basic: Type.Basic
    ): Model =
      when (basic) {
        Type.Basic.Object -> toRawJson(Allowed(true))
        Type.Basic.Boolean -> Primitive.Boolean(schema, schema.default?.toString()?.toBoolean())
        Type.Basic.Integer -> Primitive.Int(schema, schema.default?.toString()?.toIntOrNull())
        Type.Basic.Number -> Primitive.Double(schema, schema.default?.toString()?.toDoubleOrNull())
        Type.Basic.Array -> collection(schema, context)
        Type.Basic.String ->
          if (schema.format == "binary") Model.Binary
          else Primitive.String(schema, schema.default?.toString())
        Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
      }.let { primitive(context, schema, basic, it) }

    private fun OpenAPISyntax.collection(
      schema: Schema,
      context: NamingContext
    ): Collection {
      val items = requireNotNull(schema.items) { "Array type requires items to be defined." }
      val inner = when (items) {
        is ReferenceOr.Reference ->
          schema.items!!.get().toModel(NamingContext.TopLevelSchema(items.ref.drop(schemaRef.length)))

        is ReferenceOr.Value -> items.value.toModel(context)
      }
      val default = when (val example = schema.default) {
        is ExampleValue.Multiple -> example.values
        is ExampleValue.Single ->
          when (val value = example.value) {
            "[]" -> emptyList()
            else -> listOf(value)
          }

        null -> null
      }
      return if (schema.uniqueItems == true) Collection.Set(schema, inner, default)
      else Collection.List(schema, inner, default)
    }

    override fun OpenAPISyntax.type(
      context: NamingContext,
      schema: Schema,
      type: Type
    ): Model = when (type) {
      is Type.Array -> when {
        type.types.size == 1 -> type(context, schema.copy(type = type.types.single()), type.types.single())
        else -> TypeArray(schema, context, type.types.sorted().map {
          Model.Union.UnionEntry(context, Schema(type = it).toModel(context))
        }, emptyList())
      }

      is Type.Basic -> toPrimitive(context, schema, type)
    }.let { type(context, schema, type, it) }

    override fun OpenAPISyntax.toEnum(
      context: NamingContext,
      schema: Schema,
      type: Type,
      enums: List<String>
    ): Model {
      require(enums.isNotEmpty()) { "Enum requires at least 1 possible value" }
      /* To resolve the inner type, we erase the enum values.
       * Since the schema is still on the same level - we keep the topLevelName */
      val inner = schema.copy(enum = null).toModel(context)
      val default = schema.singleDefaultOrNull()
      val kenum = Model.Enum(schema, context, inner, enums, default)
      return enum(context, schema, type, enums, kenum)
    }

    // Support AnyOf = null | A, should become A?
    override fun OpenAPISyntax.toAnyOf(
      context: NamingContext,
      schema: Schema,
      anyOf: List<ReferenceOr<Schema>>,
    ): Model =
      anyOf(context, schema, anyOf, toUnion(context, schema, anyOf, Model.Union::AnyOf))

    override fun OpenAPISyntax.toOneOf(
      context: NamingContext,
      schema: Schema,
      oneOf: List<ReferenceOr<Schema>>,
    ): Model =
      oneOf(context, schema, oneOf, toUnion(context, schema, oneOf, Model.Union::OneOf))

    override fun OpenAPISyntax.toUnionCaseContext(
      context: NamingContext,
      caseSchema: ReferenceOr<Schema>
    ): NamingContext = when (caseSchema) {
      is ReferenceOr.Reference -> NamingContext.TopLevelSchema(caseSchema.ref.drop(schemaRef.length))
      is ReferenceOr.Value -> when (context) {
        is NamingContext.Inline -> when {
          caseSchema.value.type != null && caseSchema.value.type is Type.Array ->
            throw IllegalStateException("Cannot generate name for $caseSchema, ctx: $context.")

          caseSchema.value.enum != null -> context.copy(name = context.name + "_enum")
          else -> context
        }
        /*
         * TODO !!!!
         *   Top-level OneOf with inline schemas
         *   => how to generate names?
         *
         * - ChatCompletionToolChoiceOption
         *  -> enum (???)
         *  -> ChatCompletionNamedToolChoice
         */
        is NamingContext.TopLevelSchema -> generateName(context, caseSchema.value)

        is NamingContext.RouteParam -> context
        is NamingContext.Ref -> context
      }
    }

    private fun OpenAPISyntax.generateName(
      context: NamingContext.TopLevelSchema,
      schema: Schema
    ): NamingContext.TopLevelSchema =
      when (val type = schema.type) {
        is Type.Array -> TODO()
        Type.Basic.Array -> {
          val inner =
            requireNotNull(schema.items) { "Array type requires items to be defined." }
          inner.get().type
          TODO()
        }

        Type.Basic.Object -> context.copy(name = "CaseJson")
        Type.Basic.Number -> context.copy(name = "CaseDouble")
        Type.Basic.Boolean -> context.copy(name = "CaseBool")
        Type.Basic.Integer -> context.copy(name = "CaseInt")
        Type.Basic.Null -> TODO()
        Type.Basic.String -> when (val enum = schema.enum) {
          null -> context.copy(name = "CaseString")
          else -> context.copy(name = enum.joinToString(prefix = "", separator = "Or"))
        }

        null -> TODO()
      }

    private fun <A : Model> OpenAPISyntax.toUnion(
      context: NamingContext,
      schema: Schema,
      subtypes: List<ReferenceOr<Schema>>,
      transform: (Schema, NamingContext, List<Model.Union.UnionEntry>, inline: List<Model>, String?) -> A
    ): A {
      val inline = subtypes.mapNotNull { ref ->
        ref.valueOrNull()?.toModel(toUnionCaseContext(context, ref))
      }
      return transform(
        schema,
        context,
        subtypes.map { ref ->
          val caseContext = toUnionCaseContext(context, ref)
          Model.Union.UnionEntry(caseContext, ref.get().toModel(caseContext))
        },
        inline,
        // TODO We need to check the parent for default?
        inline.firstNotNullOfOrNull { (it as? Primitive.String)?.schema?.singleDefaultOrNull() }
      )
    }

    // TODO interceptor
    override fun OpenAPISyntax.toRequestBody(operation: Operation, body: RequestBody?): Route.Bodies =
      Route.Bodies(
        body?.content?.entries?.associate { (contentType, mediaType) ->
          when {
            ApplicationXml.matches(contentType) -> TODO("Add support for XML.")
            ApplicationJson.matches(contentType) -> {
              val json = when (val s = mediaType.schema) {
                is ReferenceOr.Reference -> {
                  val (name, schema) = s.namedSchema()
                  Route.Body.Json(
                    schema.toModel(NamingContext.TopLevelSchema(name)),
                    mediaType.extensions
                  )
                }

                is ReferenceOr.Value ->
                  Route.Body.Json(
                    s.value.toModel(
                      requireNotNull(
                        operation.operationId?.let { NamingContext.TopLevelSchema("${it}Request") }
                      ) { "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name" }
                    ), mediaType.extensions
                  )

                null -> Route.Body.Json(
                  Model.FreeFormJson, mediaType.extensions
                )
              }
              Pair(ApplicationJson, json)
            }

            MultipartFormData.matches(contentType) -> {
              fun ctx(name: String): NamingContext = when (val s = mediaType.schema) {
                is ReferenceOr.Reference -> NamingContext.TopLevelSchema(s.namedSchema().first)
                is ReferenceOr.Value -> NamingContext.RouteParam(
                  name,
                  operation.operationId,
                  "Request"
                )

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
    override fun OpenAPISyntax.toResponses(operation: Operation): Route.Returns =
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
                    Route.ReturnType(
                      schema.toModel(NamingContext.TopLevelSchema(name)),
                      operation.responses.extensions
                    )
                  )
                }

                is ReferenceOr.Value -> Pair(statusCode, Route.ReturnType(s.value.toModel(
                  requireNotNull(operation.operationId?.let { NamingContext.TopLevelSchema("${it}Response") }) {
                    "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                  }), response.extensions
                )
                )

                null -> Pair(
                  statusCode,
                  Route.ReturnType(toRawJson(Allowed(true)), response.extensions)
                )
              }
            }

            response.isEmpty() -> Pair(
              statusCode,
              Route.ReturnType(
                Primitive.String(Schema(type = Schema.Type.Basic.String), null),
                response.extensions
              )
            )

            else -> Pair(statusCode, responseNotSupported(operation, response, statusCode))
          }.let { (code, response) -> Pair(code, response(operation, statusCode, response)) }
        },
        operation.responses.extensions
      )
  }
}