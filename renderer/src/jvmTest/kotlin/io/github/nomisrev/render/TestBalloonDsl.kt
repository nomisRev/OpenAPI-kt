package io.github.nomisrev.render

import com.github.difflib.DiffUtils
import com.github.difflib.UnifiedDiffUtils
import com.squareup.kotlinpoet.FileSpec
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClient
import io.github.nomisrev.openapi.generateModels
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import java.nio.file.Files
import java.nio.file.Path
import org.intellij.lang.annotations.Language
import kotlin.test.assertTrue

private const val UNIFIED_DIFF_CONTEXT_LINES = 3
private const val MAX_DIFF_LINES_PER_FILE = 200
private const val GOLDEN_UPDATE_HINT =
    "Re-run with -PupdateGolden=true or UPDATE_GOLDEN=true to update renderer golden files."

private val UPDATE_GOLDEN: Boolean = listOfNotNull(
    System.getProperty("updateGolden"),
    System.getenv("UPDATE_GOLDEN")
).any { it.toBooleanStrictOrNull() == true }

private val TEST_RENDER_CONFIG = RenderConfig(
    modelPackage = "io.github.nomisrev.render.test.model",
    apiPackage = "io.github.nomisrev.render.test.api",
    targets = setOf(KmpTarget.JVM),
)

private fun readGoldenFiles(outputDir: Path, dir: String): Map<String, String> {
    require(Files.isDirectory(outputDir)) {
        "Golden directory not found for '$dir': $outputDir. $GOLDEN_UPDATE_HINT"
    }
    return Files.newDirectoryStream(outputDir) { path ->
        Files.isRegularFile(path) && path.fileName.toString().endsWith(".kt")
    }.use { entries ->
        entries.asSequence()
            .sortedBy { it.fileName.toString() }
            .associate { file ->
                file.fileName.toString().removeSuffix(".kt") to Files.readString(file)
            }
    }
}

private fun String.toDiffInputLines(): List<String> {
    if (isEmpty()) return emptyList()
    val normalized = replace("\r\n", "\n").replace('\r', '\n')
    val lines = mutableListOf<String>()
    var start = 0
    normalized.forEachIndexed { index, c ->
        if (c == '\n') {
            lines += normalized.substring(start, index)
            start = index + 1
        }
    }
    if (start <= normalized.length) {
        lines += normalized.substring(start)
    }
    return lines
}

private fun renderUnifiedDiff(dir: String, fileName: String, expected: String, actual: String): List<String> {
    val expectedPath = "expected/$dir/$fileName.kt"
    val actualPath = "actual/$dir/$fileName.kt"
    val expectedLines = expected.toDiffInputLines()
    val actualLines = actual.toDiffInputLines()
    val patch = DiffUtils.diff(expectedLines, actualLines)
    val unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(
        expectedPath,
        actualPath,
        expectedLines,
        patch,
        UNIFIED_DIFF_CONTEXT_LINES
    )
    if (unifiedDiff.size <= MAX_DIFF_LINES_PER_FILE) return unifiedDiff
    val omitted = unifiedDiff.size - MAX_DIFF_LINES_PER_FILE
    return unifiedDiff.take(MAX_DIFF_LINES_PER_FILE) +
        "... (diff truncated: $omitted more lines, max $MAX_DIFF_LINES_PER_FILE)"
}

private fun formatFileList(files: List<String>): String =
    if (files.isEmpty()) "-" else files.joinToString(", ") { "$it.kt" }

private fun FileSpec.renderKey(): String =
    if (packageName.isBlank()) "$name.kt" else "$packageName.$name.kt"

internal fun renderGeneratedFiles(files: List<FileSpec>): Map<String, String> {
    val duplicates = files.groupBy(FileSpec::renderKey)
        .filterValues { generated -> generated.size > 1 }
        .keys
        .sorted()
    check(duplicates.isEmpty()) {
        "Renderer generated duplicate files: ${duplicates.joinToString()}"
    }
    return files.associate { file ->
        file.name to buildString { file.writeTo(this) }
    }
}

private fun assertGoldenMatches(
    dir: String,
    expected: Map<String, String>,
    actual: Map<String, String>
) {
    val missing = (expected.keys - actual.keys).sorted()
    val unexpected = (actual.keys - expected.keys).sorted()
    val changed = (expected.keys intersect actual.keys)
        .asSequence()
        .filter { expected.getValue(it) != actual.getValue(it) }
        .sorted()
        .toList()

    if (missing.isEmpty() && unexpected.isEmpty() && changed.isEmpty()) return

    val message = buildString {
        appendLine("[renderer golden mismatch] dir=$dir")
        appendLine("missing expected files (${missing.size}): ${formatFileList(missing)}")
        appendLine("unexpected generated files (${unexpected.size}): ${formatFileList(unexpected)}")
        appendLine("changed files (${changed.size}): ${formatFileList(changed)}")
        if (changed.isNotEmpty()) {
            appendLine()
            changed.forEachIndexed { index, fileName ->
                renderUnifiedDiff(
                    dir = dir,
                    fileName = fileName,
                    expected = expected.getValue(fileName),
                    actual = actual.getValue(fileName)
                ).forEach(::appendLine)
                if (index != changed.lastIndex) appendLine()
            }
        }
        appendLine()
        appendLine(GOLDEN_UPDATE_HINT)
    }
    throw AssertionError(message)
}

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
    ) { apiTree, config -> apiTree.generateModels(config) }
}

@TestRegistering
fun TestSuite.clientTest(@Language("json") json: String, dir: String): Unit =
    renderSpec(json, dir) { apiTree, config -> apiTree.generateClient(config) }

@TestRegistering
fun TestSuite.renderSpec(
    @Language("json") json: String,
    dir: String,
    generate: suspend (ApiTree, RenderConfig) -> List<FileSpec> = { apiTree, config ->
        apiTree.generateModels(config) + apiTree.generateClient(config)
    },
) = test(json) {
    val config = TEST_RENDER_CONFIG.copy(
        modelPackage = "io.github.nomisrev.render.test.${dir.replace('/', '.').replace('-', '.')}",
        apiPackage = "io.github.nomisrev.render.test.${dir.replace('/', '.').replace('-', '.')}",
    )
    val apiTree = OpenAPI.fromJson(json).toApiTree()
    val files = generate(apiTree, config).sortedWith(compareBy<FileSpec> { it.packageName }.thenBy { it.name })
    assertTrue(files.isNotEmpty(), "Expected renderer to generate files, but it returned an empty result.")
    val actual = renderGeneratedFiles(files)
    val outputDir = Path.of("src/jvmTest/resources/kotlinTestData", dir)
    if (UPDATE_GOLDEN) {
        Files.createDirectories(outputDir)
        actual.forEach { (name, content) ->
            Files.writeString(outputDir.resolve("$name.kt"), content)
        }
        return@test
    }
    val expected = readGoldenFiles(outputDir, dir)
    assertGoldenMatches(dir, expected, actual)
}

private fun extractTopLevelSchemaName(json: String): String =
    requireNotNull(Regex("\"([^\"]+)\"\\s*:").find(json)?.groupValues?.get(1)) {
        "Unable to infer top-level schema name from model test input: $json"
    }
