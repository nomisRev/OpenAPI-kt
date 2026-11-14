package io.github.nomisrev.openapi.routes

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Style
import io.github.nomisrev.openapi.registry.Registry

/**
 * The `style` of a parameter at this location that declares none (OpenAPI 3.1.1, 4.8.12.2): `form` for `query` and
 * `cookie`, `simple` for `path` and `header`.
 */
val Parameter.Input.defaultStyle: Style
    get() = when (this) {
        Parameter.Input.Query, Parameter.Input.Cookie -> Style.form
        Parameter.Input.Path, Parameter.Input.Header -> Style.simple
    }

/** The styles OpenAPI defines for a parameter at this location (OpenAPI 3.1.1, 4.8.12.4, Style Values). */
private val Parameter.Input.styles: Set<Style>
    get() = when (this) {
        Parameter.Input.Query -> setOf(Style.form, Style.spaceDelimited, Style.pipeDelimited, Style.deepObject)
        Parameter.Input.Cookie -> setOf(Style.form)
        Parameter.Input.Path -> setOf(Style.simple, Style.label, Style.matrix)
        Parameter.Input.Header -> setOf(Style.simple)
    }

/**
 * The [Route.Body.FormStyle] of a query parameter, cookie or form field of [style] and [explode], or `null` for a
 * style that is not a form's: `simple`, `label` and `matrix`.
 */
internal fun formStyleOf(style: Style, explode: Boolean): Route.Body.FormStyle? = when (style) {
    Style.form -> if (explode) Route.Body.FormStyle.FormExploded else Route.Body.FormStyle.Form
    Style.spaceDelimited -> Route.Body.FormStyle.SpaceDelimited
    Style.pipeDelimited -> Route.Body.FormStyle.PipeDelimited
    Style.deepObject -> Route.Body.FormStyle.DeepObject
    Style.simple, Style.label, Style.matrix -> null
}

/**
 * Whether a parameter of this model, at [location], with the effective [style] and [explode], has a serialization the
 * client generates (OpenAPI 3.1.1, 4.8.12.4, and RFC 6570 for `simple`, `label`, `matrix` and `form`):
 *
 * - [style] must be one OpenAPI defines for [location]: `simple` for a header, `form` for a cookie.
 * - A scalar (a primitive, date, UUID, enum or a wrapper around one) is its value whatever the style of its location;
 *   only a path enum, whose segments are fixed values, and a path union, whose cases are overloads, must be `simple`.
 * - An array of non-null scalars is defined for every style but `deepObject`, and the `spaceDelimited` and
 *   `pipeDelimited` styles define it only without `explode`.
 * - An object of scalar properties, and nothing else, and a map of scalars are defined for every style of a query
 *   parameter, header or cookie, again without `explode` for the delimited styles. A path object or map is not
 *   generated. A wrapper the typed layer gives a named array or map schema is the array or map it wraps.
 * - A union is supported in a query, header or cookie when every case is a primitive, date, UUID or array the
 *   parameter supports; it is sent as the case it holds.
 * - A parameter with `content` is serialized as its media type; only a scalar, whose value is sent as is, is
 *   generated.
 *
 * Anything else (a map of non-scalars, an array of objects or of nullable items, an object with an object or array
 * property, a style OpenAPI leaves undefined) is reported instead of generated.
 */
context(scope: Registry)
internal suspend fun Model.isSerializableParameter(
    location: Parameter.Input,
    style: Style,
    explode: Boolean,
    hasContent: Boolean,
): Boolean {
    val model = formModel() ?: return false
    if (hasContent) return model.isFormScalar()
    if (model is Model.Object && model.isTransparentWrapper && !model.isScalarWrapper && model.properties.size == 1) {
        return model.properties.values.single().model.isSerializableParameter(location, style, explode, hasContent)
    }
    if (style !in location.styles) return false
    val delimited = style == Style.spaceDelimited || style == Style.pipeDelimited
    return when {
        model is Model.Enum -> location != Parameter.Input.Path || style == Style.simple
        model.isFormScalar() -> true
        model is Model.Collection ->
            style != Style.deepObject && !(delimited && explode) &&
                !model.inner.isNullable && model.inner.formModel()?.isFormScalar() == true

        model is Model.Object ->
            location != Parameter.Input.Path && !(delimited && explode) &&
                model.additionalProperties == Model.Object.AdditionalProperties.False &&
                model.properties.values.all { it.model.formModel()?.isFormScalar() == true }

        model is Model.Dictionary ->
            location != Parameter.Input.Path && !(delimited && explode) &&
                !model.inner.isNullable && model.inner.formModel()?.isFormScalar() == true

        model is Model.Union ->
            if (location == Parameter.Input.Path) style == Style.simple
            else model.cases.all { case ->
                case.model.isUnionParameterCase() &&
                    case.model.isSerializableParameter(location, style, explode, hasContent = false)
            }

        else -> false
    }
}

/**
 * A union case the client wraps in a value class of its own (`CaseString(value)`, `CaseStrings(value)`), so a
 * parameter holding it is sent as its `value`.
 */
private fun Model.isUnionParameterCase(): Boolean = when (this) {
    is Model.Collection,
    is Model.Date,
    is Model.DateTime,
    is Model.Uuid,
    is Model.Primitive.Boolean,
    is Model.Primitive.Double,
    is Model.Primitive.Float,
    is Model.Primitive.Int,
    is Model.Primitive.Long,
    is Model.Primitive.String -> true

    else -> false
}

/**
 * The model of a parameter or form field of [schema], whose [this] model is the one the schema has anywhere else.
 *
 * A query, header, cookie or path parameter and a form field is a string on the wire, so the value of a schema without
 * a `type` (`{}`, `items: {}`, an enum of members of different JSON types) is a `JsonPrimitive` sent as its
 * `content`, not any `JsonElement`: `form` and `simple` define no serialization of a nested value. A free-form object
 * (`type: object` without properties) is a map of them, which `form` defines as one pair per entry. The schema of an
 * array item that is a free-form object stays as it is: nested, so undefined.
 *
 * Free-form JSON in a JSON body or a model property is not affected.
 */
context(scope: Registry)
internal suspend fun Model.asParameterValue(schema: ReferenceOr<Schema>?): Model {
    if (schema == null) return this
    if (schema is ReferenceOr.Reference) {
        // A reference to an untyped schema is a value class around a `JsonElement`: the parameter is the primitive.
        val wrapper = formModel() as? Model.Object ?: return this
        val inner = wrapper.properties["value"]?.model as? Model.FreeFormJson
        if (!wrapper.isScalarWrapper || inner == null) return this
        return inner.with(isNullable = wrapper.isNullable || inner.isNullable)
            .asParameterValue(ReferenceOr.value(schema.resolveSchema()))
    }
    val value = (schema as? ReferenceOr.Value)?.value ?: return this
    return when (this) {
        is Model.FreeFormJson ->
            if (value.isObjectSchema) Model.Dictionary(copy(description = null, isPrimitive = true), description, constraint, isNullable, title)
            else copy(isPrimitive = true)

        is Model.Collection -> {
            val item = (value.items as? ReferenceOr.Value)?.value
            if (inner is Model.FreeFormJson && item?.isObjectSchema != true) copy(inner = inner.copy(isPrimitive = true))
            else this
        }

        else -> this
    }
}

private val Schema.isObjectSchema: Boolean
    get() = when (val type = type) {
        Schema.Type.Basic.Object -> true
        is Schema.Type.Array -> Schema.Type.Basic.Object in type.types
        else -> false
    }
