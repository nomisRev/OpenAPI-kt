package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.renderWithDiagnostics
import io.github.nomisrev.openapi.toApiTree
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Real-world specs from the parser's corpus (`parser/src/commonTest/resources/specs`) that generate a client today go
 * through the whole pipeline (parse, `toApiTree`, render every file) without an error, so a change to the typed layer
 * that breaks one of them fails here rather than in a user's build. YouTrack's discriminated subtypes override the
 * `{type: object}` placeholders their parents declare (`ActivityItem.added`, `IssueCustomField.value`), which
 * 29766c84 turned into an error for 172 of its 248 operations.
 */
val corpusRenderSmokeSpec by testSuite {
    listOf("youtrack.json", "supabase.json", "fly-io.json", "openai.yaml", "github.json").forEach { spec ->
        test("$spec renders without errors") {
            val text = File("../parser/src/commonTest/resources/specs/$spec").readText().withValidDefaults()
            val api = if (spec.endsWith(".json")) OpenAPI.fromJson(text) else OpenAPI.fromYaml(text)
            val result = api.toApiTree().renderWithDiagnostics(RenderConfig())
            val errors = result.diagnostics.filter { it.severity == GenerationDiagnosticSeverity.Error }
            assertEquals(emptyList(), errors.map { it.message }, "Errors generating $spec")
            assertTrue(result.files.isNotEmpty(), "Expected files for $spec")
            result.files.forEach { file -> assertTrue(file.toString().isNotEmpty()) }
        }
    }
}

/**
 * supabase.json declares the boolean query parameter `force` with `"default": "true"`, a string where OAS 3.0 requires
 * the boolean the schema's type declares (see `issues/default-or-enum-value-not-of-declared-type.md`). `default`
 * values are decided from their JSON type, so the document is corrected here to what its author meant.
 */
private fun String.withValidDefaults(): String =
    replace("\"default\": \"true\",\n              \"type\": \"boolean\"", "\"default\": true,\n              \"type\": \"boolean\"")
