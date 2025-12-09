package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Eq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.expect
import io.github.nomisrev.verifyAll
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type.Basic
import io.github.nomisrev.openapi.transformers.primitive
import io.github.nomisrev.openapi.toModel
import io.github.nomisrev.reference
import io.github.nomisrev.verifyFails
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalAtomicApi::class)
val PrimitiveSpec by testSuite {
    val name = NamingContext.RouteParam("value", "getBy")

    verifyAll("Primitive types", Model.Primitive.String.all())

    verifyAll("Referenced primitives", Model.Primitive.all()) { schema, inner ->
        val context = NamingContext.Reference(schema.toString(), null)
        val actual = with(Registry(api.reference(schema.toString(), schema))) {
            ReferenceOr.schema(schema.toString()).toModel(context, SchemaContext.Input)
        }
        val expected = Model.Object.value(context, inner)
        Eq(expected, actual)
    }

    context(scope: Registry.Scope)
    suspend fun Schema.primitive(): Model = Value(name, this).toModel(SchemaContext.Input)

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Number multiple default values throws IllegalArgumentException",
        Schema.number.copy(default = ExampleValue.Multiple(listOf("1.3", "2.5"))),
        "Multiple default values not supported for Number."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Number single default incorrect value throws IllegalArgumentException",
        Schema.number.copy(default = ExampleValue.Single("no-number")),
        "Default value no-number is not a Number."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Integer multiple default values throws IllegalArgumentException",
        Schema.integer.copy(default = ExampleValue.Multiple(listOf("1", "2"))),
        "Multiple default values not supported for Integer."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Integer single default incorrect value throws IllegalArgumentException",
        Schema.integer.copy(default = ExampleValue.Single("no-int")),
        "Default value no-int is not a Integer."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Boolean multiple default values throws IllegalArgumentException",
        Schema.boolean.copy(default = ExampleValue.Multiple(listOf("true", "false"))),
        "Multiple default values not supported for Boolean."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Boolean single default incorrect value throws IllegalArgumentException",
        Schema.boolean.copy(default = ExampleValue.Single("no-bool")),
        "Default value no-bool is not a Boolean."
    )

    verifyFails<IllegalStateException>(
        "Null",
        Schema(type = Basic.Null),
        "Null  should always be resolved to result in nullable types. Please report this bug. {\"type\":\"null\"}"
    )
}
