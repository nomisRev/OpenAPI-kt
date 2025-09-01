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

fun OpenAPI.models(): Set<Model> = with(OpenAPITransformer(this)) { schemas() }.toSet()

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
      val parts = path.segments()

      fun context(context: NamingContext): NamingContext =
        when (parts.size) {
          0 -> context
          1 -> NamingContext.Nested(context, Named(parts[0]))
          else ->
            NamingContext.Nested(
              context,
              parts.drop(1).fold<String, NamingContext>(Named(parts[0])) { acc, part ->
                NamingContext.Nested(Named(part), acc)
              },
            )
        }

      val opName = operation.operationId ?: method.value.lowercase()
      val inputs = operation.input(::context, opName)
      val nestedInput = inputs.mapNotNull { (it as? Resolved.Value)?.value }
      val nestedResponses =
        operation.responses.responses.mapNotNull { (_, refOrResponse) ->
          when (refOrResponse) {
            is ReferenceOr.Reference -> null
            is ReferenceOr.Value -> {
              val resolved =
                refOrResponse.value.content
                  .getOrElse("application/json") { null }
                  ?.schema
                  ?.resolve() ?: return@mapNotNull null
              val context =
                resolved.namedOr { context(NamingContext.RouteBody(opName, "Response")) }
              (resolved.toModel(context) as? Resolved.Value)?.value
            }
          }
        }

      val json =
        operation.requestBody
          ?.valueOrNull()
          ?.content
          ?.getOrElse("application/json") { null }
          ?.schema
          ?.resolve()

      val nestedBody =
        json
          ?.namedOr {
            val name = Named("${opName}Request")
            context(name)
          }
          ?.let { (json.toModel(it) as? Resolved.Value)?.value }
          .let(::listOfNotNull)

      Route(
        operationId = operation.operationId,
        summary = operation.summary,
        path = path,
        method = method,
        body = toRequestBody(operation, operation.requestBody?.get(), ::context, opName),
        input =
          inputs.zip(operation.parameters) { model, p ->
            val param = p.get()
            Route.Input(param.name, model.value, param.required, param.input, param.description)
          },
        returnType = toResponses(operation, ::context, opName),
        extensions = operation.extensions,
        nested = nestedInput + nestedResponses + nestedBody,
      )
    }

  fun Operation.input(
    create: (NamingContext) -> NamingContext,
    opName: String,
  ): List<Resolved<Model>> =
    parameters.map { p ->
      val param = p.get()
      val resolved =
        param.schema?.resolve() ?: throw IllegalStateException("No Schema for Parameter.")
      val context =
        resolved.namedOr { create(NamingContext.RouteParam(param.name, opName, "Request")) }
      resolved.toModel(context)
    }

  /** Gathers all "top-level", or components schemas. */
  fun schemas(): List<Model> =
    openAPI.components.schemas.map { (name, refOrSchema) ->
      when (val resolved = refOrSchema.resolve()) {
        is Resolved.Ref -> throw IllegalStateException("Remote schemas not supported yet.")
        is Resolved.Value -> resolved.toModel(Named(name)).value
      }
    }

  fun Resolved<Schema>.toModel(context: NamingContext): Resolved<Model> =
    when (this) {
      is Resolved.Ref -> {
        val desc = value.description.get()
        Resolved.Ref(name, Model.Reference(Named(name), desc))
      }
      is Resolved.Value -> {
        val schema: Schema = value
        val model =
          when {
            schema.isOpenEnumeration() ->
              schema.toOpenEnum(context, schema.anyOf!!.firstNotNullOf { it.resolve().value.enum })

            /*
             * We're modifying the schema here...
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
            value.anyOf != null && value.anyOf?.size == 1 -> {
              val inner = value.anyOf!![0].resolve().toModel(context).value
              inner.withDescriptionIfNull(schema.description.get())
            }
            value.oneOf != null && value.oneOf?.size == 1 -> {
              val inner = value.oneOf!![0].resolve().toModel(context).value
              inner.withDescriptionIfNull(schema.description.get())
            }
            schema.anyOf != null -> schema.toUnion(context, schema.anyOf!!)
            // oneOf + properties => oneOf requirements: 'propA OR propB is required'.
            schema.oneOf != null && schema.properties != null -> schema.toObject(context)
            schema.oneOf != null -> schema.toUnion(context, schema.oneOf!!)
            schema.allOf != null -> allOf(schema, context)
            schema.enum != null -> schema.toEnum(context, schema.enum.orEmpty())
            else -> schema.type(context)
          }
        Resolved.Value(model)
      }
    }

  private tailrec fun Schema.type(context: NamingContext): Model =
    when (val type = type) {
      is Type.Array ->
        when (val single = type.types.singleOrNull()) {
          null -> {
            require(type.types.isNotEmpty()) { "Array type requires types to be defined. $this" }
            val resolved = type.types.sorted().map { t -> Resolved.Value(Schema(type = t)) }
            Model.Union(
              context = context,
              cases = resolved.map { Model.Union.Case(context, it.toModel(context).value) },
              default = null,
              description = description.get(),
              inline = resolved.mapNotNull { nestedModel(it, context) },
            )
          }
          else -> copy(type = single).type(context)
        }
      is Type.Basic ->
        when (type) {
          Type.Basic.Array -> collection(context)
          Type.Basic.Boolean ->
            Primitive.Boolean(default("Boolean", String::toBooleanStrictOrNull), description.get())
          Type.Basic.Integer ->
            Primitive.Int(
              default("Integer", String::toIntOrNull),
              description.get(),
              Constraints.Number(this),
            )
          Type.Basic.Number ->
            Primitive.Double(
              default("Number", String::toDoubleOrNull),
              description.get(),
              Constraints.Number(this),
            )
          Type.Basic.String ->
            if (format == "binary") Model.OctetStream(description.get())
            else
              Primitive.String(
                default("String", String::toString) { it.joinToString() },
                description.get(),
                Constraints.Text(this),
              )
          Type.Basic.Object -> toObject(context)
          Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
        }
      null ->
        when {
          // If no type is defined, but we find properties, or additionalProperties, we assume it's
          // an object.
          properties != null || additionalProperties != null -> toObject(context)
          // If 'items' is defined, we assume it's an array.
          items != null -> collection(context)
          else -> TODO("Schema: $this not yet supported. Please report to issue tracker.")
        }
    }

  private fun <A> Schema.default(
    label: String,
    onSingle: (String) -> A?,
    onMultiple: (List<String>) -> A?,
  ): A? =
    when (val default = default) {
      is ExampleValue.Single ->
        onSingle(default.value)
          ?: throw IllegalStateException("Default value ${default.value} is not a $label.")
      is ExampleValue.Multiple -> onMultiple(default.values)
      null -> null
    }

  private fun <A> Schema.default(label: String, onSingle: (String) -> A?): A? =
    default(label, onSingle) {
      throw IllegalStateException("Multiple default values not supported for $label.")
    }

  private fun Model.withDescriptionIfNull(desc: String?): Model {
    if (desc == null) return this
    return when (this) {
      is Primitive.Int -> if (this.description != null) this else this.copy(description = desc)
      is Primitive.Double -> if (this.description != null) this else this.copy(description = desc)
      is Primitive.Boolean -> if (this.description != null) this else this.copy(description = desc)
      is Primitive.String -> if (this.description != null) this else this.copy(description = desc)
      is Primitive.Unit -> if (this.description != null) this else this.copy(description = desc)
      is Model.OctetStream -> if (this.description != null) this else this.copy(description = desc)
      is Model.FreeFormJson -> if (this.description != null) this else this.copy(description = desc)
      is Collection.List -> if (this.description != null) this else this.copy(description = desc)
      is Collection.Set -> if (this.description != null) this else this.copy(description = desc)
      is Collection.Map -> if (this.description != null) this else this.copy(description = desc)
      is Model.Object -> if (this.description != null) this else this.copy(description = desc)
      is Model.Union -> if (this.description != null) this else this.copy(description = desc)
      is Enum.Closed -> if (this.description != null) this else this.copy(description = desc)
      is Enum.Open -> if (this.description != null) this else this.copy(description = desc)
      is Model.Reference -> if (this.description != null) this else this.copy(description = desc)
    }
  }

  fun Schema.isOpenEnumeration(): Boolean {
    val anyOf = anyOf ?: return false
    return anyOf.size == 2 &&
      anyOf.count { it.resolve().value.enum != null } == 1 &&
      anyOf.count { it.resolve().value.type == Type.Basic.String } == 2
  }

  fun ReferenceOr<Schema>.resolve(): Resolved<Schema> =
    when (this) {
      is ReferenceOr.Value -> Resolved.Value(value)
      is ReferenceOr.Reference -> {
        val name = ref.drop("#/components/schemas/".length)
        val schema =
          requireNotNull(openAPI.components.schemas[name]) {
              "Schema $name could not be found in ${openAPI.components.schemas}. Is it missing?"
            }
            .valueOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")
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

  tailrec fun ReferenceOr<String>?.get(): String? =
    when (this) {
      is ReferenceOr.Value -> value
      null -> null
      is ReferenceOr.Reference -> {
        val name = ref.drop("#/components/schemas/".length).dropLast("/description".length)
        val schema =
          requireNotNull(openAPI.components.schemas[name]) {
              "Schema $name could not be found in ${openAPI.components.schemas}. Is it missing?"
            }
            .valueOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")
        schema.description.get()
      }
    }

  private fun Schema.toObject(context: NamingContext): Model =
    when {
      properties != null -> toObject(context, properties!!)
      additionalProperties != null ->
        when (val props = additionalProperties!!) {
          // TODO: implement Schema validation
          is AdditionalProperties.PSchema ->
            Model.FreeFormJson(description.get(), Constraints.Object(this))
          is Allowed ->
            if (props.value) Model.FreeFormJson(description.get(), Constraints.Object(this))
            else
              throw IllegalStateException(
                "No additional properties allowed on object without properties. $this"
              )
        }
      else -> Model.FreeFormJson(description.get(), Constraints.Object(this))
    }

  /**
   * allOf defines an object that is a combination of all the defined allOf schemas. For example: an
   * object with age, and name + an object with id == an object with age, name and id.
   *
   * This is still a WIP. We need to implement a more fine-grained approach to combining schemas,
   * such that we can generate the most idiomatic Kotlin code in all cases. Different results are
   * likely desired, depending on what kind of schemas need to be combined. Simple products, or more
   * complex combinations including oneOf, anyOf, etc.
   */
  private fun allOf(schema: Schema, context: NamingContext): Model {
    val resolved = schema.allOf!!.map { it.resolve().value }
    // Determine if all sub-schemas are object-like (i.e., can be merged as an object)
    val allObjectLike =
      resolved.all { sub ->
        sub.type == Type.Basic.Object || sub.properties != null || sub.additionalProperties != null
      }
    if (allObjectLike) {
      // Merge properties in order; later subschemas override earlier ones on key conflicts
      val mergedProps = linkedMapOf<String, ReferenceOr<Schema>>()
      val mergedRequired = linkedSetOf<String>()

      // Prefer top-level description; if absent, take first non-null encountered below
      var description: ReferenceOr<String>? = schema.description
      var additionalProperties: AdditionalProperties? = null // drop to avoid unsupported combos
      var nullable: Boolean? = schema.nullable
      var discriminator: Schema.Discriminator? = null
      var minProperties: Int? = null
      var maxProperties: Int? = null
      var readOnly: Boolean? = null
      var writeOnly: Boolean? = null
      var externalDocs: ExternalDocs? = null
      var example: ExampleValue? = null
      var default: ExampleValue? = null
      var id: String? = null
      var anchor: String? = null
      var deprecated: Boolean? = null

      resolved.forEach { sub ->
        sub.properties?.let { mergedProps.putAll(it) }
        sub.required.forEach { mergedRequired.add(it) }
        if (description == null) description = sub.description
        // Avoid setting additionalProperties=true with properties due to toObject() restriction
        if (additionalProperties == null) additionalProperties = sub.additionalProperties
        if (nullable == null) nullable = sub.nullable
        if (discriminator == null) discriminator = sub.discriminator
        if (minProperties == null) minProperties = sub.minProperties
        if (maxProperties == null) maxProperties = sub.maxProperties
        if (readOnly == null) readOnly = sub.readOnly
        if (writeOnly == null) writeOnly = sub.writeOnly
        if (externalDocs == null) externalDocs = sub.externalDocs
        if (example == null) example = sub.example
        if (default == null) default = sub.default
        if (id == null) id = sub.id
        if (anchor == null) anchor = sub.anchor
        if (deprecated == null) deprecated = sub.deprecated
      }

      // Edge-case: if no properties resulted and any subschema allows free-form
      // additionalProperties,
      // represent as free-form JSON.
      val anyAllowsAdditional =
        resolved.any { (it.additionalProperties as? Allowed)?.value == true }
      if (mergedProps.isEmpty() && anyAllowsAdditional) {
        return Model.FreeFormJson(schema.description.get(), Constraints.Object(schema))
      }

      // If container had additionalProperties allowed true and properties exist, keep null here to
      // avoid violating toObject() precondition.
      val mergedSchema =
        Schema(
          type = Type.Basic.Object,
          properties = mergedProps,
          additionalProperties = null,
          description = description,
          required = mergedProps.keys.toList(),
          nullable = nullable,
          discriminator = discriminator,
          minProperties = minProperties,
          maxProperties = maxProperties,
          readOnly = readOnly,
          writeOnly = writeOnly,
          externalDocs = externalDocs,
          example = example,
          default = default,
          id = id,
          anchor = anchor,
          deprecated = deprecated,
        )
      return mergedSchema.toObject(context, mergedProps)
    }

    return if ((schema.additionalProperties as? Allowed)?.value == true)
      Model.FreeFormJson(schema.description.get(), Constraints.Object(schema))
    else schema.toUnion(context, schema.allOf!!)
  }

  fun Schema.toObject(context: NamingContext, properties: Map<String, ReferenceOr<Schema>>): Model {
    require((additionalProperties as? Allowed)?.value != true) {
      "Additional properties, on a schema with properties, are not yet supported."
    }
    return Model.Object(
      context,
      description.get(),
      properties.map { (name, ref) ->
        val resolved = ref.resolve()
        val pContext =
          when (resolved) {
            is Resolved.Ref -> Named(resolved.name)
            is Resolved.Value -> NamingContext.Nested(Named(name), context)
          }
        val model = resolved.toModel(pContext)
        // TODO implement oneOf required properties properly
        //   This cannot be done with @Required, but needs to be part of validation
        //        val oneOfRequired = oneOf?.any {
        // it.resolve().value.required.orEmpty().contains(name) }
        Property(
          name,
          model.value,
          required.contains(name),
          resolved.value.nullable ?: !required.contains(name),
          resolved.value.description.get(),
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
      },
    )
  }

  private fun Schema.singleDefaultOrNull(): String? = (default as? ExampleValue.Single)?.value

  fun Schema.toOpenEnum(context: NamingContext, values: List<String>): Enum.Open {
    require(values.isNotEmpty()) { "OpenEnum requires at least 1 possible value" }
    val default = singleDefaultOrNull()
    return Enum.Open(context, values, default, description.get())
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
            // Translate empty JS array to empty list
            value == "[]" -> emptyList()
            // 'null' for a non-nullable collection becomes an empty list
            value.equals("null", ignoreCase = true) ->
              if (nullable == true) listOf("null") else emptyList()
            else -> listOf(value)
          }
        }
        null -> null
      }
    return if (uniqueItems == true)
      Collection.Set(inner.value, default, description.get(), Constraints.Collection(this))
    else Collection.List(inner.value, default, description.get(), Constraints.Collection(this))
  }

  fun Schema.toEnum(context: NamingContext, enums: List<String>): Enum.Closed {
    require(enums.isNotEmpty()) { "Enum requires at least 1 possible value" }
    /* To resolve the inner type, we erase the enum values.
     * Since the schema is still on the same level - we keep the topLevelName */
    val inner = Resolved.Value(copy(enum = null)).toModel(context)
    val default = singleDefaultOrNull()
    return Enum.Closed(context, inner.value, enums, default, description.get())
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
    subtypes: List<ReferenceOr<Schema>>,
  ): Model.Union {
    val caseToContext =
      subtypes.withIndex().map { (idx, ref) ->
        val resolved = ref.resolve()
        Pair(resolved, toUnionCaseContext(context, resolved, idx))
      }
    val cases =
      caseToContext
        .map { (resolved, caseContext) ->
          Model.Union.Case(caseContext, resolved.toModel(caseContext).value)
        }
        .sortedWith(unionSchemaComparator)
    val inline =
      caseToContext.mapNotNull { (resolved, caseContext) -> nestedModel(resolved, caseContext) }
    return Model.Union(
      context,
      cases,
      singleDefaultOrNull()
        ?: subtypes.firstNotNullOfOrNull { it.resolve().value.singleDefaultOrNull() },
      description.get(),
      inline,
    )
  }

  fun toUnionCaseContext(
    context: NamingContext,
    case: Resolved<Schema>,
    index: Int,
  ): NamingContext =
    when (case) {
      is Resolved.Ref -> Named(case.name)
      is Resolved.Value ->
        when {
          context is Named && case.value.type == Type.Basic.String && case.value.enum != null ->
            NamingContext.Nested(
              Named(
                case.value.enum!!.joinToString(prefix = "", separator = "Or") {
                  it.replaceFirstChar(Char::uppercaseChar)
                }
              ),
              context,
            )
          case.value.type == Type.Basic.Object ->
            NamingContext.Nested(
              case.value.properties
                ?.firstNotNullOfOrNull { (key, value) ->
                  if (key == "event" || key == "type") value.resolve().value.enum else null
                }
                ?.singleOrNull()
                ?.let(::Named)
                ?: run {
                  val baseName = if (context is Named) context.name else "Inline"
                  Named("${baseName}Case${index + 1}")
                },
              context,
            )
          case.value.type == Type.Basic.Array ->
            case.value.items
              ?.resolve()
              ?.namedOr { if (case.value.uniqueItems == true) Named("Set") else Named("List") }
              ?.let { NamingContext.Nested(it, context) } ?: context
          else -> context
        }
    }

  private fun nestedModel(resolved: Resolved<Schema>, caseContext: NamingContext): Model? =
    when (val model = resolved.toModel(caseContext)) {
      is Resolved.Ref -> null
      is Resolved.Value ->
        when (model.value) {
          is Collection ->
            when (val inner = resolved.value.items?.resolve()) {
              is Resolved.Value -> nestedModel(Resolved.Value(inner.value), caseContext)
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
    create: (NamingContext) -> NamingContext,
    opName: String,
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
                    json.namedOr {
                      val name = Named("${opName}Request")
                      create(name)
                    }

                  Route.Body.Json.Defined(
                    json.toModel(context).value,
                    body.description,
                    mediaType.extensions,
                  )
                } ?: Route.Body.Json.FreeForm(body.description, mediaType.extensions)
              Pair(ContentType.Application.Json, json)
            }
            ContentType.MultiPart.FormData.match(contentType) -> {
              val resolved =
                mediaType.schema?.resolve()
                  ?: throw IllegalStateException(
                    "$mediaType without a schema. Generation doesn't know what to do, please open a ticket!"
                  )

              fun ctx(name: String): NamingContext =
                resolved.namedOr { create(NamingContext.RouteParam(name, opName, "Request")) }

              val multipart =
                when (resolved) {
                  is Resolved.Ref -> {
                    val model = resolved.toModel(Named(resolved.name)) as Resolved.Ref
                    Route.Body.Multipart.Ref(
                      model.name,
                      model.value,
                      body.description,
                      mediaType.extensions,
                    )
                  }
                  is Resolved.Value ->
                    Route.Body.Multipart.Value(
                      resolved.value.properties!!.map { (name, ref) ->
                        Route.Body.Multipart.FormData(name, ref.resolve().toModel(ctx(name)).value)
                      },
                      body.description,
                      mediaType.extensions,
                    )
                }

              Pair(ContentType.MultiPart.FormData, multipart)
            }
            ContentType.Application.OctetStream.match(contentType) ->
              Pair(
                ContentType.Application.OctetStream,
                Route.Body.OctetStream(body.description, mediaType.extensions),
              )
            else ->
              throw IllegalStateException("RequestBody content type: $this not yet supported.")
          }
        }
        .orEmpty(),
      body?.extensions.orEmpty(),
    )

  private fun Response.isEmpty(): Boolean =
    headers.isEmpty() && content.isEmpty() && links.isEmpty() && extensions.isEmpty()

  fun toResponses(
    operation: Operation,
    create: (NamingContext) -> NamingContext,
    opName: String,
  ): Route.Returns =
    Route.Returns(
      operation.responses.responses.entries.associate { (code, refOrResponse) ->
        val statusCode = HttpStatusCode.fromValue(code)
        val response = refOrResponse.get()
        when {
          response.content.contains("application/octet-stream") ->
            Pair(
              statusCode,
              Route.ReturnType(Model.OctetStream(response.description), response.extensions),
            )
          response.content.contains("application/json") -> {
            val mediaType = response.content.getValue("application/json")
            val route =
              when (val resolved = mediaType.schema?.resolve()) {
                is Resolved -> {
                  val context =
                    resolved.namedOr { create(NamingContext.RouteBody(opName, "Response")) }
                  run {
                    val model = resolved.toModel(context).value
                    val cleaned =
                      when (model) {
                        is Model.Object -> model.copy(inline = emptyList())
                        is Model.Union -> model.copy(inline = emptyList())
                        else -> model
                      }
                    Route.ReturnType(cleaned, response.extensions)
                  }
                }
                null ->
                  Route.ReturnType(
                    if (Allowed(true).value) Model.FreeFormJson(response.description, null)
                    else
                      throw IllegalStateException(
                        "Illegal State: No additional properties allowed on empty object."
                      ),
                    response.extensions,
                  )
              }
            Pair(statusCode, route)
          }
          response.isEmpty() ->
            Pair(
              statusCode,
              Route.ReturnType(
                Primitive.String(null, response.description, null),
                response.extensions,
              ),
            )
          else ->
            throw IllegalStateException("OpenAPI requires at least 1 valid response. $response")
        }
      },
      operation.responses.extensions,
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

    @JvmInline value class Value<A>(override val value: A) : Resolved<A>

    fun namedOr(orElse: () -> NamingContext): NamingContext =
      when (this) {
        is Ref -> Named(name)
        is Value -> orElse()
      }

    fun valueOrNull(): A? =
      when (this) {
        is Ref -> null
        is Value -> value
      }
  }
}
