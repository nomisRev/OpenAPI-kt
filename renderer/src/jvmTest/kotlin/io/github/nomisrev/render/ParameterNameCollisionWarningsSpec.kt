package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.spellSymbolsOrNull
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

val parameterNameCollisionWarningsSpec by testSuite {
    test("every argument that is not named after its wire name is reported, with the operation, wire name and location") {
        val result = OpenAPI.fromJson(parameterNameCollisionJson).toApiTree().generateClientWithDiagnostics(
            RenderConfig(modelPackage = "test", apiPackage = "test", targets = setOf(KmpTarget.JVM))
        )

        val bodyPrefix = "a parameter has the same Kotlin name, so the request body's prefix is added."
        val spelled = "another parameter has the same Kotlin name, so the symbols of this one are spelled out."
        val location =
            "another parameter in a different location has the same Kotlin name, so the location is appended."
        val numbered = "an earlier argument has the same Kotlin name, so the first free number from 2 is appended."
        val generated = "another argument has the name the generated code gives it."
        assertEquals(
            listOf(
                "PUT /albums: request body property 'ids' is generated as argument 'bodyIds': $bodyPrefix",
                "POST /forms: request body property 'title' is generated as argument 'bodyTitle': $bodyPrefix",
                "POST /uploads: request body property 'name' is generated as argument 'bodyName': $bodyPrefix",
                "GET /prices: parameter 'ValidUntilDate<' (in query) is generated as argument 'validUntilDateLt': $spelled",
                "GET /prices: parameter 'ValidUntilDate>' (in query) is generated as argument 'validUntilDateGt': $spelled",
                "GET /prices: parameter 'amount>=' (in query) is generated as argument 'amountGte': $spelled",
                "GET /prices: parameter 'amount<=' (in query) is generated as argument 'amountLte': $spelled",
                "GET /prices: parameter 'status!' (in query) is generated as argument 'statusNot': $spelled",
                "GET /segments: parameter 'x[]' (in query) is generated as argument 'xList': $spelled",
                "GET /things/{id}: parameter 'id' (in query) is generated as argument 'idQuery': $location",
                "GET /things/{id}: parameter 'id' (in header) is generated as argument 'idHeader': $location",
                "GET /things/{id}: parameter 'id' (in cookie) is generated as argument 'idCookie': $location",
                "GET /limits: parameter 'MaxResults' (in query) is generated as argument 'maxResults2': $numbered",
                "GET /limits: parameter 'max_results' (in query) is generated as argument 'maxResults3': $numbered",
                "POST /variants: the request type is generated as argument 'bodyRequestType': $generated",
                "POST /echo: the request body is generated as argument 'requestBody': $generated",
            ).sorted(),
            result.diagnostics.map { it.message }.sorted(),
        )
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
    }

    test("an operation whose inputs have names of their own is not reported") {
        val result = OpenAPI.fromJson(
            """
            {
              "openapi": "3.1.0",
              "info": { "title": "Api", "version": "0.0.1" },
              "paths": {
                "/items/{id}": {
                  "post": {
                    "parameters": [
                      { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } },
                      { "name": "id", "in": "query", "schema": { "type": "string" } }
                    ],
                    "requestBody": {
                      "content": {
                        "application/json": {
                          "schema": { "type": "object", "properties": { "name": { "type": "string" } } }
                        }
                      }
                    },
                    "responses": { "204": { "description": "No Content" } }
                  }
                }
              }
            }
            """.trimIndent()
        ).toApiTree().generateClientWithDiagnostics(
            RenderConfig(modelPackage = "test", apiPackage = "test", targets = setOf(KmpTarget.JVM))
        )

        assertEquals(emptyList(), result.diagnostics)
    }

    test("the symbols that are spelled out") {
        assertEquals("ValidUntilDate Lt ", "ValidUntilDate<".spellSymbolsOrNull())
        assertEquals("a Gte ", "a>=".spellSymbolsOrNull())
        assertEquals("a Lte ", "a<=".spellSymbolsOrNull())
        assertEquals("x List ", "x[]".spellSymbolsOrNull())
        assertEquals(" Not status", "!status".spellSymbolsOrNull())
        assertNull("filter[name]".spellSymbolsOrNull())
        assertNull("maxResults".spellSymbolsOrNull())
    }
}
