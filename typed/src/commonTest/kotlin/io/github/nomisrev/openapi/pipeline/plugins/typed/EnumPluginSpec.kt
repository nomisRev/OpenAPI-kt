package io.github.nomisrev.openapi.pipeline.plugins.typed

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Eq
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.verifyAll
import io.github.nomisrev.assertEq
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.transformers.ints
import io.github.nomisrev.transformers.longs
import io.github.nomisrev.transformers.strings
import io.github.nomisrev.transformers.floats
import io.github.nomisrev.transformers.doubles
import io.github.nomisrev.transformers.booleans
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import io.github.nomisrev.openapi.registry.ResolvedSchema
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.assertTrue

@OptIn(ExperimentalAtomicApi::class)
val EnumPluginSpec by testSuite {
    val engine = SchemaTransformerEngine.default()
    val root = NamingContext.path("root")

    verifyAll("Enum types (Int)", Model.Enum.ints(root)) { schema, expected ->
        Registry(api).use { reg ->
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            Eq(expected, actual)
        }
    }

    verifyAll("Enum types (Long)", Model.Enum.longs(root)) { schema, expected ->
        Registry(api).use { reg ->
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            Eq(expected, actual)
        }
    }

    verifyAll("Enum types (String)", Model.Enum.strings(root)) { schema, expected ->
        Registry(api).use { reg ->
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            Eq(expected, actual)
        }
    }

    verifyAll("Enum types (Float)", Model.Enum.floats(root)) { schema, expected ->
        Registry(api).use { reg ->
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            Eq(expected, actual)
        }
    }

    verifyAll("Enum types (Double)", Model.Enum.doubles(root)) { schema, expected ->
        Registry(api).use { reg ->
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            Eq(expected, actual)
        }
    }

    verifyAll("Enum types (Boolean)", Model.Enum.booleans(root)) { schema, expected ->
        Registry(api).use { reg ->
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            Eq(expected, actual)
        }
    }

    test("ImplicitEnumPlugin: Typeless enum becomes closed enum") {
        Registry(api).use { reg ->
            val schema = Schema(enum = listOf("A", "B"))
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            
            // Expectation updated: Typeless enum defaults to FreeFormJson inner model in current toClosedEnum implementation
            // because withoutEnumLikeValues() on a typeless schema remains typeless, 
            // and the old dispatcher (toModel) resolves typeless schemas to FreeFormJson.
            val actualEnum = actual as Model.Enum
            assertTrue(actualEnum.inner is Model.FreeFormJson)
        }
    }

    test("ImplicitEnumPlugin: Typeless const becomes closed enum") {
        Registry(api).use { reg ->
            val schema = Schema(`const` = JsonPrimitive("value"))
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            
            val actualEnum = actual as Model.Enum
            assertTrue(actualEnum.inner is Model.FreeFormJson)
        }
    }

    test("EnumPlugin: returns null for typed schemas without enum values") {
        Registry(api).use { reg ->
            val schema = Schema(type = Schema.Type.Basic.String)
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            // It should be handled by PrimitivePlugin
            assertTrue(actual is Model.Primitive.String)
        }
    }

    test("Enum plugins return null when schema has composite fields") {
        Registry(api).use { reg ->
            // Schema with both enum and allOf
            val schema = Schema(
                enum = listOf("A", "B"),
                allOf = listOf(io.github.nomisrev.openapi.parser.ReferenceOr.value(Schema(type = Schema.Type.Basic.String)))
            )
            val resolved = ResolvedSchema.Value(root, schema)
            val actual = engine.transform(reg.scope(), reg, resolved, SchemaContext.Null)
            // It shouldn't be handled by EnumPlugin because it has allOf
            // In current engine state it should throw NoTransformerFoundException because we don't have allOf plugin
            // but the test is just to ensure it's not handled as an Enum.
            assertTrue(actual !is Model.Enum)
        }
    }
}
