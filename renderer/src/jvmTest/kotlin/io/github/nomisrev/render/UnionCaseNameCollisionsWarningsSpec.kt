package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.renderWithDiagnostics
import io.github.nomisrev.openapi.toApiTree
import io.ktor.http.HttpMethod
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val config = RenderConfig(modelPackage = "test.model", apiPackage = "test.api", targets = setOf(KmpTarget.JVM))

val unionCaseNameCollisionsWarningsSpec by testSuite {
    test("cases that are numbered by position and a constraint-only oneOf are reported") {
        val result = OpenAPI.fromJson(
            $$"""
            {
              "openapi": "3.0.3",
              "info": { "title": "Api", "version": "1" },
              "paths": { "/a": { "get": { "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Holder" } } } } } } } },
              "components": { "schemas": {
                "Holder": {
                  "type": "object",
                  "properties": {
                    "contact": { "$ref": "#/components/schemas/Contact" },
                    "positional": { "oneOf": [
                      { "type": "object", "properties": { "value": { "type": "string" } } },
                      { "type": "object", "properties": { "value": { "type": "integer", "format": "int32" } } } ] },
                    "result": { "oneOf": [
                      { "type": "object", "properties": { "status": { "type": "string", "enum": ["succeeded"] } } },
                      { "type": "object", "properties": { "status": { "type": "string", "enum": ["failed", "rejected"] } } } ] },
                    "files": { "type": "object", "additionalProperties": {
                      "type": "object", "nullable": true,
                      "properties": { "content": { "type": "string" }, "filename": { "type": "string" } },
                      "anyOf": [ { "required": ["content"] }, { "required": ["filename"] }, { "type": "object", "maxProperties": 0 } ] } }
                  }
                },
                "Contact": {
                  "type": "object",
                  "properties": { "email": { "type": "string" }, "phone": { "type": "string" } },
                  "oneOf": [ { "required": ["email"] }, { "required": ["phone"] } ]
                }
              } }
            }
            """.trimIndent()
        ).toApiTree().renderWithDiagnostics(config)

        val required = "which is a constraint on the object, not alternative types. The type has all declared " +
            "properties; \"at least one of\" / \"one of these property sets\" is not enforced by the generated type."
        assertEquals(
            listOf(
                "Schema Contact: its `oneOf` only lists required properties ([email] | [phone]), $required",
                "Schema Holder.files: its `anyOf` only lists required properties " +
                    "([content] | [filename] | []), $required",
                "Union Holder.positional: the cases at positions 1 and 2 are different object shapes that would " +
                    "all be named 'Value', and nothing in the schema tells them apart (no `title`, no property " +
                    "with an enum of its own values). They are named Value1, Value2 by their position.",
            ),
            result.diagnostics.map { it.message }.sorted(),
        )
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
    }

    test("a union that is already told apart, or merged, is not reported") {
        val result = OpenAPI.fromJson(
            $$"""
            {
              "openapi": "3.0.3",
              "info": { "title": "Api", "version": "1" },
              "paths": { "/a": { "get": { "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Holder" } } } } } } } },
              "components": { "schemas": {
                "Holder": { "type": "object", "properties": {
                  "id": { "anyOf": [ { "type": "string", "pattern": "^a$" }, { "type": "string", "pattern": "^b$" } ] },
                  "titled": { "oneOf": [
                    { "title": "A", "type": "object", "properties": { "v": { "type": "string" } } },
                    { "title": "B", "type": "object", "properties": { "v": { "type": "integer" } } } ] }
                } }
              } }
            }
            """.trimIndent()
        ).toApiTree().renderWithDiagnostics(config)

        assertEquals(emptyList(), result.diagnostics.map { it.message })
    }

    test("two cases of one class name that reach the renderer are numbered, with a warning") {
        fun string() = Model.Primitive.String(null, null, null, false, null)
        val union = Model.OneOf(
            context = NamingContext.reference("Id", io.github.nomisrev.openapi.routes.SchemaContext.Null),
            cases = listOf(string(), string(), Model.Primitive.Int(null, null, null, false, null))
                .map { Model.Union.Case(it, emptySet()) },
            default = null,
            description = null,
            title = null,
            dispatch = UnionDispatch.Structural,
            isNullable = false,
        )
        val tree = ApiTree(name = "Api", operations = emptyMap<HttpMethod, io.github.nomisrev.openapi.routes.Route>(), children = emptyList(), models = listOf(union))

        val result = tree.renderWithDiagnostics(config)

        val code = result.files.joinToString("\n") { it.toString() }
        assertTrue("class CaseString(" in code && "class CaseString2(" in code && "class CaseInt(" in code, code)
        assertEquals(
            listOf(
                "Union Id: two cases would be named 'CaseString', so one is named 'CaseString2'. " +
                    "The cases are different on the wire; report this schema."
            ),
            result.diagnostics.map { it.message },
        )
    }
}
