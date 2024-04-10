package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.AdditionalProperties
import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Operation
import io.github.nomisrev.openapi.ReferenceOr
import io.github.nomisrev.openapi.RequestBody
import io.github.nomisrev.openapi.Response
import io.github.nomisrev.openapi.Schema
import io.github.nomisrev.openapi.Schema.Type
import io.github.nomisrev.openapi.applicationJson
import io.github.nomisrev.openapi.defaultArgument
import io.github.nomisrev.openapi.isEmpty
import io.github.nomisrev.openapi.multipartData
import io.github.nomisrev.openapi.schemaRef
import io.github.nomisrev.openapi.test.KModel.Collection
import io.github.nomisrev.openapi.test.KModel.Object.Property
import io.github.nomisrev.openapi.test.KModel.Primitive
import io.github.nomisrev.openapi.test.KModel.Union.TypeArray
import io.github.nomisrev.openapi.toPascalCase

/**
 * This class exposes the entire behavior of the transformation,
 * it works in two phases.
 *
 * 1. Collecting phase:
 * OpenAPI is traversed,
 * and calls the [OpenAPISyntax] functions to transform the OpenAPI data into [KModel].
 * [OpenAPISyntax] allows special syntax such as [ReferenceOr.get],
 * which allows resolving a reference everywhere in the DSL.
 * **IMPORTANT:** [KModel] ADT is structured, and nested in the same way as the OpenAPI.
 * Meaning that if a class is _inline_ it should be generated _nested_ like [KModel.Object.inline].
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
  ): KModel

  public fun OpenAPISyntax.toMap(
    context: NamingContext,
    pSchema: AdditionalProperties.PSchema
  ): KModel

  public fun OpenAPISyntax.toRawJson(allowed: Allowed): KModel

  public fun OpenAPISyntax.toPrimitive(context: NamingContext, schema: Schema, basic: Type.Basic): KModel

  public fun OpenAPISyntax.type(
    context: NamingContext,
    schema: Schema,
    type: Type
  ): KModel

  public fun toEnumName(context: NamingContext): String

  public fun OpenAPISyntax.toEnum(
    context: NamingContext,
    schema: Schema,
    type: Type,
    enum: List<String>
  ): KModel

  public fun toUnionName(context: NamingContext): String

  public fun OpenAPISyntax.toAnyOf(
    context: NamingContext,
    schema: Schema,
    anyOf: List<ReferenceOr<Schema>>,
  ): KModel

  public fun OpenAPISyntax.toOneOf(
    context: NamingContext,
    schema: Schema,
    oneOf: List<ReferenceOr<Schema>>,
  ): KModel

  /**
   * This is the hard piece of generating union types (oneOf, anyOf).
   * Depending on where the union occurs, we need a different name.
   *  1. Top-level case schema, use user defined name
   *  2. _Inline case schema_, if primitive we generate CasePrimitive
   *     => Int, Ints for List<Int>, IntsList for List<List<Int>>.
   *     => duplicate schemas can be filtered out.
   */
  public fun OpenAPISyntax.toUnionCaseContext(
    context: NamingContext,
    unionName: String,
    unionSchema: Schema,
    caseSchema: ReferenceOr<Schema>,
    caseIndex: Int
  ): NamingContext

  public fun OpenAPISyntax.toRequestBody(operation: Operation, body: RequestBody): KRoute.Body
  public fun OpenAPISyntax.toInlineResponsesOrNull(operation: Operation): KModel?
  public fun OpenAPISyntax.toResponses(operation: Operation): KModel?

  // Recover from missing responses
  public fun OpenAPISyntax.responseNotSupported(operation: Operation, response: Response, code: Int): KModel? =
    throw IllegalStateException("OpenAPI requires at least 1 response")

  // model transformers
  public fun `object`(
    context: NamingContext,
    schema: Schema,
    required: List<String>,
    properties: Map<String, ReferenceOr<Schema>>,
    model: KModel
  ): KModel = model

  public fun map(
    context: NamingContext,
    pSchema: AdditionalProperties.PSchema,
    model: KModel
  ): KModel = model

  public fun rawJson(allowed: Allowed, model: KModel): KModel = model

  public fun primitive(context: NamingContext, schema: Schema, basic: Type.Basic, model: KModel): KModel =
    model

  public fun OpenAPISyntax.type(
    context: NamingContext,
    schema: Schema,
    type: Type,
    model: KModel
  ): KModel = model

  public fun enum(
    context: NamingContext,
    schema: Schema,
    type: Type,
    enum: List<String>,
    model: KModel
  ): KModel = model

  public fun anyOf(
    context: NamingContext,
    schema: Schema,
    anyOf: List<ReferenceOr<Schema>>,
    model: KModel
  ): KModel = model

  public fun oneOf(
    context: NamingContext,
    schema: Schema,
    oneOf: List<ReferenceOr<Schema>>,
    model: KModel
  ): KModel = model

  public fun requestBodies(
    operationId: String?,
    requestBody: RequestBody,
    models: List<KModel>
  ): List<KModel> = models

  public fun response(
    operation: Operation,
    model: KModel
  ): KModel = model

  public companion object Default : OpenAPIInterceptor {
    public override fun OpenAPISyntax.toObject(
      context: NamingContext,
      schema: Schema,
      required: List<String>,
      properties: Map<String, ReferenceOr<Schema>>
    ): KModel = KModel.Object(
      context.content,
      schema.description,
      properties.map { (paramName, ref) ->
        val pContext = ref.getOrNull()?.let {
          NamingContext.Inline(paramName, context)
        } ?: NamingContext.Ref(paramName, context)
        val pSchema = ref.get()
        Property(
          paramName,
          pSchema.toModel(pContext),
          required.contains(paramName),
          pSchema.nullable ?: true,
          pSchema.description,
          pSchema.defaultArgument(paramName)
        )
      },
      properties.mapNotNull { (paramName, ref) ->
        val model = ref.getOrNull()?.toModel(NamingContext.Inline(paramName, context))
        if (model is Collection) model.value else model
      }.filterNot { it is Primitive }
    ).let { `object`(context, schema, required, properties, it) }

    public override fun OpenAPISyntax.toMap(
      context: NamingContext,
      pSchema: AdditionalProperties.PSchema
    ): KModel =
      map(context, pSchema, Collection.Map(pSchema.value.get().toModel(context)))

    public override fun OpenAPISyntax.toRawJson(allowed: Allowed): KModel =
      if (allowed.value) rawJson(allowed, KModel.JsonObject)
      else throw IllegalStateException("Illegal State: No additional properties allowed on empty object.")

    public override fun OpenAPISyntax.toPrimitive(context: NamingContext, schema: Schema, basic: Type.Basic): KModel =
      when (basic) {
        Type.Basic.Object -> toRawJson(Allowed(true))
        Type.Basic.Boolean -> Primitive.Boolean
        Type.Basic.Integer -> Primitive.Int
        Type.Basic.Number -> Primitive.Double
        Type.Basic.String -> Primitive.String
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
    ): KModel = when (type) {
      is Type.Array -> when {
        type.types.size == 1 -> type(context, schema.copy(type = type.types.single()), type.types.single())
        else -> TypeArray(toUnionName(context), type.types.sorted().map {
          toPrimitive(context, Schema(type = it), it)
        })
      }

      is Type.Basic -> toPrimitive(context, schema, type)
    }.let { type(context, schema, type, it) }

    public override fun toEnumName(context: NamingContext): String =
      when (context) {
        is NamingContext.Inline -> context.content.toPascalCase()
        is NamingContext.Ref -> context.outer.content
        is NamingContext.ClassName -> context.content
        is NamingContext.OperationParam -> {
          requireNotNull(context.operationId) { "Need operationId to generate enum name" }
          // $MyObject$Param$Request, this allows for multiple custom objects in a single operation
          "${context.operationId.toPascalCase()}${context.content.toPascalCase()}${context.postfix.toPascalCase()}"
        }
      }

    public override fun OpenAPISyntax.toEnum(
      context: NamingContext,
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
      ).let { enum(context, schema, type, enum, it) }
    }

    public override fun toUnionName(context: NamingContext): String =
      when (context) {
        is NamingContext.Ref -> throw IllegalStateException("OneOf is never called for Param!")
        is NamingContext.Inline -> context.content.toPascalCase()
        is NamingContext.ClassName -> context.content.toPascalCase()
        is NamingContext.OperationParam -> context.content.toPascalCase()
      }

    // Support AnyOf = null | A, should become A?
    public override fun OpenAPISyntax.toAnyOf(
      context: NamingContext,
      schema: Schema,
      anyOf: List<ReferenceOr<Schema>>,
    ): KModel =
      anyOf(context, schema, anyOf, toUnion(context, schema, anyOf, KModel.Union::AnyOf))

    public override fun OpenAPISyntax.toOneOf(
      context: NamingContext,
      schema: Schema,
      oneOf: List<ReferenceOr<Schema>>,
    ): KModel =
      oneOf(context, schema, oneOf, toUnion(context, schema, oneOf, KModel.Union::OneOf))

    public override fun OpenAPISyntax.toUnionCaseContext(
      context: NamingContext,
      unionName: String,
      unionSchema: Schema,
      caseSchema: ReferenceOr<Schema>,
      caseIndex: Int
    ): NamingContext = when (caseSchema) {
      // TODO, can we register ReferenceOr.Reference to implement unionName directly?
      is ReferenceOr.Reference -> NamingContext.ClassName(caseSchema.ref.drop(schemaRef.length))
      is ReferenceOr.Value -> when (context) {
        is NamingContext.Inline ->
          if (caseSchema.value.type != null && caseSchema.value.type !is Type.Array) context
          else throw IllegalStateException("Cannot generate OneOf name for $this.")

        else -> NamingContext.ClassName(unionName)
      }
    }

    private fun <A : KModel> OpenAPISyntax.toUnion(
      context: NamingContext,
      schema: Schema,
      subtypes: List<ReferenceOr<Schema>>,
      transform: (String, List<KModel>) -> A
    ): A {
      val name = toUnionName(context)
      return transform(name, subtypes.mapIndexed { index, ref ->
        ref.get().toModel(toUnionCaseContext(context, name, schema, ref, index))
      })
    }

    /** Null if [RequestBody.content]'s [MediaType.content] is null */
    public fun OpenAPISyntax.toRequestBodyOrNull(
      operation: Operation,
      body: RequestBody
    ): KRoute.Body? = toRequestBody(operation, body) { null }

    public override fun OpenAPISyntax.toRequestBody(
      operation: Operation,
      body: RequestBody
    ): KRoute.Body = toRequestBody(operation, body) {
      val (name, schema) = it.namedSchema()
      KRoute.Body.Json(schema.toModel(NamingContext.ClassName(name)))
    }

    private fun <A : KRoute.Body?> OpenAPISyntax.toRequestBody(
      operation: Operation,
      body: RequestBody,
      onReference: (ref: ReferenceOr.Reference) -> A
    ): A {
      require(body.content.entries.size == 1) {
        "Only a single content type is supported. Found ${body.content.keys}. Please open a feature request."
      }
      val jsonContent = body.content[applicationJson]
      val multipartContent = body.content[multipartData]

      return when {
        jsonContent?.schema != null -> {
          when (val s = jsonContent.schema!!) {
            is ReferenceOr.Reference -> onReference(s)
            is ReferenceOr.Value ->
              KRoute.Body.Json(
                s.value.toModel(
                  requireNotNull(
                    operation.operationId?.let { NamingContext.ClassName("${it.toPascalCase()}Request") }
                  ) { "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name" }
                )
              )
          }
        }

        jsonContent != null -> KRoute.Body.Json(KModel.JsonObject)

        multipartContent?.schema != null -> {
          fun ctx(name: String): NamingContext = when (val s = multipartContent.schema) {
            is ReferenceOr.Reference -> NamingContext.ClassName(s.namedSchema().first)
            is ReferenceOr.Value -> NamingContext.OperationParam(name, operation.operationId, "Request")
            null -> throw IllegalStateException("$multipartContent without a schema. Generation doesn't know what to do, please open a ticket!")
          }

          val props = requireNotNull(multipartContent.schema!!.get().properties) {
            "Generating multipart/form-data bodies without properties is not possible."
          }
          require(props.isNotEmpty()) { "Generating multipart/form-data bodies without properties is not possible." }
          KRoute.Body.Multipart(props.map { (name, ref) ->
            KRoute.Body.Multipart.FormData(
              name,
              ref.get().toModel(ctx(name))
            )
          })
        }

        else -> throw IllegalStateException("RequestBody content type: $this not yet supported.")
      } as A // Kotlin not smart enough
    }

    /** Null if [Responses.content]'s [MediaType.content] is null */
    public override fun OpenAPISyntax.toInlineResponsesOrNull(operation: Operation): KModel? =
      toResponses(operation) { null }

    public override fun OpenAPISyntax.toResponses(operation: Operation): KModel? =
      toResponses(operation) { s ->
        val (name, schema) = s.namedSchema()
        schema.toModel(NamingContext.ClassName(name))
      }

    private fun OpenAPISyntax.toResponses(
      operation: Operation,
      onReference: (ref: ReferenceOr.Reference) -> KModel?
    ): KModel? =
      when (operation.responses.responses.size) {
        1 -> {
          val (code, refOrResponse) = operation.responses.responses.entries.first()
          val response = refOrResponse.get()
          when {
            response.content.contains("application/octet-stream") -> KModel.OctetStream
            response.content.contains(applicationJson) -> {
              val mediaType = response.content.getValue(applicationJson)
              when (val s = mediaType.schema) {
                is ReferenceOr.Reference -> onReference(s)
                is ReferenceOr.Value -> s.value.toModel(
                  requireNotNull(operation.operationId?.let { NamingContext.ClassName("${it.toPascalCase()}Response") }) {
                    "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                  })

                null -> toRawJson(Allowed(true))
              }
            }

            response.isEmpty() -> Primitive.Unit
            else -> responseNotSupported(operation, response, code)
          }
        }
        // TODO best effort for now, look for JSON which we support and generate that.
        else -> throw IllegalStateException("We don't support multiple formats yet: ${operation.responses}")
      }?.let { response(operation, it) }
  }
}