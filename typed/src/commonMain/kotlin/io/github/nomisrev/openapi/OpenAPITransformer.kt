package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model.Collection
import io.github.nomisrev.openapi.Model.Enum
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.Schema.Type
import io.ktor.http.*
import kotlin.jvm.JvmInline

fun OpenAPI.routes(): List<Route> = OpenAPITransformer(this).routes()

fun OpenAPI.models(): Set<Model> =
  with(OpenAPITransformer(this)) { schemas() }
    .mapNotNull { model ->
      when (model) {
        is Collection -> model.inner
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

  fun operations(): List<Triple<String, HttpMethod, Operation>> =
    openAPI.paths.entries.flatMap { (path, p) ->
      listOfNotNull(
        p.get?.let { Triple(path, HttpMethod.Get, it) },
        p.put?.let { Triple(path, HttpMethod.Put, it) },
        p.post?.let { Triple(path, HttpMethod.Post, it) },
        p.delete?.let { Triple(path, HttpMethod.Delete, it) },
        p.head?.let { Triple(path, HttpMethod.Head, it) },
        p.options?.let { Triple(path, HttpMethod.Options, it) },
        p.trace?.let { Triple(path, HttpMethod.parse("Trace"), it) },
        p.patch?.let { Triple(path, HttpMethod.Patch, it) },
      )
    }

  fun routes(): List<Route> =
    operations().map { (path, method, operation) ->
      val parts = path.replace(Regex("\\{.*?\\}"), "").split("/").filter { it.isNotEmpty() }

      fun context(context: NamingContext): NamingContext =
        if (context is Named) context
        else
          when (parts.size) {
            0 -> context
            1 -> NamingContext.Nested(context, Named(parts[0]))
            else ->
              NamingContext.Nested(
                context,
                parts.drop(1).fold<String, NamingContext>(Named(parts[0])) { acc, part ->
                  NamingContext.Nested(Named(part), acc)
                }
              )
          }

      val inputs = operation.input(::context)
      val nestedInput = inputs.mapNotNull { (it as? Resolved.Value)?.value }
      val nestedResponses = operation
        .responses
        .responses
        .mapNotNull { (_, refOrResponse) ->
          when (refOrResponse) {
            is ReferenceOr.Reference -> null
            is ReferenceOr.Value -> {
              val resolved = refOrResponse.value.content.getOrElse("application/json") { null }
                ?.schema
                ?.resolve() ?: return@mapNotNull null
              val context =
                resolved
                  .namedOr {
                    val operationId =
                      requireNotNull(operation.operationId) {
                        "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                      }
                    NamingContext.RouteBody(operationId, "Response")
                  }.let(::context)
              (resolved.toModel(context) as? Resolved.Value)?.value
            }
          }
        }

      val json = operation
        .requestBody
        ?.valueOrNull()
        ?.content
        ?.getOrElse("application/json") { null }
        ?.schema
        ?.resolve()

      val nestedBody = json?.namedOr {
        requireNotNull(operation.operationId?.let { Named("${it}Request") }) {
          "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
        }
      }
        ?.let(::context)
        ?.let { (json.toModel(it) as? Resolved.Value)?.value }
        .let(::listOfNotNull)

      Route(
        operation = operation,
        path = path,
        method = method,
        body = toRequestBody(operation, operation.requestBody?.get(), ::context),
        input = inputs.zip(operation.parameters) { model, p ->
          val param = p.get()
          Route.Input(param.name, model.value, param.required, param.input, param.description)
        },
        returnType = toResponses(operation, ::context),
        extensions = operation.extensions,
        nested = nestedInput + nestedResponses + nestedBody
      )
    }

  fun Operation.input(create: (NamingContext) -> NamingContext): List<Resolved<Model>> =
    parameters.map { p ->
      val param = p.get()
      val resolved =
        param.schema?.resolve() ?: throw IllegalStateException("No Schema for Parameter.")
      val context =
        resolved
          .namedOr {
            val operationId =
              requireNotNull(operationId) {
                "operationId currently required to generate inline schemas for operation parameters."
              }
            NamingContext.RouteParam(param.name, operationId, "Request")
          }
          .let(create)
      resolved.toModel(context)
    }

  /** Gathers all "top-level", or components schemas. */
  fun schemas(): List<Model> =
    openAPI.components.schemas.map { (name, refOrSchema) ->
      when (val resolved = refOrSchema.resolve()) {
        is Resolved.Ref -> throw IllegalStateException("Remote schemas not supported yet.")
        is Resolved.Value -> Resolved.Ref(name, resolved.value).toModel(Named(name)).value
      }
    }

  fun Resolved<Schema>.toModel(context: NamingContext): Resolved<Model> {
    val schema: Schema = value
    val model =
      when {
        schema.isOpenEnumeration() ->
          schema.toOpenEnum(context, schema.anyOf!!.firstNotNullOf { it.resolve().value.enum })

        /*
         * We're modifying the schema here... Should we...?
         * This is to flatten the following to just `String`
         * "model": {
         *   "description": "ID of the model to use. You can use the [List models](/docs/api-reference/models/list) API to see all of your available models, or see our [Model overview](/docs/models/overview) for descriptions of them.\n",
         *   "anyOf": [
         *     {
         *       "type": "string"
         *     }
         *   ]
         * }
         */
        this is Resolved.Value && value.anyOf != null && value.anyOf?.size == 1 ->
          value.anyOf!![0].resolve().toModel(context).value

        this is Resolved.Value && value.oneOf != null && value.oneOf?.size == 1 ->
          value.oneOf!![0].resolve().toModel(context).value

        schema.anyOf != null -> schema.toUnion(context, schema.anyOf!!)
        schema.oneOf != null -> schema.toUnion(context, schema.oneOf!!)
        schema.allOf != null -> TODO("allOf")
        schema.enum != null -> schema.toEnum(context, schema.enum.orEmpty())
        schema.properties != null -> schema.asObject(context)
        // If no values, properties, or schemas, were found, lets check the types
        schema.type != null -> schema.type(context, schema.type!!)
        else -> TODO("Schema: $schema not yet supported. Please report to issue tracker.")
      }
    return when (this) {
      is Resolved.Ref -> Resolved.Ref(name, model)
      is Resolved.Value -> Resolved.Value(model)
    }
  }

  fun Schema.isOpenEnumeration(): Boolean =
    anyOf != null &&
      anyOf!!.size == 2 &&
      anyOf!!.count { it.resolve().value.enum != null } == 1 &&
      anyOf!!.count { it.resolve().value.type == Type.Basic.String } == 2

  fun <A> ReferenceOr<Schema>.resolve(onValue: (Schema) -> A, onRef: (String, Schema) -> A): A =
    when (this) {
      is ReferenceOr.Value -> onValue(value)
      is ReferenceOr.Reference -> {
        val name = ref.drop(schemaRef.length)
        val schema =
          requireNotNull(openAPI.components.schemas[name]) {
            "Schema $name could not be found in ${openAPI.components.schemas}. Is it missing?"
          }
            .valueOrNull()
            ?: throw IllegalStateException("Remote schemas are not yet supported.")
        onRef(name, schema)
      }
    }

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

  private fun Schema.asObject(context: NamingContext): Model =
    when {
      properties != null -> toObject(context, properties!!)
      additionalProperties != null ->
        when (val aProps = additionalProperties!!) {
          is AdditionalProperties.PSchema ->
            Collection.Map(aProps.value.resolve().toModel(context).value, description)

          is Allowed ->
            if (aProps.value) Model.FreeFormJson(description)
            else
              throw IllegalStateException(
                "Illegal State: No additional properties allowed on empty object."
              )
        }

      else -> Model.FreeFormJson(description)
    }

  fun Schema.toObject(context: NamingContext, properties: Map<String, ReferenceOr<Schema>>): Model =
    Model.Object(
      context,
      description,
      properties.map { (name, ref) ->
        val resolved = ref.resolve()
        val pContext =
          when (resolved) {
            is Resolved.Ref -> Named(resolved.name)
            is Resolved.Value -> NamingContext.Nested(Named(name), context)
          }
        val model = resolved.toModel(pContext)
        Property(
          name,
          model.value,
          required?.contains(name) == true,
          resolved.value.nullable ?: required?.contains(name)?.not() ?: true,
          resolved.value.description
        )
      },
      properties.mapNotNull { (name, ref) ->
        val resolved = ref.resolve()
        val pContext =
          when (resolved) {
            is Resolved.Ref -> Named(resolved.name)
            is Resolved.Value -> NamingContext.Nested(Named(name), context)
          }
        nestedModel(resolved, pContext)
      }
    )

  private fun Schema.singleDefaultOrNull(): String? = (default as? ExampleValue.Single)?.value

  fun Schema.toOpenEnum(context: NamingContext, values: List<String>): Enum.Open {
    require(values.isNotEmpty()) { "OpenEnum requires at least 1 possible value" }
    val default = singleDefaultOrNull()
    return Enum.Open(context, values, default, description)
  }

  fun Schema.toPrimitive(context: NamingContext, basic: Type.Basic): Model =
    when (basic) {
      Type.Basic.Object ->
        if (Allowed(true).value) Model.FreeFormJson(description)
        else
          throw IllegalStateException(
            "Illegal State: No additional properties allowed on empty object."
          )

      Type.Basic.Boolean -> Primitive.Boolean(default?.toString()?.toBoolean(), description)
      Type.Basic.Integer -> Primitive.Int(default?.toString()?.toIntOrNull(), description)
      Type.Basic.Number -> Primitive.Double(default?.toString()?.toDoubleOrNull(), description)
      Type.Basic.Array -> collection(context)
      Type.Basic.String ->
        if (format == "binary") Model.OctetStream(description)
        else Primitive.String(default?.toString(), description)

      Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
    }

  private fun Schema.collection(context: NamingContext): Collection {
    val items = requireNotNull(items?.resolve()) { "Array type requires items to be defined." }
    val inner = items.toModel(items.namedOr { context })
    val default =
      when (val example = default) {
        is ExampleValue.Multiple -> example.values
        is ExampleValue.Single -> {
          val value = example.value
          when {
            value == "[]" -> emptyList()
            value.equals("null", ignoreCase = true) -> emptyList()
            else -> listOf(value)
          }
        }

        null -> null
      }
    return if (uniqueItems == true) Collection.Set(inner.value, default, description)
    else Collection.List(inner.value, default, description)
  }

  fun Schema.type(context: NamingContext, type: Type): Model =
    when (type) {
      is Type.Array ->
        when {
          type.types.size == 1 ->
            copy(type = type.types.single()).type(context, type.types.single())

          else ->
            Model.Union(
              context,
              type.types.sorted().map { t ->
                val resolved = Resolved.Value(Schema(type = t))
                Model.Union.Case(context, resolved.toModel(context).value)
              },
              null,
              description,
              emptyList()
            )
        }

      is Type.Basic -> toPrimitive(context, type)
    }

  fun Schema.toEnum(context: NamingContext, enums: List<String>): Enum.Closed {
    require(enums.isNotEmpty()) { "Enum requires at least 1 possible value" }
    /* To resolve the inner type, we erase the enum values.
     * Since the schema is still on the same level - we keep the topLevelName */
    val inner = Resolved.Value(copy(enum = null)).toModel(context)
    val default = singleDefaultOrNull()
    return Enum.Closed(context, inner.value, enums, default, description)
  }

  fun toUnionCaseContext(context: NamingContext, caseSchema: ReferenceOr<Schema>): NamingContext =
    when (caseSchema) {
      is ReferenceOr.Reference -> Named(caseSchema.ref.drop(schemaRef.length))
      is ReferenceOr.Value ->
        when (context) {
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
          is NamingContext.Nested,
          is NamingContext.RouteParam,
          is NamingContext.RouteBody -> context
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
        inner.resolve().value.type
        TODO("Name generation for Type Arrays not yet supported")
      }

      Type.Basic.Object -> {
        // OpenAI specific:
        //   When there is an `event` property,
        //   rely on the single enum event name as generated name.
        NamingContext.Nested(
          schema.properties
            ?.firstNotNullOfOrNull { (key, value) ->
              if (key == "event") value.resolve().value.enum else null
            }
            ?.singleOrNull()
            ?.let(::Named)
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
              inner =
              Named(
                enum.joinToString(prefix = "", separator = "Or") {
                  it.replaceFirstChar(Char::uppercaseChar)
                }
              ),
              context
            )
        }

      null -> TODO()
    }

  /**
   * This Comparator will sort union cases by their most complex schema first Such that if we have {
   * "text" : String } & { "text" : String, "id" : Int } That we don't accidentally result in the
   * first case, when we receive the second case. Primitive.String always comes last.
   */
  private val unionSchemaComparator: Comparator<Model.Union.Case> = Comparator { o1, o2 ->
    val m1 = o1.model
    val m2 = o2.model
    val m1Complexity =
      when (m1) {
        is Model.Object -> m1.properties.size
        is Enum -> m1.values.size
        is Primitive.String -> -1
        else -> 0
      }
    val m2Complexity =
      when (m2) {
        is Model.Object -> m2.properties.size
        is Enum -> m2.values.size
        is Primitive.String -> -1
        else -> 0
      }
    m2Complexity - m1Complexity
  }

  private fun Schema.toUnion(
    context: NamingContext,
    subtypes: List<ReferenceOr<Schema>>
  ): Model.Union {
    val cases =
      subtypes
        .map { ref ->
          val caseContext = toUnionCaseContext(context, ref)
          val resolved = ref.resolve()
          Model.Union.Case(caseContext, resolved.toModel(caseContext).value)
        }
        .sortedWith(unionSchemaComparator)
    return Model.Union(
      context,
      cases,
      singleDefaultOrNull()
        ?: subtypes.firstNotNullOfOrNull { it.resolve().value.singleDefaultOrNull() },
      description,
      subtypes.mapNotNull { ref ->
        val caseContext = toUnionCaseContext(context, ref)
        val resolved = ref.resolve()
        nestedModel(resolved, caseContext)
      }
    )
  }

  private fun nestedModel(resolved: Resolved<Schema>, caseContext: NamingContext) =
    when (val model = resolved.toModel(caseContext)) {
      is Resolved.Ref -> null
      is Resolved.Value ->
        when (model.value) {
          is Collection ->
            when (val inner = resolved.value.items?.resolve()) {
              is Resolved.Value -> inner.toModel(caseContext).value
              is Resolved.Ref -> null
              null -> throw RuntimeException("Impossible: List without inner type")
            }

          else -> model.value
        }
    }

  // TODO interceptor
  fun toRequestBody(
    operation: Operation,
    body: RequestBody?,
    create: (NamingContext) -> NamingContext
  ): Route.Bodies =
    Route.Bodies(
      body?.required ?: false,
      body
        ?.content
        ?.entries
        ?.associate { (contentType, mediaType) ->
          when {
            ContentType.Application.Xml.match(contentType) -> TODO("Add support for XML.")
            ContentType.Application.Json.match(contentType) -> {
              val json =
                mediaType.schema?.resolve()?.let { json ->
                  val context =
                    json
                      .namedOr {
                        requireNotNull(operation.operationId?.let { Named("${it}Request") }) {
                          "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                        }
                      }
                      .let(create)

                  Route.Body.Json.Defined(
                    json.toModel(context).value,
                    body.description,
                    mediaType.extensions
                  )
                }
                  ?: Route.Body.Json.FreeForm(body.description, mediaType.extensions)
              Pair(ContentType.Application.Json, json)
            }

            ContentType.MultiPart.FormData.match(contentType) -> {
              val resolved =
                mediaType.schema?.resolve()
                  ?: throw IllegalStateException(
                    "$mediaType without a schema. Generation doesn't know what to do, please open a ticket!"
                  )

              fun ctx(name: String): NamingContext =
                resolved
                  .namedOr {
                    val operationId =
                      requireNotNull(operation.operationId) {
                        "operationId currently required to generate inline schemas for operation parameters."
                      }
                    NamingContext.RouteParam(name, operationId, "Request")
                  }
                  .let(create)

              val multipart =
                when (resolved) {
                  is Resolved.Ref -> {
                    val model = resolved.toModel(Named(resolved.name)) as Resolved.Ref
                    Route.Body.Multipart.Ref(
                      model.name,
                      model.value,
                      body.description,
                      mediaType.extensions
                    )
                  }

                  is Resolved.Value ->
                    Route.Body.Multipart.Value(
                      resolved.value.properties!!.map { (name, ref) ->
                        Route.Body.Multipart.FormData(name, ref.resolve().toModel(ctx(name)).value)
                      },
                      body.description,
                      mediaType.extensions
                    )
                }

              Pair(ContentType.MultiPart.FormData, multipart)
            }

            ContentType.Application.OctetStream.match(contentType) ->
              Pair(
                ContentType.Application.OctetStream,
                Route.Body.OctetStream(body.description, mediaType.extensions)
              )

            else ->
              throw IllegalStateException("RequestBody content type: $this not yet supported.")
          }
        }
        .orEmpty(),
      body?.extensions.orEmpty()
    )

  private fun Response.isEmpty(): Boolean =
    headers.isEmpty() && content.isEmpty() && links.isEmpty() && extensions.isEmpty()

  fun toResponses(operation: Operation, create: (NamingContext) -> NamingContext): Route.Returns =
    Route.Returns(
      operation.responses.responses.entries.associate { (code, refOrResponse) ->
        val statusCode = HttpStatusCode.fromValue(code)
        val response = refOrResponse.get()
        when {
          response.content.contains("application/octet-stream") ->
            Pair(
              statusCode,
              Route.ReturnType(Model.OctetStream(response.description), response.extensions)
            )

          response.content.contains("application/json") -> {
            val mediaType = response.content.getValue("application/json")
            val route =
              when (val resolved = mediaType.schema?.resolve()) {
                is Resolved -> {
                  val context =
                    resolved
                      .namedOr {
                        val operationId =
                          requireNotNull(operation.operationId) {
                            "OperationId is required for request body inline schemas. Otherwise we cannot generate OperationIdRequest class name"
                          }
                        NamingContext.RouteBody(operationId, "Response")
                      }
                      .let(create)
                  Route.ReturnType(resolved.toModel(context).value, response.extensions)
                }

                null ->
                  Route.ReturnType(
                    if (Allowed(true).value) Model.FreeFormJson(response.description)
                    else
                      throw IllegalStateException(
                        "Illegal State: No additional properties allowed on empty object."
                      ),
                    response.extensions
                  )
              }
            Pair(statusCode, route)
          }

          response.isEmpty() ->
            Pair(
              statusCode,
              Route.ReturnType(Primitive.String(null, response.description), response.extensions)
            )

          else ->
            throw IllegalStateException("OpenAPI requires at least 1 valid response. $response")
        }
      },
      operation.responses.extensions
    )

  /**
   * Allows tracking whether data was referenced by name, or defined inline. This is important to be
   * able to maintain the structure of the specification.
   */
  // TODO this can be removed.
  //   Move 'nested' logic to OpenAPITransformer
  //   Inline `namedOr` logic where used
  //   Rely on `ReferenceOr<Schema>` everywhere within `OpenAPITransformer`?
  sealed interface Resolved<A> {
    val value: A

    data class Ref<A>(val name: String, override val value: A) : Resolved<A>

    @JvmInline
    value class Value<A>(override val value: A) : Resolved<A>

    fun namedOr(orElse: () -> NamingContext): NamingContext =
      when (this) {
        is Ref -> NamingContext.Named(name)
        is Value -> orElse()
      }
  }
}

internal const val schemaRef = "#/components/schemas/"
