package io.github.nomisrev

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.Components
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema

fun <A : Any, B : Any> Model.Default<A>.map(block: (A) -> B): Model.Default<B> = when (this) {
    Model.Default.Null -> Model.Default.Null
    is Model.Default.Value<A> -> Model.Default.Value(block(value))
}

fun Schema.nullable(): Schema = copy(nullable = true)

@Suppress("UNCHECKED_CAST")
fun Model.default(default: Model.Default<*>?): Model = when (this) {
    is Model.Enum -> copy(default = default as Model.Default<String>?)
    is Model.Collection -> copy(default = default as Model.Default<List<String>>?)
    is Model.Primitive.Boolean -> copy(default = default as Model.Default<Boolean>?)
    is Model.Primitive.Double -> copy(default = default as Model.Default<Double>?)
    is Model.Primitive.Float -> copy(default = default as Model.Default<Float>?)
    is Model.Primitive.Int -> copy(default = default as Model.Default<Int>?)
    is Model.Primitive.Long -> copy(default = default as Model.Default<Long>?)
    is Model.Primitive.String -> copy(default = default as Model.Default<String>?)
    is Model.Union -> copy(default = default as Model.Default<String>?)
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive.Unit,
    is Model.DiscriminatedObject,
    is Model.DateTime,
    is Model.Date,
    is Model.Object,
    is Model.FreeFormJson,
    is Model.ByteArray -> this
}

fun Model.context(block: (NamingContext) -> NamingContext): Model = when (this) {
    is Model.ContextHolder -> when (this) {
        is Model.DiscriminatedObject -> copy(context = block(context))
        is Model.Enum -> copy(context = block(context))
        is Model.Object -> copy(context = block(context))
        is Model.Reference -> copy(context = block(context))
        is Model.Union -> copy(context = block(context))
    }

    else -> this
}

val api = OpenAPI(
    info = Info("Test", "1.0"),
    components = Components(
        schemas = mapOf(
            "MyDescription" to ReferenceOr.value(Schema(description = ReferenceOr.value("My Description")))
        )
    )
)

fun OpenAPI.reference(name: String, schema: Schema) =
    copy(components = components.copy(schemas = components.schemas + (name to ReferenceOr.value(schema))))

val description = listOf(
    ReferenceOr.value("My Description") expect "My Description",
    ReferenceOr.schema("MyDescription") expect "My Description",
    null expect null
)