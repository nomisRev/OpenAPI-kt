package io.github.nomisrev

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.json.Json
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.jvm.JvmName
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail

class Eq<A>(val expected: A, val actual: A)

private val ModelJson = Json {
    prettyPrint = false
    encodeDefaults = false
    explicitNulls = false
}

@TestRegistering
context(suite: TestSuite)
fun <A, B> List<A>.verifyAll(
    name: String,
    check: suspend (A) -> Eq<B>,
) {
    suite.test(name) {
        forEach { value ->
            val eq = check(value)
            if (eq.expected != eq.actual) {
                fail(
                    """
                    A: ${eq.actual}
                    E: ${eq.expected}
                """.trimIndent()
                )
            }
        }
    }
}

data class ExpectedApi(
    val schema: Schema,
    val model: Model,
    val api: OpenAPI,
    val names: List<NamingContext.Reference>
)

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
@JvmName("checkAllExpectedApi")
fun TestSuite.verifyAll(
    name: String,
    values: List<Expect<Schema, Model>>,
    vararg names: NamingContext.Reference
) = verifyAll(name, values, Write, names.toList())

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
@JvmName("checkAllExpectedApi")
fun TestSuite.verifyAll(
    name: String,
    values: List<Expect<Schema, Model>>,
    context: SchemaContext,
    names: List<NamingContext.Reference>
) {
    test(name) {
        val refName = names.first()
        for (expected in values) {
            val registry = Registry(api.reference(refName.name, expected.actual))
            val actual = try {
                // TODO report bug about super obscure error. bug?
                context(registry) { ReferenceOr.schema(refName.name).toModel(refName, context) }
            } catch (e: Throwable) {
                println(
                    """
                        O: ${expected.actual}
                        E: ${ModelJson.encodeToString(Model.serializer(), expected.expected)}""".trimIndent()
                )
                throw e
            }
            if (actual != expected.expected) {
                fail(
                    """
                    O: ${expected.actual}
                    A: ${ModelJson.encodeToString(Model.serializer(), actual)}
                    E: ${ModelJson.encodeToString(Model.serializer(), expected.expected)}
                    
                """.trimIndent()
                )
            }
            if (!registry.names().containsAll(names)) {
                val missing = names - registry.names()
                throw AssertionError("{$missing} are missing from registry. ${registry.names()}")
            }
        }
    }
}

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
@JvmName("checkAllExpectedApi")
fun TestSuite.verifyAll(
    name: String,
    values: List<ExpectedApi>,
    context: SchemaContext = Write,
    actual: suspend (Schema) -> ResolvedSchema
) {
    test(name) {
        for (expected in values) {
            val registry = Registry(expected.api)
            val actual = try {
                val resolved = actual(expected.schema)
                with(registry) {
                    when (resolved) {
                        is ResolvedSchema.Reference -> ReferenceOr.schema(resolved.reference.name)
                        is ResolvedSchema.Value -> ReferenceOr.value(resolved.schema)
                        is ResolvedSchema.Recursive -> ReferenceOr.schema((resolved.name as NamingContext.Reference).name)
                    }.toModel(resolved.name, context)
                }
            } catch (e: Throwable) {
                println(
                    """
                        O: ${expected.schema}
                        E: ${ModelJson.encodeToString(Model.serializer(), expected.model)}""".trimIndent()
                )
                throw e
            }
            if (actual != expected.model) {
                fail(
                    """
                    O: ${expected.schema}
                    A: ${ModelJson.encodeToString(Model.serializer(), actual)}
                    E: ${ModelJson.encodeToString(Model.serializer(), expected.model)}
                    
                """.trimIndent()
                )
            }
            if (!registry.names().containsAll(expected.names)) {
                val missing = expected.names - registry.names()
                throw AssertionError("{$missing} are missing from registry. ${registry.names()}")
            }
        }
    }
}


@TestRegistering
inline fun <reified T : Throwable> TestSuite.verifyFails(
    name: String,
    schema: Schema,
    message: String
) = test(name) {
    val e = assertFailsWith<T> {
        with(Registry(api)) {
            val ignored = ReferenceOr.value(schema).toModel(NamingContext.path("test"), Write)
        }
    }
    assertEquals(message, e.message)
}

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
fun TestSuite.verify(
    name: String,
    schema: Schema,
    model: Model,
) = verifyAll(name, listOf(schema expect model))

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
fun TestSuite.verifyAll(
    name: String,
    values: List<Expect<Schema, Model>>,
    context: SchemaContext = Write,
    actual: suspend (Schema) -> ResolvedSchema =
        { schema -> ResolvedSchema.Value(NamingContext.path("test"), schema) }
) {
    test(name) {
        val registry = Registry(api)
        for ((schema, model) in values) {
            val actual = try {
                with(registry) {
                    val resolved = actual(schema)
                    when (resolved) {
                        is ResolvedSchema.Reference -> ReferenceOr.schema("#/components/schema/${resolved.reference.name}")
                        is ResolvedSchema.Value -> ReferenceOr.value(resolved.schema)
                        is ResolvedSchema.Recursive -> ReferenceOr.schema("#/components/schema/${(resolved.name as NamingContext.Reference).name}")
                    }.toModel(resolved.name, context)
                }
            } catch (e: Throwable) {
                println(
                    """
                    O: $schema
                    E: ${ModelJson.encodeToString(Model.serializer(), model)}
                """.trimIndent()
                )
                throw e
            }
            if (actual != model) {
                fail(
                    """
                    O: $schema
                    A: ${ModelJson.encodeToString(Model.serializer(), actual)}
                    E: ${ModelJson.encodeToString(Model.serializer(), model)}
                """.trimIndent()
                )
            }
        }
    }
}

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
fun TestSuite.verifyAll(
    name: String,
    values: List<Expect<Schema, Model>>,
    actual: suspend (Schema, Model) -> Eq<Model>
) {
    test(name) {
        val registry = Registry(api)
        for ((schema, model) in values) {
            val eq = with(registry) { actual(schema, model) }
            if (eq.actual != eq.expected) {
                fail(
                    """
                    Original: $schema
                    Actual: ${ModelJson.encodeToString(Model.serializer(), eq.actual)}
                    Expected: ${ModelJson.encodeToString(Model.serializer(), eq.expected)}
                    
                """.trimIndent()
                )
            }
        }
    }
}


fun assertEq(expected: Model, actual: Model) {
    if (expected != actual) fail(
        """
    A: ${ModelJson.encodeToString(Model.serializer(), actual)}
    E: ${ModelJson.encodeToString(Model.serializer(), expected)}
    """.trimIndent()
    ) else Model.Primitive.Unit
}