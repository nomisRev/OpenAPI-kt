package io.github.nomisrev

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.ResolvedSchema
import io.github.nomisrev.openapi.SchemaContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.toModel
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
        val res = fold(StringBuilder()) { acc, value ->
            val eq = check(value)
            if (eq.expected != eq.actual) {
                acc.appendLine(
                    """
                    A: ${eq.actual}
                    E: ${eq.expected}
                """.trimIndent()
                )
                acc.appendLine("\n---\n")
            } else acc
        }
        if (res.isNotEmpty()) fail(res.toString())
    }
}

data class ExpectedApi(
    val schema: Schema,
    val model: Model,
    val api: OpenAPI,
    val names: List<NamingContext>
)

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
@JvmName("checkAllExpectedApi")
fun TestSuite.verifyAll(
    name: String,
    values: List<ExpectedApi>,
    context: SchemaContext = Input,
    actual: suspend (Schema) -> ResolvedSchema
) {
    test(name) {
        val res = buildString {
            for (expected in values) {
                val registry = Registry(expected.api)
                val actual = try {
                    val resolved = actual(expected.schema)
                    with(registry) {
                        when (resolved) {
                            is ResolvedSchema.Reference -> ReferenceOr.schema(resolved.name.name)
                            is ResolvedSchema.Value -> ReferenceOr.value(resolved.schema)
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
                    appendLine(
                        """
                    O: ${expected.schema}
                    A: ${ModelJson.encodeToString(Model.serializer(), actual)}
                    E: ${ModelJson.encodeToString(Model.serializer(), expected.model)}
                """.trimIndent()
                    )
                    appendLine("\n---\n")
                }
                if (!registry.names().containsAll(expected.names)) {
                    val missing = expected.names - registry.names()
                    throw AssertionError("{$missing} are missing from registry. ${registry.names()}")
                }
            }
        }
        if (res.isNotEmpty()) fail(res)
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
            ReferenceOr.value(schema).toModel(NamingContext.ObjectProperty("test"), Input)
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
    context: SchemaContext = Input,
    actual: suspend (Schema) -> ResolvedSchema =
        { schema -> ResolvedSchema.Value(NamingContext.ObjectProperty("test"), schema) }
) {
    test(name) {
        val registry = Registry(api)
        val res = buildString {
            for ((schema, model) in values) {
                val actual = try {
                    with(registry) {
                        val resolved = actual(schema)
                        when (resolved) {
                            is ResolvedSchema.Reference -> ReferenceOr.schema("#/components/schema/${resolved.name}")
                            is ResolvedSchema.Value -> ReferenceOr.value(resolved.schema)
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
                    appendLine(
                        """
                    O: $schema
                    A: ${ModelJson.encodeToString(Model.serializer(), actual)}
                    E: ${ModelJson.encodeToString(Model.serializer(), model)}
                """.trimIndent()
                    )
                    appendLine("\n---\n")
                }
            }
        }
        if (res.isNotEmpty()) fail(res)
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
        val res = buildString {
            for ((schema, model) in values) {
                val eq = with(registry) { actual(schema, model) }
                if (eq.actual != eq.expected) {
                    appendLine(
                        """
                    Original: $schema
                    Actual: ${ModelJson.encodeToString(Model.serializer(), eq.actual)}
                    Expected: ${ModelJson.encodeToString(Model.serializer(), eq.expected)}
                """.trimIndent()
                    )
                    appendLine("\n---\n")
                }
            }
        }
        if (res.isNotEmpty()) fail(res)
    }
}


fun assertEq(expected: Model, actual: Model) =
    if (expected != actual) fail(
        """
    Actual: ${ModelJson.encodeToString(Model.serializer(), actual)}
    Expected: ${ModelJson.encodeToString(Model.serializer(), expected)}
    """.trimIndent()
    ) else Model.Primitive.Unit

