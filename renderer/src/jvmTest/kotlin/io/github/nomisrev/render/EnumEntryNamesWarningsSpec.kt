package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.renderWithDiagnostics
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertTrue

val enumEntryNamesWarningsSpec by testSuite {
    val spelled = "so the symbols of this value are spelled out."
    val numbered = "an earlier value has the same Kotlin name, so the first free number from 2 is appended."

    test("an enum entry that is not named after its value is reported, with the enum, the wire value and the entry") {
        val result = OpenAPI.fromJson(enumEntryNamesJson).toApiTree().renderWithDiagnostics(
            RenderConfig(modelPackage = "test.model", apiPackage = "test.api", targets = setOf(KmpTarget.JVM))
        )

        assertEquals(
            listOf(
                "Enum Casing: value 'foo' is generated as entry 'Foo2': $numbered",
                "Enum Encryption: value 'aws-kms' is generated as entry 'AwsMinusKms': " +
                    "another value has the same Kotlin name ('AwsKms'), $spelled",
                "Enum Encryption: value 'aws:kms' is generated as entry 'AwsColonKms': " +
                    "another value has the same Kotlin name ('AwsKms'), $spelled",
                "Enum Items.Sort: value '-digest' is generated as entry 'MinusDigest': " +
                    "another value has the same Kotlin name ('Digest'), $spelled",
                "Enum Ordering: value '-last_activity' is generated as entry 'MinusLastActivity': " +
                    "another value has the same Kotlin name ('LastActivity'), $spelled",
                "Enum of server variable 'kind' in 'https://{kind}.example.com': value 'a-b' is generated as entry " +
                    "'AMinusB': another value has the same Kotlin name ('AB'), $spelled",
                "Enum of server variable 'kind' in 'https://{kind}.example.com': value 'a:b' is generated as entry " +
                    "'AColonB': another value has the same Kotlin name ('AB'), $spelled",
            ),
            result.diagnostics.map { it.message }.filter { it.contains("generated as entry") }.sorted(),
        )
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
    }

    test("an enum whose values keep their names or that compiles today is not reported") {
        val result = OpenAPI.fromJson(
            """
            {
              "openapi": "3.1.0",
              "info": { "title": "Api", "version": "0.0.1" },
              "paths": { "/a": { "get": { "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Mixed" } } } } } } } },
              "components": { "schemas": { "Mixed": { "type": "string", "enum": ["<", "<=", "", " ", "+1", "-1", "a-b", "aws:kms"] } } }
            }
            """.trimIndent()
        ).toApiTree().renderWithDiagnostics(
            RenderConfig(modelPackage = "test.model", apiPackage = "test.api", targets = setOf(KmpTarget.JVM))
        )

        assertEquals(emptyList(), result.diagnostics.map { it.message }.filter { it.contains("generated as entry") })
    }
}

private val enumEntryNamesJson = """
{
  "openapi": "3.1.0",
  "info": { "title": "Api", "version": "0.0.1" },
  "servers": [
    { "url": "https://{kind}.example.com", "variables": { "kind": { "enum": ["a:b", "a-b"], "default": "a-b" } } }
  ],
  "paths": {
    "/items": {
      "get": {
        "operationId": "listItems",
        "parameters": [
          { "name": "sort", "in": "query", "schema": { "type": "string", "enum": ["digest", "-digest"] } }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": { "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Listing" } } }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "Ordering": { "type": "string", "enum": ["last_activity", "-last_activity"] },
      "Encryption": { "type": "string", "enum": ["aws:kms", "aws-kms"] },
      "Casing": { "type": "string", "enum": ["Foo", "foo"] },
      "Listing": {
        "type": "object",
        "properties": {
          "ordering": { "${'$'}ref": "#/components/schemas/Ordering" },
          "encryption": { "${'$'}ref": "#/components/schemas/Encryption" },
          "casing": { "${'$'}ref": "#/components/schemas/Casing" }
        }
      }
    }
  }
}
""".trimIndent()
