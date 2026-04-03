package io.github.nomisrev.openapi.pipeline.plugins.typed

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Eq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.verifyAll
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type.Basic
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.reference
import io.github.nomisrev.verifyFails
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import io.github.nomisrev.openapi.registry.ResolvedSchema

@OptIn(ExperimentalAtomicApi::class)
val PrimitivePluginSpec by testSuite {
    val engine = SchemaTransformerEngine.default()
    val root = NamingContext.path("root")

    verifyAll("Primitive types (Plugin)", Model.Primitive.String.all()) { schema, expected ->
        Registry(api).use { reg ->
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            Eq(expected, actual)
        }
    }

    verifyAll("Referenced primitives (Plugin)", Model.Primitive.all()) { schema, inner ->
        val refName = schema.toString()
        Registry(api.reference(refName, schema)).use { reg ->
            val head = NamingContext.Reference(refName, SchemaContext.Null)
            val resolved = ResolvedSchema.Reference(head, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            val expected = Model.Object.value(
                NamingContext.reference(refName, SchemaContext.Null).head as NamingContext.Reference,
                inner,
                isScalarWrapper = true,
            )
            Eq(expected, actual)
        }
    }

    /*
    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Number multiple default values throws IllegalArgumentException (Plugin)",
        Schema.number.copy(default = ExampleValue.Multiple(listOf("1.3", "2.5"))),
        "Default value 1.3, 2.5 is not a Number."
    )

    verifyFails<IllegalArgumentException>(
        "Schema.Type.Basic.Number single default incorrect value throws IllegalArgumentException (Plugin)",
        Schema.number.copy(default = ExampleValue.Single("no-number")),
        "Default value no-number is not a Number."
    )
    */
}
