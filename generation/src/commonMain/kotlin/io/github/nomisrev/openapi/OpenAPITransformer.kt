package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.Model.Enum
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.Schema.Type
import io.github.nomisrev.openapi.http.MediaType.Companion.ApplicationJson
import io.github.nomisrev.openapi.http.MediaType.Companion.ApplicationOctetStream
import io.github.nomisrev.openapi.http.MediaType.Companion.ApplicationXml
import io.github.nomisrev.openapi.http.MediaType.Companion.MultipartFormData
import io.github.nomisrev.openapi.http.Method
import io.github.nomisrev.openapi.http.StatusCode

suspend fun OpenAPI.routes(sorter: ApiSorter = ApiSorter.ByPath): Root =
  OpenAPITransformer(this).routes().let { sorter.sort(it) }

fun OpenAPI.models(): Set<Model> =
  with(OpenAPITransformer(this)) { schemas() }
    .mapNotNull { model ->
      when (model) {
        is Collection -> model.value
        is Primitive.Int,
        is Primitive.Double,
        is Primitive.Boolean,
        is Primitive.String,
        is Primitive.Unit -> null
        else -> model
      }
    }
    .toSet()

/**
 * This class implements the traverser, it goes through the [OpenAPI] file, and gathers all the
 * information.
 *
 * It does the heavy lifting of figuring out what a `Schema` is, a `String`, `enum=[alive, dead]`,
 * object, etc.
 */
private class OpenAPITransformer(private val openAPI: OpenAPI) {

  fun operations(): List<Triple<String, Method, Operation>> =
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

  fun routes(): List<Route> =
    operations().map { (path, method, operation) ->
      Route(
        operation = operation,
        path = path,
        method = method,
        body = toRequestBody(operation, operation.requestBody?.get()),
        input = operation.input(),
        returnType = toResponses(operation),
        extensions = operation.extensions
      )
    }

  fun Operation.input(): List<Route.Input> =
    parameters.map { p ->
      val param = p.get()
      val resolved =
        param.schema?.resolve()
          ?: throw IllegalStateException("No Schema for Parameter. Fallback to JsonObject?")
      val context =
        resolved.namedOr { NamingContext.RouteParam(param.name, operationId, "Request") }
      val model = resolved.toModel(context)

      Route.Input(
        param.name,
        model, // TODO the nullable param should be configurable
        param.schema!!.get().nullable ?: true,
        param.required,
        param.input
      )
    }

  private fun Route.Body.model(): List<Model> =
    when (this) {
      is Route.Body.Json -> listOf(type)
      is Route.Body.Multipart -> parameters.map { it.type }
      is Route.Body.OctetStream -> listOf(Model.Binary)
      is Route.Body.Xml -> TODO("We don't support XML yet")
    }

  /**
   * Gets all the **inline** schemas for operations, and gathers them in the nesting that they occur
   * within the document. So they can be generated whilst maintaining their order of nesting.
   */
  //  fun operationModels(): List<Model> =
  //    operations().flatMap { (_, _, operation) ->
  //      // TODO this can probably be removed in favor of `Set` to remove duplication
  //      // Gather the **inline** request bodies & returnType, ignore top-level
  //      val bodies =
  //        operation.requestBody
  //          ?.get()
  //          ?.let { toRequestBody(operation, it).types.values }
  //          ?.flatMap { it.model() }
  //          .orEmpty()
  //
  //      val responses = toResponses(operation).map { it.value.type }
  //
  //      val parameters =
  //        operation.parameters.mapNotNull { refOrParam ->
  //          refOrParam.valueOrNull()?.let { param ->
  //            param.schema
  //              ?.valueOrNull()
  //              ?.toModel(NamingContext.RouteParam(param.name, operation.operationId, "Request"))
  //          }
  //        }
  //      bodies + responses + parameters
  //    }

  /** Gathers all "top-level", or components schemas. */
  fun schemas(): List<Model> =
    openAPI.components.schemas.map { (name, refOrSchema) ->
      when (val resolved = refOrSchema.resolve()) {
        is Resolved.Ref -> throw IllegalStateException("Remote schemas not supported yet.")
        is Resolved.Value -> resolved.toModel(Named(name))
      }
    }

  fun Resolved<Schema>.toModel(context: NamingContext): Model =
    with(value) {
      when {
        isOpenEnumeration() ->
          toOpenEnum(context, anyOf!!.firstNotNullOf { it.resolve().value.enum })
        // Flatten anyOf, and oneOf, when they have a single schema
        anyOf != null && anyOf?.size == 1 -> anyOf!![0].resolve().toModel(context)
        oneOf != null && oneOf?.size == 1 -> oneOf!![0].resolve().toModel(context)
        anyOf != null -> toUnion(context, anyOf!!)
        oneOf != null -> toUnion(context, oneOf!!)
        allOf != null -> TODO("allOf")
        enum != null -> toEnum(context, enum.orEmpty())
        properties != null -> asObject(context)
        // If no values, properties, or schemas, were found, lets check the types
        type != null -> type(context, type!!)
        else -> TODO("Schema: $this not yet supported. Please report to issue tracker.")
      }
    }

  fun Schema.isOpenEnumeration(): Boolean =
    anyOf != null &&
      anyOf!!.size == 2 &&
      anyOf!!.count { it.get().enum != null } == 1 &&
      anyOf!!.count { it.get().type == Type.Basic.String } == 1

  fun ReferenceOr<Schema>.resolve(): Resolved<Schema> =
    when (this) {
      is ReferenceOr.Value -> Resolved.Value(value)
      is ReferenceOr.Reference -> {
        val name = ref.drop(schemaRef.length)
        val schema =
          requireNotNull(openAPI.components.schemas[name]) {
              "Schema $name could not be found in ${openAPI.components.schemas}. Is it missing?"
            }
            .valueOrNull()
            ?: throw IllegalStateException("Remote schemas are not yet supported.")
        Resolved.Ref(name, schema)
      }
    }

  fun ReferenceOr<Schema>.get(): Schema =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> namedSchema().second
    }

  fun ReferenceOr.Reference.namedSchema(): Pair<String, Schema> =
    (resolve() as Resolved.Ref<Schema>).let { (name, schema) -> Pair(name, schema) }

  tailrec fun ReferenceOr<Response>.get(): Response =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop("#/components/responses/".length)
        requireNotNull(openAPI.components.responses[typeName]) {
            "Response $typeName could not be found in ${openAPI.components.responses}. Is it missing?"
          }
          .get()
      }
    }

  tailrec fun ReferenceOr<Parameter>.get(): Parameter =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop("#/components/parameters/".length)
        requireNotNull(openAPI.components.parameters[typeName]) {
            "Parameter $typeName could not be found in ${openAPI.components.parameters}. Is it missing?"
          }
          .get()
      }
    }

  tailrec fun ReferenceOr<RequestBody>.get(): RequestBody =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop("#/components/requestBodies/".length)
        requireNotNull(openAPI.components.requestBodies[typeName]) {
            "RequestBody $typeName could not be found in ${openAPI.components.requestBodies}. Is it missing?"
          }
          .get()
      }
    }

  tailrec fun ReferenceOr<PathItem>.get(): PathItem =
    when (this) {
      is ReferenceOr.Value -> value
      is ReferenceOr.Reference -> {
        val typeName = ref.drop("#/components/pathItems/".length)
        requireNotNull(openAPI.components.pathItems[typeName]) {
            "PathItem $typeName could not be found in ${openAPI.components.pathItems}. Is it missing?"
          }
          .get()
      }
    }

  private fun Resolved<Schema>.asObject(context: NamingContext): Model =
    when {
      value.properties != null -> toObject(context, value.properties!!)
      value.additionalProperties != null ->
        when (val aProps = value.additionalProperties!!) {
          is AdditionalProperties.PSchema -> toMap(context, aProps)
          is Allowed -> toRawJson(aProps)
        }
      else -> toRawJson(Allowed(true))
    }

  fun Resolved<Schema>.toObject(
    context: NamingContext,
    properties: Map<String, ReferenceOr<Schema>>
  ): Model =
    Model.Object(
      this,
      context,
      value.description,
      properties.map { (name, ref) ->
        val pContext = toPropertyContext(name, ref, context)
        val pSchema = ref.resolve()
        val model = pSchema.toModel(pContext)
        Property(
          pSchema,
          name,
          pContext.name,
          model,
          value.required?.contains(name) == true,
          pSchema.value.nullable ?: value.required?.contains(name)?.not() ?: true,
          pSchema.value.description
        )
      }
    )

  fun toPropertyContext(
    key: String,
    propSchema: ReferenceOr<Schema>,
    original: NamingContext
  ): NamingContext =
    when (propSchema) {
      is ReferenceOr.Reference -> Named(propSchema.namedSchema().first)
      is ReferenceOr.Value -> NamingContext.Nested(key, original)
    }

  private fun Schema.singleDefaultOrNull(): String? = (default as? ExampleValue.Single)?.value

  fun Resolved<Schema>.toOpenEnum(context: NamingContext, values: List<String>): Enum.Open {
    require(values.isNotEmpty()) { "OpenEnum requires at least 1 possible value" }
    val default = value.singleDefaultOrNull()
    return Enum.Open(this, context, values, default)
  }

  fun Resolved<Schema>.toMap(
    context: NamingContext,
    additionalSchema: AdditionalProperties.PSchema
  ): Model = Collection.Map(this, additionalSchema.value.resolve().toModel(context))

  fun toRawJson(allowed: Allowed): Model =
    if (allowed.value) Model.FreeFormJson
    else
      throw IllegalStateException(
        "Illegal State: No additional properties allowed on empty object."
      )

  fun Resolved<Schema>.toPrimitive(context: NamingContext, basic: Type.Basic): Model =
    with(value) {
      when (basic) {
        Type.Basic.Object -> toRawJson(Allowed(true))
        Type.Basic.Boolean -> Primitive.Boolean(this, default?.toString()?.toBoolean())
        Type.Basic.Integer -> Primitive.Int(this, default?.toString()?.toIntOrNull())
        Type.Basic.Number -> Primitive.Double(this, default?.toString()?.toDoubleOrNull())
        Type.Basic.Array -> collection(context)
        Type.Basic.String ->
          if (format == "binary") Model.Binary else Primitive.String(this, default?.toString())
        Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
      }
    }

  private fun Resolved<Schema>.collection(context: NamingContext): Collection {
    val items =
      requireNotNull(value.items?.resolve()) { "Array type requires items to be defined." }
    val inner = items.toModel(items.namedOr { context })
    val default =
      when (val example = value.default) {
        is ExampleValue.Multiple -> example.values
        is ExampleValue.Single ->
          when (val value = example.value) {
            "[]" -> emptyList()
            else -> listOf(value)
          }
        null -> null
      }
    return if (value.uniqueItems == true) Collection.Set(this, inner, default)
    else Collection.List(this, inner, default)
  }

  fun Resolved<Schema>.type(context: NamingContext, type: Type): Model =
    when (type) {
      is Type.Array ->
        when {
          type.types.size == 1 ->
            Resolved.Value(value.copy(type = type.types.single()))
              .type(context, type.types.single())
          else ->
            Model.Union(
              this,
              context,
              type.types.sorted().map {
                val resolved = Resolved.Value(Schema(type = it))
                Model.Union.Entry(resolved, context, resolved.toModel(context))
              },
              null
            )
        }
      is Type.Basic -> toPrimitive(context, type)
    }

  fun Resolved<Schema>.toEnum(context: NamingContext, enums: List<String>): Model.Enum.Closed {
    require(enums.isNotEmpty()) { "Enum requires at least 1 possible value" }
    /* To resolve the inner type, we erase the enum values.
     * Since the schema is still on the same level - we keep the topLevelName */
    val inner = Resolved.Value(value.copy(enum = null)).toModel(context)
    val default = value.singleDefaultOrNull()
    return Enum.Closed(this, context, inner, enums, default)
  }

  fun toUnionCaseContext(context: NamingContext, caseSchema: ReferenceOr<Schema>): NamingContext =
    when (caseSchema) {
      is ReferenceOr.Reference -> Named(caseSchema.ref.drop(schemaRef.length))
      is ReferenceOr.Value ->
        when (context) {
          is NamingContext.Nested ->
            when {
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
          is Named -> generateName(context, caseSchema.value)
          is NamingContext.RouteParam -> context
        }
    }

  /**
   * TODO This needs a rock solid implementation, and should be super easy to override from Gradle.
   * This is what we use to generate names for inline schemas, most of the time we can get away with
   * other information, but not always.
   *
   * This typically occurs when a top-level oneOf, or anyOf has inline schemas. Since union cases
   * don't have names, we need to generate a name for an inline schema. Generically we cannot do
   * better than First, Second, etc.
   */
  private fun generateName(context: Named, schema: Schema): NamingContext =
    when (schema.type) {
      Type.Basic.Array -> {
        val inner = requireNotNull(schema.items) { "Array type requires items to be defined." }
        inner.get().type
        TODO("Name generation for Type Arrays not yet supported")
      }
      Type.Basic.Object -> {
        // OpenAI specific:
        //   When there is an `event` property,
        //   rely on the single enum event name as generated name.
        NamingContext.Nested(
          schema.properties
            ?.firstNotNullOfOrNull { (key, value) ->
              if (key == "event") value.get().enum else null
            }
            ?.singleOrNull()
            ?: TODO("Name Generated for inline objects of unions not yet supported."),
          context
        )
      }
      is Type.Array -> TODO()
      Type.Basic.Number -> TODO()
      Type.Basic.Boolean -> TODO()
      Type.Basic.Integer -> TODO()
      Type.Basic.Null -> TODO()
      Type.Basic.String ->
        when (val enum = schema.enum) {
          null -> context.copy(name = "CaseString")
          else ->
            NamingContext.Nested(
              name =
                enum.joinToString(prefix = "", separator = "Or") {
                  it.replaceFirstChar(Char::uppercaseChar)
                },
              context
            )
        }
      null -> TODO()
    }

  private fun Resolved<Schema>.toUnion(
    context: NamingContext,
    subtypes: List<ReferenceOr<Schema>>
  ): Model.Union {
    val cases =
      subtypes.map { ref ->
        val caseContext = toUnionCaseContext(context, ref)
        val resolved = ref.resolve()
        Model.Union.Entry(resolved, caseContext, resolved.toModel(caseContext))
      }
    return Model.Union(
      this,
      context,
      cases,
      // TODO We need to check the parent for default?
      cases.firstNotNullOfOrNull { (it.model as? Primitive.String)?.schema?.singleDefaultOrNull() }
    )
  }

  // TODO interceptor
  fun toRequestBody(operation: Operation, body: RequestBody?): Route.Bodies =
    Route.Bodies(
      body?.required ?: false,
      body
        ?.content
        ?.entries
        ?.associate { (contentType, mediaType) ->
          when {
            ApplicationXml.matches(contentType) -> TODO("Add support for XML.")
            ApplicationJson.matches(contentType) -> {
              val json =
                mediaType.schema?.resolve()?.let { json ->
                  val context =
                    json.namedOr {
                      requireNotNull(operation.operationId?.let { Named("${it}Request") }) {
                        "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                      }
                    }
                  Route.Body.Json(json, json.toModel(context), mediaType.extensions)
                }
                  ?: Route.Body.Json(null, Model.FreeFormJson, mediaType.extensions)
              Pair(ApplicationJson, json)
            }
            MultipartFormData.matches(contentType) -> {
              val resolved =
                mediaType.schema?.resolve()
                  ?: throw IllegalStateException(
                    "$mediaType without a schema. Generation doesn't know what to do, please open a ticket!"
                  )

              fun ctx(name: String): NamingContext =
                resolved.namedOr {
                  NamingContext.RouteParam(name, operation.operationId, "Request")
                }

              val (model, formData) =
                when (resolved) {
                  is Resolved.Ref -> {
                    val model = resolved.toModel(Named(resolved.name))
                    Pair(
                      model,
                      listOf(Route.Body.Multipart.FormData(resolved, resolved.name, model))
                    )
                  }
                  is Resolved.Value ->
                    Pair(
                      null,
                      resolved.value.properties!!.map { (name, ref) ->
                        val resolved = ref.resolve()
                        Route.Body.Multipart.FormData(resolved, name, resolved.toModel(ctx(name)))
                      }
                    )
                }

              Pair(MultipartFormData, Route.Body.Multipart(model, formData, mediaType.extensions))
            }
            ApplicationOctetStream.matches(contentType) ->
              Pair(ApplicationOctetStream, Route.Body.OctetStream(mediaType.extensions))
            else ->
              throw IllegalStateException("RequestBody content type: $this not yet supported.")
          }
        }
        .orEmpty(),
      body?.extensions.orEmpty()
    )

  private fun Response.isEmpty(): Boolean =
    headers.isEmpty() && content.isEmpty() && links.isEmpty() && extensions.isEmpty()

  // TODO interceptor
  fun toResponses(operation: Operation): Route.Returns =
    Route.Returns(
      operation.responses.responses.entries.associate { (code, refOrResponse) ->
        val statusCode = StatusCode.orThrow(code)
        val response = refOrResponse.get()
        when {
          response.content.contains("application/octet-stream") ->
            Pair(statusCode, Route.ReturnType(Model.Binary, response.extensions))
          response.content.contains("application/json") -> {
            val mediaType = response.content.getValue("application/json")
            when (val resolved = mediaType.schema?.resolve()) {
              is Resolved.Ref ->
                Pair(
                  statusCode,
                  Route.ReturnType(resolved.toModel(Named(resolved.name)), response.extensions)
                )
              is Resolved.Value ->
                Pair(
                  statusCode,
                  Route.ReturnType(
                    resolved.toModel(
                      requireNotNull(operation.operationId?.let { Named("${it}Response") }) {
                        "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                      }
                    ),
                    response.extensions
                  )
                )
              null ->
                Pair(statusCode, Route.ReturnType(toRawJson(Allowed(true)), response.extensions))
            }
          }
          response.isEmpty() ->
            Pair(
              statusCode,
              Route.ReturnType(
                Primitive.String(Schema(type = Schema.Type.Basic.String), null),
                response.extensions
              )
            )
          else ->
            throw IllegalStateException("OpenAPI requires at least 1 valid response. $response")
        }
      },
      operation.responses.extensions
    )
}

internal const val schemaRef = "#/components/schemas/"
