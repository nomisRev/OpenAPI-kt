package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClient
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.generateModels
import io.github.nomisrev.openapi.parser.OpenAPI
import com.squareup.kotlinpoet.TypeSpec
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
    fun spec(
        title: String,
        path: String = "/scores/{id}",
        status: String = "204",
        content: String = "",
    ) = """
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
                "responses": { "$status": { "description": "No Content"$content } }
              }
            }
          }
        }
    """.trimIndent()

    renderSpec(spec("CS:GO v3 Scores"), "client/root-class-name-title")
    renderSpec(spec("files", path = "/files/{id}"), "client/root-class-name-path")
    renderSpec(spec("Listen API: Podcast Search"), "client/root-class-name-option") { tree, config ->
        val named = config.copy(clientName = "ListenNotes")
        tree.generateModels(named) + tree.generateClient(named)
    }

    suspend fun generate(json: String, clientName: String? = null) =
        OpenAPI.fromJson(json).toApiTree().generateClientWithDiagnostics(
            RenderConfig(
                modelPackage = "test.model",
                apiPackage = "test.api",
                targets = setOf(KmpTarget.JVM),
                clientName = clientName,
            )
        )

    suspend fun rootOf(
        title: String,
        path: String = "/scores/{id}",
        clientName: String? = null,
        json: String = spec(title, path),
    ): Pair<String, List<String>> {
        val result = generate(json, clientName)
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
        val names = result.files.map { it.name }
        assertEquals(names.size, names.map(String::lowercase).distinct().size, "Two files with one name: $names")
        // Two top-level declarations of one name in a package do not compile, whichever files they are in.
        val types = result.files.flatMap { file ->
            file.members.filterIsInstance<TypeSpec>().map { "${file.packageName}.${it.name}".lowercase() }
        }
        assertEquals(types.size, types.distinct().size, "Two top-level classes with one name: $types")
        val root = result.files.single { file -> "public constructor" in file.toString() || "fun close" in file.toString() }
        return root.name to result.diagnostics.map { it.message }
    }

    suspend fun errorsOf(
        clientName: String,
        path: String = "/scores/{id}",
        json: String = spec("Pet Store", path),
    ): List<String> =
        generate(json, clientName).diagnostics
            .filter { it.severity == GenerationDiagnosticSeverity.Error }
            .map { it.message }

    test("a configured client name is the root class as it is, without a warning") {
        assertEquals("ListenNotes" to emptyList(), rootOf("Listen API: Podcast Search", clientName = "ListenNotes"))
        assertEquals("Api" to emptyList(), rootOf("", clientName = "Api"))
        assertEquals("scores_v3" to emptyList(), rootOf("Scores", clientName = "scores_v3"))
    }

    test("a configured client name that is not a Kotlin class name is an error") {
        for (name in listOf("Listen Notes", "CS:GO", "1Password", "object", "", "Pet-Store")) {
            assertEquals(
                listOf(
                    "The client name '$name' is not a Kotlin class name: use letters, digits and '_', not starting " +
                        "with a digit, and no keyword."
                ),
                errorsOf(name),
                name,
            )
        }
    }

    test("a configured client name with the name of a first path segment's class is an error") {
        val expected = listOf(
            "The client name 'Scores' cannot be used: a first path segment is generated as the class 'Scores', which " +
                "would be the same file."
        )
        assertEquals(expected, errorsOf("Scores"))
        // One file on a case-insensitive file system.
        assertEquals(
            listOf(
                "The client name 'SCORES' cannot be used: a first path segment is generated as the class 'Scores', " +
                    "which would be the same file."
            ),
            errorsOf("SCORES"),
        )
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

    test("a name whose server class is the class of a first path segment is named with Api after it") {
        // The server class (`<Root>Server`) is next to the root class, in the same package as the path classes.
        assertEquals(
            "PetsApi" to listOf(
                "The client class is generated as 'PetsApi': a first path segment is generated as the class " +
                    "'PetsServer', the name of the client's server class."
            ),
            rootOf("Pets", path = "/pets-server/{id}"),
        )
        assertEquals(
            listOf(
                "The client name 'Pets' cannot be used: a first path segment is generated as the class " +
                    "'PetsServer', the name of the client's server class."
            ),
            errorsOf("Pets", path = "/pets-server/{id}"),
        )
    }

    test("a name of a support file in the package of the client is named with Api after it") {
        val statusCodes = spec("Status Codes", status = "421")
        val withStatusCodes = generate(spec("Pet Store", status = "421")).files.map { it.name }
        assertTrue("StatusCodes" in withStatusCodes, "421 has no Ktor name, so it is in StatusCodes.kt: $withStatusCodes")
        assertEquals(
            "StatusCodesApi" to listOf(
                "The client class is generated as 'StatusCodesApi': the names of the status codes are generated in " +
                    "the file 'StatusCodes', which would be the same file."
            ),
            rootOf("Status Codes", json = statusCodes),
        )
        // Without a status code that needs the file, the title is the name as it is.
        assertEquals("StatusCodes" to emptyList(), rootOf("Status Codes"))

        fun union(title: String) = spec(
            title,
            status = "200",
            content = """, "content": { "application/json": { "schema": { "oneOf": [
                { "type": "string" }, { "type": "integer" }
            ] } } }""",
        )
        val union = union("Attempt Deserialize")
        val withUnion = generate(union("Pet Store")).files.map { it.name }
        assertTrue("AttemptDeserialize" in withUnion, "An inline union response needs the helpers: $withUnion")
        assertEquals(
            "AttemptDeserializeApi" to listOf(
                "The client class is generated as 'AttemptDeserializeApi': the union serialization helpers are " +
                    "generated in the file 'AttemptDeserialize', which would be the same file."
            ),
            rootOf("Attempt Deserialize", json = union),
        )
        assertEquals(
            listOf(
                "The client name 'StatusCodes' cannot be used: the names of the status codes are generated in the " +
                    "file 'StatusCodes', which would be the same file."
            ),
            errorsOf("StatusCodes", json = statusCodes),
        )
    }
}
