package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.AdditionalProperties
import io.github.nomisrev.openapi.AdditionalProperties.Allowed
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

  public fun OpenAPISyntax.toRequestBody(operationId: String?, requestBody: RequestBody): List<KModel>
  public fun OpenAPISyntax.toResponses(operationId: String?, responses: Responses): KModel?

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
    operationId: String?,
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
        ref.getOrNull()?.toModel(NamingContext.Inline(paramName, context))
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

    public override fun OpenAPISyntax.toAnyOf(
      context: NamingContext,
      schema: Schema,
      anyOf: List<ReferenceOr<Schema>>,
    ): KModel =
      toUnion(context, schema, anyOf, KModel.Union::AnyOf)
        .let { anyOf(context, schema, anyOf, it) }

    public override fun OpenAPISyntax.toOneOf(
      context: NamingContext,
      schema: Schema,
      oneOf: List<ReferenceOr<Schema>>,
    ): KModel =
      toUnion(context, schema, oneOf, KModel.Union::OneOf)
        .let { oneOf(context, schema, oneOf, it) }

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

    public override fun OpenAPISyntax.toRequestBody(operationId: String?, requestBody: RequestBody): List<KModel> {
      require(requestBody.content.entries.size == 1) {
        "Only a single content type is supported. Found ${requestBody.content.keys}. Please open a feature request."
      }
      val jsonContent = requestBody.content[applicationJson]
      val multipartContent = requestBody.content[multipartData]
      return when {
        jsonContent != null ->
          listOfNotNull(jsonContent.schema?.getOrNull()?.toModel(
            requireNotNull(
              operationId?.let { NamingContext.ClassName("${operationId.toPascalCase()}Request") }
            ) { "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name" }
          ))

        multipartContent != null ->
          multipartContent.schema?.getOrNull()?.properties?.mapNotNull { (name, ref) ->
            ref.getOrNull()?.toModel(NamingContext.OperationParam(name, operationId, "Request"))
          }.orEmpty()

        else -> throw IllegalStateException("RequestBody content type: $this not yet supported.")
      }.let { requestBodies(operationId, requestBody, it) }
    }

    public override fun OpenAPISyntax.toResponses(operationId: String?, responses: Responses): KModel? =
      when (responses.responses.size) {
        1 -> {
          val response = responses.responses.values.first().getOrNull()
          when {
            response?.content?.contains("application/octet-stream") == true ->
              KModel.OctetStream

            response?.content?.contains(applicationJson) == true -> {
              val mediaType = response.content.getValue(applicationJson)
              mediaType.schema
                ?.getOrNull()
                ?.toModel(
                  requireNotNull(operationId?.let { NamingContext.ClassName("${it.toPascalCase()}Response") }) {
                    "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                  }) ?: toRawJson(Allowed(true))
            }

            response?.isEmpty() == true -> Primitive.Unit
            else -> null
          }
        }

        else -> throw IllegalStateException("We don't support multiple formats yet: $responses")
      }?.let { response(operationId, it) }
  }
}