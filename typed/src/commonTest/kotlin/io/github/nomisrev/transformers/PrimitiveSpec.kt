package io.github.nomisrev.transformers

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Eq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.verifyAll
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type.Basic
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.reference
import io.github.nomisrev.verifyFails
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
val PrimitiveSpec by testSuite {
    verifyAll("Primitive types", Model.Primitive.String.all())

    verifyAll("Referenced primitives", Model.Primitive.all()) { schema, inner ->
        val actual = registry(api.reference(schema.toString(), schema)) {
            ReferenceOr.schema(schema.toString())
                .toModel(NamingContext.reference(schema.toString(), SchemaContext.Null), SchemaContext.Write)
        }
        val expected = Model.Object.value(
            NamingContext.Reference(schema.toString(), SchemaContext.Null),
            inner,
            isScalarWrapper = true,
        )
        Eq(expected, actual)
    }

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Number multiple default values throws IllegalArgumentException",
        Schema.number.copy(default = JsonArray(listOf(JsonPrimitive(1.3), JsonPrimitive(2.5)))),
        "Multiple default values not supported for Number at GET /test."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Number single default incorrect value throws IllegalArgumentException",
        Schema.number.copy(default = JsonPrimitive("no-number")),
        "Default value \"no-number\" at GET /test is not a Number."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Integer multiple default values throws IllegalArgumentException",
        Schema.integer.copy(default = JsonArray(listOf(JsonPrimitive(1), JsonPrimitive(2)))),
        "Multiple default values not supported for Integer at GET /test."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Integer single default incorrect value throws IllegalArgumentException",
        Schema.integer.copy(default = JsonPrimitive("no-int")),
        "Default value \"no-int\" at GET /test is not a Integer."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Boolean multiple default values throws IllegalArgumentException",
        Schema.boolean.copy(default = JsonArray(listOf(JsonPrimitive(true), JsonPrimitive(false)))),
        "Multiple default values not supported for Boolean at GET /test."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Boolean single default incorrect value throws IllegalArgumentException",
        Schema.boolean.copy(default = JsonPrimitive("no-bool")),
        "Default value \"no-bool\" at GET /test is not a Boolean."
    )

    verifyFails<IllegalStateException>(
        "Null",
        Schema(type = Basic.Null),
        "Null  should always be resolved to result in nullable types. Please report this bug. {\"type\":\"null\"}"
    )
}
