package io.github.nomisrev

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Registry
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry
import kotlinx.serialization.json.Json
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.fail

class Eq<A>(val expected: A, val actual: A)

private val ModelJson = Json {
    prettyPrint = false
    encodeDefaults = false
    explicitNulls = false
}

@TestRegistering
context(suite: TestSuite)
fun <A, B> List<A>.checkAll(
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

@OptIn(ExperimentalAtomicApi::class)
@TestRegistering
fun TestSuite.checkAll(
    name: String,
    values: List<Expect<Schema, Model>>,
    actual: suspend context(Registry) (Schema) -> Model
) {
    test(name) {
        val res = buildString {
            for ((schema, model) in values) {
                val actual = registry(api) { actual(schema) }
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
fun TestSuite.checkAll(
    name: String,
    values: List<Expect<Schema, Model>>,
    actual: suspend context(Registry) (Schema, Model) -> Eq<Model>
) {
    test(name) {
        val res = buildString {
            for ((schema, model) in values) {
                val eq = registry(api) { actual(schema, model) }
                if (eq.actual != eq.expected) {
                    appendLine(
                        """
                    Original: $schema
                    Actual: ${ModelJson.encodeToString(Model.serializer(), eq.actual)}
                    Expected: ${ModelJson.encodeToString(Model.serializer(), model)}
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
    if (expected != actual) fail("""
    Actual: ${ModelJson.encodeToString(Model.serializer(), actual)}
    Expected: ${ModelJson.encodeToString(Model.serializer(), expected)}
    """.trimIndent()) else Model.Primitive.Unit

