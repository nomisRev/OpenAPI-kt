package io.github.nomisrev.openapi

import com.charleskorn.kaml.Yaml.Companion.default
import io.github.nomisrev.openapi.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.Schema.Type
import kotlin.text.toBooleanStrictOrNull
import kotlin.text.toDoubleOrNull

//<editor-fold desc="toModel">
context(ctx: TypedApiContext)
internal fun Resolved<Schema>.toModel(
  context: NamingContext
): Resolved<Model> = toModel(ModelResolutionContext(context, SchemaResolutionStrategy.ForComponents))

context(ctx: TypedApiContext)
internal fun Resolved<Schema>.toModel(
  resolutionContext: ModelResolutionContext
): Resolved<Model> = when (this) {
  is Resolved.Value -> Resolved.Value(value.toModel(resolutionContext))
  is Resolved.Ref -> {
    val desc = value.description.get()
    when (resolutionContext.strategy) {
      is SchemaResolutionStrategy.ForComponents ->
        Resolved.Ref(name, Model.Reference(Named(name), desc))

      // When processing inline schemas, resolve references through ComponentRegistry
      is SchemaResolutionStrategy.ForInline -> when (val model = ctx.get(name)) {
        // Fallback to reference if not found in the registry
        null -> Resolved.Ref(name, Model.Reference(Named(name), desc))
        else -> Resolved.Value(model.withDescriptionIfNull(desc))
      }
    }
  }
}

context(ctx: TypedApiContext)
private fun Schema.context(context: ModelResolutionContext): ModelResolutionContext {
  val anchorName = (context.naming as? Named)?.name
  return when {
    recursiveAnchor == true && anchorName != null ->
      ModelResolutionContext(context.naming, context.strategy, anchorName to this)

    else -> context
  }
}

context(ctx: TypedApiContext)
internal fun Schema.toModel(
  resolutionContext: ModelResolutionContext
): Model {
  val resolutionContext = context(resolutionContext)
  return when {
    isOpenEnumeration(resolutionContext.strategy) -> toOpenEnum(
      resolutionContext,
      anyOf!!.firstNotNullOf {
        it.resolve(resolutionContext.strategy).value.enum

      }.filterNotNull(),
    )

    anyOf != null && anyOf?.size == 2
      && anyOf!!.any { it.resolve(resolutionContext.strategy).value.type == Type.Basic.Null } -> {
      anyOf!!.single { it.resolve(resolutionContext.strategy).value.type != Type.Basic.Null }
        .resolve(resolutionContext.strategy)
        .copy { it.copy(nullable = true) }
        .toModel(resolutionContext)
        .value
    }

    anyOf != null && anyOf?.size == 1 -> {
      val inner = anyOf!![0].resolve(resolutionContext.strategy).toModel(resolutionContext).value
      inner.withDescriptionIfNull(description.get())
    }

    oneOf != null && oneOf?.size == 1 -> {
      val inner = oneOf!![0].resolve(resolutionContext.strategy).toModel(resolutionContext).value
      inner.withDescriptionIfNull(description.get())
    }

    anyOf != null -> toUnion(resolutionContext, anyOf!!)
    oneOf != null && properties != null -> toObject(resolutionContext)
    oneOf != null -> toUnion(resolutionContext, oneOf!!)
    allOf != null -> allOf(this@toModel, resolutionContext)
    enum != null -> {
      val enums = enum!!
      val hasNull = enums.any { it == null }
      val filtered = enums.filterNotNull()
      val effectiveSchema =
        if (hasNull && nullable != true) copy(nullable = true) else this@toModel
      effectiveSchema.toEnum(resolutionContext, filtered)
    }

    else -> type(resolutionContext)
  }
}

context(ctx: TypedApiContext)
private fun Schema.type(resolutionContext: ModelResolutionContext): Model =
  when (val type = type) {
    is Type.Array ->
      when (val single = type.types.singleOrNull()) {
        null if type.types.isEmpty() -> collection(resolutionContext)
        null -> {
          val resolved = type.types.sorted().map { t -> Resolved.Value(Schema(type = t)) }
          Model.Union(
            context = resolutionContext.naming,
            cases = resolved.map { Model.Union.Case(resolutionContext.naming, it.toModel(resolutionContext).value) },
            default = null,
            description = description.get(),
            inline = resolved.mapNotNull { nestedModel(it, resolutionContext) },
            discriminator = null,
          )
        }

        else -> copy(type = single).type(resolutionContext)
      }

    is Type.Basic ->
      when (type) {
        Type.Basic.Array -> collection(resolutionContext)
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

        Type.Basic.Object -> toObject(resolutionContext)
        Type.Basic.Null -> TODO("Schema.Type.Basic.Null")
      }

    null ->
      when {
        // If no type is defined, but we find properties, or additionalProperties, we assume it's
        // an object.
        properties != null || additionalProperties != null -> toObject(resolutionContext)
        // If 'items' is defined, we assume it's an array.
        items != null -> collection(resolutionContext)
        else -> Model.FreeFormJson(description.get(), null)
      }
  }

context(ctx: TypedApiContext)
private fun Schema.collection(resolutionContext: ModelResolutionContext): Model.Collection {
  val inner = when (val items = items?.resolve(resolutionContext.strategy)) {
    null -> Model.FreeFormJson(description.get(), null)
    else -> items.toModel(
      ModelResolutionContext(
        items.namedOr { resolutionContext.naming },
        resolutionContext.strategy
      )
    ).value
  }
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
  return Model.Collection.List(inner, default, description.get(), Constraints.Collection(this))
}

context(ctx: TypedApiContext)
private fun nestedModel(resolved: Resolved<Schema>, resolutionContext: ModelResolutionContext): Model? =
  when (resolved) {
    is Resolved.Ref -> null // Do not inline referenced component schemas as nested models
    is Resolved.Value ->
      when (val model = resolved.toModel(resolutionContext)) {
        is Resolved.Ref -> null // redundant, kept for safety
        is Resolved.Value ->
          when (val value = model.value) {
            is Model.Collection ->
              when (value) {
                is Model.Collection.List ->
                  when (val inner = resolved.value.items?.resolve(resolutionContext.strategy)) {
                    is Resolved.Value -> nestedModel(Resolved.Value(inner.value), resolutionContext)
                    is Resolved.Ref -> null
                    null -> null
                  }

                is Model.Collection.Map ->
                  when (val ap = resolved.value.additionalProperties) {
                    is AdditionalProperties.PSchema ->
                      when (val inner = ap.value.resolve(resolutionContext.strategy)) {
                        is Resolved.Value -> nestedModel(Resolved.Value(inner.value), resolutionContext)
                        is Resolved.Ref -> null
                      }

                    else -> null
                  }
              }

            else -> model.value
          }
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

context(ctx: TypedApiContext)
private fun Schema.isOpenEnumeration(strategy: SchemaResolutionStrategy): Boolean {
  val anyOf = anyOf ?: return false
  return anyOf.size == 2 &&
    anyOf.count { it.resolve(strategy).value.enum != null } == 1 &&
    anyOf.count { it.resolve(strategy).value.type == Type.Basic.String } == 2
}
//</editor-fold>

//<editor-fold desc="toObject">
context(ctx: TypedApiContext)
private fun Schema.toObject(resolutionContext: ModelResolutionContext): Model =
  when {
    properties?.isEmpty() == true && (additionalProperties as? Allowed)?.value == true ->
      Model.FreeFormJson(description.get(), Constraints.Object(this))

    properties != null -> toObject(resolutionContext, properties!!)
    additionalProperties != null ->
      when (val props = additionalProperties!!) {
        is AdditionalProperties.PSchema -> {
          val innerResolved = props.value.resolve(resolutionContext.strategy)
          val isEmpty = (innerResolved as? Resolved.Value)?.value == Schema()
          if (isEmpty) {
            // {} is equivalent to additionalProperties: true -> free-form
            Model.FreeFormJson(description.get(), Constraints.Object(this))
          } else {
            val innerCtx = innerResolved.namedOr { resolutionContext.naming }
            val innerModel = innerResolved.toModel(ModelResolutionContext(innerCtx, resolutionContext.strategy)).value
            Model.Collection.Map(innerModel, description.get(), /* constraint= */ null)
          }
        }

        is Allowed ->
          if (props.value) Model.FreeFormJson(description.get(), Constraints.Object(this))
          else
            throw IllegalStateException(
              "No additional properties allowed on object without properties. $this"
            )
      }

    else -> Model.FreeFormJson(description.get(), Constraints.Object(this))
  }

context(ctx: TypedApiContext)
private fun Schema.toObject(
  resolutionContext: ModelResolutionContext,
  properties: Map<String, ReferenceOr<Schema>>
): Model {
  // Allow additionalProperties=true with properties; capture as marker on Model.Object
  val hasAnyAdditional: Boolean = when (val ap = additionalProperties) {
    is Allowed -> ap.value
    is AdditionalProperties.PSchema -> {
      val inner = ap.value.resolve(resolutionContext.strategy)
      (inner as? Resolved.Value)?.value == Schema()
    }

    else -> false
  }
  return Model.Object(
    resolutionContext.naming,
    description.get(),
    properties.map { (name, ref) ->
      val resolved = ref.resolve(resolutionContext.strategy)
      val pContext =
        when (resolved) {
          is Resolved.Ref -> Named(resolved.name)
          is Resolved.Value -> NamingContext.Nested(Named(name), resolutionContext.naming)
        }
      val model = resolved.toModel(ModelResolutionContext(pContext, resolutionContext.strategy))
      // TODO implement oneOf required properties properly
      //   This cannot be done with @Required, but needs to be part of validation
      //        val oneOfRequired = oneOf?.any {
      // it.resolve().value.required.orEmpty().contains(name) }
      Property(
        name,
        model.value,
        required.contains(name),
        resolved.value.nullable ?: required.contains(name).not(),
        resolved.value.description.get(),
      )
    },
    properties.mapNotNull { (name, ref) ->
      val resolved = ref.resolve(resolutionContext.strategy)
      val pContext =
        when (resolved) {
          is Resolved.Ref -> Named(resolved.name)
          is Resolved.Value -> NamingContext.Nested(Named(name), resolutionContext.naming)
        }
      nestedModel(resolved, ModelResolutionContext(pContext, resolutionContext.strategy))
    },
    additionalProperties = hasAnyAdditional,
  )
}
//</editor-fold>

//<editor-fold desc="Enum">
context(ctx: TypedApiContext)
private fun Schema.toOpenEnum(resolutionContext: ModelResolutionContext, values: List<String>): Model.Enum.Open {
  require(values.isNotEmpty()) { "OpenEnum requires at least 1 possible value" }
  val default = singleDefaultOrNull()
  return Model.Enum.Open(resolutionContext.naming, values, default, description.get())
}

context(ctx: TypedApiContext)
private fun Schema.toEnum(resolutionContext: ModelResolutionContext, enums: List<String>): Model.Enum.Closed {
  require(enums.isNotEmpty()) { "Enum requires at least 1 possible value" }
  /* To resolve the inner type, we erase the enum values.
   * Since the schema is still on the same level - we keep the topLevelName */
  val inner = Resolved.Value(copy(enum = null)).toModel(resolutionContext)
  val default = singleDefaultOrNull()
  return Model.Enum.Closed(resolutionContext.naming, inner.value, enums, default, description.get())
}

context(ctx: TypedApiContext)
private fun Schema.findSingleEnumValue(
  propName: String,
  strategy: SchemaResolutionStrategy = SchemaResolutionStrategy.ForComponents
): String? {
  // direct properties first
  val direct = this.properties?.get(propName)?.resolve(strategy)?.value?.enum?.singleOrNull()
  if (direct != null) return direct
  // search in allOf sub-schemas
  val subs = this.allOf ?: return null
  for (sub in subs) {
    val v = sub.resolve(strategy).value.findSingleEnumValue(propName, strategy)
    if (v != null) return v
  }
  return null
}

context(ctx: TypedApiContext)
private fun Schema.findFirstEnumSingle(strategy: SchemaResolutionStrategy, vararg propNames: String): String? {
  for (p in propNames) {
    val v = findSingleEnumValue(p, strategy)
    if (v != null) return v
  }
  return null
}
//</editor-fold>

//<editor-fold desc="oneOf">
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
      is Model.Enum -> m1.values.size
      is Primitive.String -> -1
      else -> 0
    }
  val m2Complexity =
    when (m2) {
      is Model.Object -> m2.properties.size
      is Model.Enum -> m2.values.size
      is Primitive.String -> -1
      else -> 0
    }
  m2Complexity - m1Complexity
}

context(ctx: TypedApiContext)
private fun Schema.toUnion(
  resolutionContext: ModelResolutionContext,
  subtypes: List<ReferenceOr<Schema>>,
): Model.Union {
  val discriminator = this.discriminator
  val caseToContext =
    subtypes.withIndex().map { (idx, ref) ->
      val resolved = ref.resolve(resolutionContext.strategy)
      Pair(
        resolved,
        toUnionCaseContext(resolutionContext.naming, resolved, idx, discriminator, resolutionContext.strategy)
      )
    }
  val cases =
    caseToContext
      .map { (resolved, caseContext) ->
        Model.Union.Case(
          caseContext,
          resolved.toModel(ModelResolutionContext(caseContext, resolutionContext.strategy)).value
        )
      }
      .sortedWith(unionSchemaComparator)
  val inline =
    caseToContext.mapNotNull { (resolved, caseContext) ->
      nestedModel(
        resolved,
        ModelResolutionContext(caseContext, resolutionContext.strategy)
      )
    }
  return Model.Union(
    resolutionContext.naming,
    cases,
    singleDefaultOrNull()
      ?: subtypes.firstNotNullOfOrNull { it.resolve(resolutionContext.strategy).value.singleDefaultOrNull() },
    description.get(),
    inline,
    this.discriminator?.let { Model.Discriminator(it.propertyName, it.mapping) },
  )
}

context(ctx: TypedApiContext)
private fun toUnionCaseContext(
  context: NamingContext,
  case: Resolved<Schema>,
  index: Int,
  discriminator: Schema.Discriminator?,
  strategy: SchemaResolutionStrategy,
): NamingContext =
  when (case) {
    is Resolved.Ref -> {
      val mappedName = discriminator?.mapping?.entries
        ?.firstOrNull { (_, ref) ->
          ref.endsWith("/${case.name}") || ref == case.name
        }
        ?.key
        ?.replaceFirstChar(Char::uppercaseChar)
      if (mappedName != null) Named(mappedName) else Named(case.name)
    }

    is Resolved.Value ->
      when {
        context is Named && case.value.type == Type.Basic.String && case.value.enum != null ->
          NamingContext.Nested(
            Named(
              case.value.enum.orEmpty().filterNotNull().joinToString(
                prefix = "",
                separator = "Or",
              ) {
                it.replaceFirstChar(Char::uppercaseChar)
              }
            ),
            context,
          )

        case.value.type == Type.Basic.Object || case.value.properties != null || case.value.allOf != null ->
          NamingContext.Nested(
            run {
              // Prefer discriminator property if provided and available as single-value enum (search across allOf)
              val fromDisc = discriminator?.propertyName?.let { prop ->
                case.value.findSingleEnumValue(prop, strategy)
              }
              val fromSpecial = case.value.findFirstEnumSingle(strategy, "event", "type")
              val chosen = fromDisc ?: fromSpecial
              if (chosen != null) Named(chosen.replaceFirstChar(Char::uppercaseChar))
              else {
                val baseName = if (context is Named) context.name else ""
                Named("${baseName}Case${index + 1}")
              }
            },
            context,
          )

        case.value.type == Type.Basic.Array ->
          case.value.items
            ?.resolve(strategy)
            ?.namedOr { Named("List") }
            ?.let { NamingContext.Nested(it, context) } ?: context

        else -> context
      }
  }
//</editor-fold>
//<editor-fold desc="allOff">
/**
 * allOf defines an object that is a combination of all the defined allOf schemas. For example: an
 * object with age, and name + an object with id == an object with age, name and id.
 *
 * This is still a WIP. We need to implement a more fine-grained approach to combining schemas,
 * such that we can generate the most idiomatic Kotlin code in all cases. Different results are
 * likely desired, depending on what kind of schemas need to be combined. Simple products, or more
 * complex combinations including oneOf, anyOf, etc.
 */
context(ctx: TypedApiContext)
private fun allOf(schema: Schema, resolutionContext: ModelResolutionContext): Model {
  val resolved = schema.allOf!!.map { it.resolve(resolutionContext.strategy).value }
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
    var additionalProperties: AdditionalProperties? = null // collect and preserve when true
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
      sub.required?.forEach { mergedRequired.add(it) }
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

    // Optional improvement: if no properties resulted and any subschema defines
    // additionalProperties as a schema,
    // represent as a Map (or FreeForm if the schema is the empty object {}).
    if (mergedProps.isEmpty()) {
      val apSchema =
        resolved.firstNotNullOfOrNull { it.additionalProperties as? AdditionalProperties.PSchema }
      if (apSchema != null) {
        val innerResolved = apSchema.value.resolve(resolutionContext.strategy)
        val isEmpty = (innerResolved as? Resolved.Value)?.value == Schema()
        if (isEmpty) {
          return Model.FreeFormJson(schema.description.get(), Constraints.Object(schema))
        } else {
          val innerCtx = innerResolved.namedOr { resolutionContext.naming }
          val innerModel = innerResolved.toModel(ModelResolutionContext(innerCtx, resolutionContext.strategy)).value
          return Model.Collection.Map(
            innerModel,
            schema.description.get(),
            /* constraint = */ null,
          )
        }
      }
      // Edge-case: if no properties resulted and any subschema allows free-form
      // additionalProperties,
      // represent as free-form JSON.
      val anyAllowsAdditional =
        resolved.any { (it.additionalProperties as? Allowed)?.value == true }
      if (anyAllowsAdditional) {
        return Model.FreeFormJson(schema.description.get(), Constraints.Object(schema))
      }
    }

    // If container had additionalProperties allowed true and properties exist, keep null here to
    // avoid violating toObject() precondition.
    val allowAnyAp: Boolean =
      resolved.any { (it.additionalProperties as? Allowed)?.value == true } ||
        resolved.any {
          val ap = it.additionalProperties as? AdditionalProperties.PSchema
          if (ap != null) {
            val inner = ap.value.resolve(resolutionContext.strategy)
            (inner as? Resolved.Value)?.value == Schema()
          } else false
        }

    val mergedSchema =
      Schema(
        type = Type.Basic.Object,
        properties = mergedProps,
        additionalProperties = if (allowAnyAp) Allowed(true) else null,
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
    return mergedSchema.toObject(resolutionContext, mergedProps)
  }

  return if ((schema.additionalProperties as? Allowed)?.value == true)
    Model.FreeFormJson(schema.description.get(), Constraints.Object(schema))
  else schema.toUnion(resolutionContext, schema.allOf!!)
}
//</editor-fold>

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
    is Model.Collection.List -> if (this.description != null) this else this.copy(description = desc)
    is Model.Collection.Map -> if (this.description != null) this else this.copy(description = desc)
    is Model.Object -> if (this.description != null) this else this.copy(description = desc)
    is Model.Union -> if (this.description != null) this else this.copy(description = desc)
    is Model.Enum.Closed -> if (this.description != null) this else this.copy(description = desc)
    is Model.Enum.Open -> if (this.description != null) this else this.copy(description = desc)
    is Model.Reference -> if (this.description != null) this else this.copy(description = desc)
  }
}

private fun Schema.singleDefaultOrNull(): String? = (default as? ExampleValue.Single)?.value

context(ctx: TypedApiContext)
internal fun ReferenceOr<Schema>.resolve(strategy: SchemaResolutionStrategy): Resolved<Schema> =
  when (this) {
    is ReferenceOr.Value -> Resolved.Value(value)
    is ReferenceOr.Reference -> {
      when (strategy) {
        // Handle JSON Schema $recursiveRef: "#" by resolving to the current anchor
        else if ref == "#" -> {
          val (anchorName, anchorSchema) =
            requireNotNull(ctx.currentAnchor) {
              "Recursive reference '#' encountered but no active \$recursiveAnchor was found."
            }
          Resolved.Ref(anchorName, anchorSchema)
        }

        SchemaResolutionStrategy.ForComponents -> Resolved.Ref(schemaName, ctx.schema(this))
        is SchemaResolutionStrategy.ForInline -> Resolved.Ref(schemaName, ctx.schema(this))
      }
    }
  }

context(ctx: TypedApiContext)
private tailrec fun ReferenceOr<String>?.get(): String? =
  when (this) {
    is ReferenceOr.Value -> value
    null -> null
    is ReferenceOr.Reference -> {
      val name = ref.drop("#/components/schemas/".length).dropLast("/description".length)
      val schema =
        requireNotNull(ctx.openAPI.components.schemas[name]) {
          "Schema $name could not be found in ${ctx.openAPI.components.schemas}. Is it missing?"
        }
          .valueOrNull() ?: throw IllegalStateException("Remote schemas are not yet supported.")
      schema.description.get()
    }
  }