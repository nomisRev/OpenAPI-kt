package io.github.nomisrev.openapi

public fun Model.Primitive.Companion.string(
  default: String? = null,
  description: String? = null,
  constraint: TextConstraint = TextConstraint(Int.MAX_VALUE, 0, null)
): Model.Primitive.String =
  Model.Primitive.String(default = default, description = description, bounds = constraint)

public fun Model.Primitive.Companion.int(
  default: Int? = null,
  description: String? = null,
  constraint: NumberConstraint =
    NumberConstraint(false, Double.NEGATIVE_INFINITY, false, Double.POSITIVE_INFINITY, null)
): Model.Primitive.Int =
  Model.Primitive.Int(default = default, description = description, bounds = constraint)

public fun Model.Primitive.Companion.double(
  default: Double? = null,
  description: String? = null,
  constraint: NumberConstraint =
    NumberConstraint(false, Double.NEGATIVE_INFINITY, false, Double.POSITIVE_INFINITY, null)
): Model.Primitive.Double =
  Model.Primitive.Double(default = default, description = description, bounds = constraint)

public fun Model.Collection.Companion.list(
  inner: Model,
  default: List<String>? = null,
  description: String? = null,
  constraint: CollectionConstraint = CollectionConstraint(0, Int.MAX_VALUE)
): Model.Collection.List =
  Model.Collection.List(
    inner = inner,
    default = default,
    description = description,
    bounds = constraint
  )

public fun Model.Collection.Companion.set(
  inner: Model,
  default: List<String>? = null,
  description: String? = null,
  constraint: CollectionConstraint = CollectionConstraint(0, Int.MAX_VALUE)
): Model.Collection.Set =
  Model.Collection.Set(inner = inner, default = default, description = description, bounds = constraint)

public fun Model.Object.Companion.property(
  baseName: String,
  model: Model,
  isRequired: Boolean = false,
  isNullable: Boolean = true,
  description: String? = null
): Model.Object.Property =
  Model.Object.Property(
    baseName = baseName,
    model = model,
    isRequired = isRequired,
    isNullable = isNullable,
    description = description
  )


public fun Model.Companion.obj(
  context: NamingContext,
  properties: List<Model.Object.Property>,
  inline: List<Model>,
  constraint: ObjectConstraint = ObjectConstraint(0, Int.MAX_VALUE),
  description: String? = null
): Model.Object =
  Model.Object(
    context = context,
    description = description,
    properties = properties,
    inline = inline,
    constraint = constraint,
  )

public fun Model.Companion.union(
  context: NamingContext,
  cases: List<Model.Union.Case>,
  inline: List<Model>,
  default: String? = null,
  description: String? = null
): Model.Union =
  Model.Union(context = context, cases = cases, default = default, inline = inline, description = description)

fun Schema.toModel(name: String): Model =
  testAPI.copy(
    components = Components(schemas = mapOf(name to ReferenceOr.value(this)))
  ).models().first()