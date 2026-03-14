package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.parser.OpenAPI
import org.intellij.lang.annotations.Language
import kotlin.jvm.java
import kotlin.test.assertEquals

private fun readResourceText(path: String): String =
    requireNotNull(TestSuite::class.java.classLoader.getResource(path)?.readText()) { "Resource not found: $path" }

@TestRegistering
fun TestSuite.modelTest(json: String, dir: String) =
    renderSpec("""
        {
          "openapi": "3.1.0",
          "info": {
            "title": "Test API",
            "version": "0.0.1"
          },
          "components": {
            "schemas": {
              $json
            }
          }
        }
    """.trimIndent(), dir)


@TestRegistering
fun TestSuite.renderSpec(@Language("json") json: String, dir: String) = test(json) {
    val files = OpenAPI.fromJson(json).generate().sortedBy { it.name }
    val actual = files.associate({ file ->
        Pair(file.name, buildString { file.writeTo(this) })
    })
    val expected = files.associate {
        Pair(it.name, readResourceText("/kotlinTestData/$dir/${it.name}.kt"))
    }
    assertEquals(expected, actual)
}
