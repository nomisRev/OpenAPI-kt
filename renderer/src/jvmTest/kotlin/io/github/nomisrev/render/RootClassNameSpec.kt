package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * The root class of the client is named from the title of the document, as an identifier: a character that is not a
 * letter or a digit is a word break. A title with the name of the class of a first path segment (`files` and
 * `/files`) would be the same file as that class, so the root class is then `FilesApi`.
 * `RootClassNameRuntimeTest` creates both clients and checks a request.
 */
val rootClassNameSpec by testSuite {
    fun spec(title: String, path: String = "/scores/{id}") = """
        {
          "openapi": "3.0.3",
          "info": { "title": "$title", "version": "0.0.1" },
          "servers": [
            { "url": "https://{region}.example.com", "variables": { "region": { "default": "eu" } } }
          ],
          "paths": {
            "$path": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
    """.trimIndent()

    renderSpec(spec("CS:GO v3 Scores"), "client/root-class-name-title")
    renderSpec(spec("files", path = "/files/{id}"), "client/root-class-name-path")

    suspend fun generate(title: String, path: String = "/scores/{id}") =
        OpenAPI.fromJson(spec(title, path)).toApiTree().generateClientWithDiagnostics(
            RenderConfig(modelPackage = "test.model", apiPackage = "test.api", targets = setOf(KmpTarget.JVM))
        )

    suspend fun rootOf(title: String, path: String = "/scores/{id}"): Pair<String, List<String>> {
        val result = generate(title, path)
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
        val names = result.files.map { it.name }
        assertEquals(names.size, names.map(String::lowercase).distinct().size, "Two files with one name: $names")
        val root = result.files.single { file -> "public constructor" in file.toString() || "fun close" in file.toString() }
        return root.name to result.diagnostics.map { it.message }
    }

    test("a title that is a Kotlin name is the root class as it is") {
        assertEquals("PetStore" to emptyList(), rootOf("Pet Store"))
        assertEquals("PetStoreApi" to emptyList(), rootOf("pet-store_api"))
    }

    test("a title with characters that are not letters or digits is named by its words, with a warning") {
        assertEquals(
            "CSGOV3Scores" to listOf("The client class is generated as 'CSGOV3Scores': the title 'CS:GOV3Scores' is not a Kotlin name."),
            rootOf("CS:GO v3 Scores"),
        )
        assertEquals("GeodesystemsCom443", rootOf("geodesystems.com:443").first)
        assertEquals(
            "ListenAPIPodcastSearchDirectoryAndInsightsAPI",
            rootOf("Listen API: Podcast Search, Directory, and Insights API").first,
        )
    }

    test("a title without letters or digits, or that starts with a digit, is named Api") {
        assertEquals(
            "Api" to listOf("The client class is generated as 'Api': the document has no title to name it from."),
            rootOf(""),
        )
        assertEquals("Api", rootOf("***").first)
        assertEquals("Api1PasswordConnect", rootOf("1Password Connect").first)
    }

    test("a title with the name of a first path segment is named with Api after it, with a warning") {
        assertEquals(
            "FilesApi" to listOf(
                "The client class is generated as 'FilesApi': a first path segment is generated as the class " +
                    "'Files', which would be the same file."
            ),
            rootOf("files", path = "/files/{id}"),
        )
        // Names that differ only by case are one file on a case-insensitive file system.
        assertEquals("APODApi", rootOf("APOD", path = "/apod/{id}").first)
        assertEquals("FilesApiApi", rootOf("files api", path = "/files-api/{id}").first)
    }
}
