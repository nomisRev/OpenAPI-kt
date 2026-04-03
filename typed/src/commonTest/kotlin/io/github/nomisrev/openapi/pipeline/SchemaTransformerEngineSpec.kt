package io.github.nomisrev.openapi.pipeline

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.plugins.fallback.FallbackPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

val SchemaTransformerEngineSpec by testSuite {
    val emptyApi = OpenAPI(info = Info(title = "Test", version = "1.0.0"))

    test("default() returns engine with fallback plugin") {
        val engine = SchemaTransformerEngine.default()
        // We can't easily inspect plugins because they are private, 
        // but we can test behavior.
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            val schema = ResolvedSchema.Value(NamingContext.path("test"), Schema(title = "Test"))
            val model = engine.transform(scope, registry, schema, SchemaContext.Write)
            assertEquals("Test", (model as Model.FreeFormJson).title)
        }
    }

    test("Pre-check: Recursive -> Model.Reference") {
        val engine = SchemaTransformerEngine.build { /* empty */ }
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            val schema = ResolvedSchema.Recursive(NamingContext.path("recursive"), Schema(title = "Rec"))
            val model = engine.transform(scope, registry, schema, SchemaContext.Write)
            val head = (model as Model.Reference).context.head
            val name = if (head is NamingContext.Reference) head.name else "recursive"
            assertEquals("recursive", name)
        }
    }

    test("Pre-check: Reference with resolveReference=false -> Model.Reference") {
        val engine = SchemaTransformerEngine.build { /* empty */ }
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            val ref = NamingContext.Reference("MyRef", SchemaContext.Null)
            val schema = ResolvedSchema.Reference(ref, Schema(title = "Ref"))
            val model = engine.transform(scope, registry, schema, SchemaContext.Write, resolveReference = false)
            assertEquals("MyRef", ((model as Model.Reference).context.head as NamingContext.Reference).name)
        }
    }

    test("Pre-check: type: null -> throws error") {
        val engine = SchemaTransformerEngine.default()
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            val schema = ResolvedSchema.Value(NamingContext.path("test"), Schema(type = Schema.Type.Basic.Null))
            val e = assertFailsWith<IllegalStateException> {
                engine.transform(scope, registry, schema, SchemaContext.Write)
            }
            assertTrue(e.message!!.contains("Null  should always be resolved"))
        }
    }

    test("FallbackPlugin: produces FreeFormJson for Value") {
        val engine = SchemaTransformerEngine.build { addFirst(Phase.FALLBACK, FallbackPlugin) }
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            val schema = ResolvedSchema.Value(NamingContext.path("test"), Schema(description = io.github.nomisrev.openapi.parser.ReferenceOr.value("desc")))
            val model = engine.transform(scope, registry, schema, SchemaContext.Write) as Model.FreeFormJson
            assertEquals("desc", model.description)
        }
    }

    test("FallbackPlugin: produces wrapped Object.value for Reference") {
        val engine = SchemaTransformerEngine.build { addFirst(Phase.FALLBACK, FallbackPlugin) }
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            val ref = NamingContext.Reference("MyRef", SchemaContext.Null)
            // Schema.description is ReferenceOr<String>?, so we use ReferenceOr.value("desc")
            val schema = ResolvedSchema.Reference(ref, Schema(description = io.github.nomisrev.openapi.parser.ReferenceOr.value("desc")))
            val model = engine.transform(scope, registry, schema, SchemaContext.Write) as Model.Object
            assertEquals(ref, model.context.head as NamingContext.Reference)
            assertTrue(model.isScalarWrapper)
            assertEquals("desc", model.description)
            assertTrue(model.properties.values.first().model is Model.FreeFormJson)
        }
    }

    test("Builder: duplicate PluginKey throws") {
        assertFailsWith<IllegalArgumentException> {
            SchemaTransformerEngine.build {
                addFirst(Phase.FALLBACK, FallbackPlugin)
                addFirst(Phase.FALLBACK, FallbackPlugin)
            }
        }
    }

    test("Builder: replace on absent key throws") {
        assertFailsWith<IllegalArgumentException> {
            SchemaTransformerEngine.build {
                replace(PluginKey("missing"), FallbackPlugin)
            }
        }
    }

    test("Builder: addAfter/addBefore on absent key throws") {
        assertFailsWith<IllegalArgumentException> {
            SchemaTransformerEngine.build {
                addAfter(PluginKey("missing"), FallbackPlugin)
            }
        }
        assertFailsWith<IllegalArgumentException> {
            SchemaTransformerEngine.build {
                addBefore(PluginKey("missing"), FallbackPlugin)
            }
        }
    }

    test("ModelInterceptor chain runs in order") {
        val logs = mutableListOf<String>()
        val engine = SchemaTransformerEngine.build {
            addFirst(Phase.FALLBACK, FallbackPlugin)
            interceptor(object : ModelInterceptor {
                context(ctx: Registry)
                override suspend fun intercept(model: Model, context: SchemaContext): Model {
                    logs.add("first")
                    return model
                }
            })
            interceptor(object : ModelInterceptor {
                context(ctx: Registry)
                override suspend fun intercept(model: Model, context: SchemaContext): Model {
                    logs.add("second")
                    return model
                }
            })
        }
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            engine.transform(scope, registry, ResolvedSchema.Value(NamingContext.path("test"), Schema()), SchemaContext.Write)
        }
        assertEquals(listOf("first", "second"), logs)
    }

    test("NoTransformerFoundException thrown when chain exhausts") {
        val engine = SchemaTransformerEngine.build { /* empty */ }
        Registry(emptyApi).use { registry ->
            val scope = registry.scope()
            assertFailsWith<NoTransformerFoundException> {
                engine.transform(scope, registry, ResolvedSchema.Value(NamingContext.path("test"), Schema()), SchemaContext.Write)
            }
        }
    }
}
