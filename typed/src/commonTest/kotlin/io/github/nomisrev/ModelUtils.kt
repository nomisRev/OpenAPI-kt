package io.github.nomisrev

import io.github.nomisrev.openapi.Model
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

val api = OpenAPI(
    info = Info("Test", "1.0"),
    components = Components(
        schemas = mapOf(
            "MyDescription" to ReferenceOr.value(Schema(description = ReferenceOr.value("My Description")))
        )
    )
)

val description = listOf(
    ReferenceOr.value("My Description") expect "My Description",
    ReferenceOr.schema("MyDescription") expect "My Description",
    null expect null
)