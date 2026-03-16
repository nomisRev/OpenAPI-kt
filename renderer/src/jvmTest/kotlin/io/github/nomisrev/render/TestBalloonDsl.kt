package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.parser.OpenAPI
import org.intellij.lang.annotations.Language
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Path
import kotlin.jvm.java
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun readResourceText(path: String): String =
    requireNotNull(TestSuite::class.java.classLoader.getResource(path.removePrefix("/"))?.readText()) {
        "Resource not found: $path"
    }

private val updateGolden: Boolean = listOfNotNull(
    System.getProperty("updateGolden"),
    System.getenv("UPDATE_GOLDEN")
).any { it.toBooleanStrictOrNull() == true }

private val testRenderConfig = RenderConfig(
    modelPackage = "io.github.nomisrev.render.test.model",
    apiPackage = "io.github.nomisrev.render.test.api",
    targets = setOf(KmpTarget.JVM),
)

@TestRegistering
fun TestSuite.modelTest(json: String, dir: String): Unit {
    val schemaName = extractTopLevelSchemaName(json)
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": {
            "title": "Test API",
            "version": "0.0.1"
          },
          "paths": {
            "/test": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": {
                          "$ref": "#/components/schemas/$$schemaName"
                        }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              $$json
            }
          }
        }
    """.trimIndent(),
        dir
    )
}


@TestRegistering
fun TestSuite.renderSpec(@Language("json") json: String, dir: String) = test(json) {
    val config = testRenderConfig.copy(
        modelPackage = "io.github.nomisrev.render.test.${dir.replace('/', '.').replace('-', '.')}",
    )
    val files = OpenAPI.fromJson(json).generate(config).sortedBy { it.name }
    assertTrue(files.isNotEmpty(), "Expected renderer to generate files, but it returned an empty result.")
    val actual = files.associate({ file ->
        Pair(file.name, buildString { file.writeTo(this) })
    })
    if (updateGolden) {
        val outputDir = Path.of("src/jvmTest/resources/kotlinTestData", dir)
        Files.createDirectories(outputDir)
        actual.forEach { (name, content) ->
            Files.writeString(outputDir.resolve("$name.kt"), content)
        }
        return@test
    }
    val expected = files.associate {
        Pair(it.name, readResourceText("/kotlinTestData/$dir/${it.name}.kt"))
    }
    assertEquals(expected, actual)
}

private fun extractTopLevelSchemaName(json: String): String =
    requireNotNull(Regex("\"([^\"]+)\"\\s*:").find(json)?.groupValues?.get(1)) {
        "Unable to infer top-level schema name from model test input: $json"
    }
