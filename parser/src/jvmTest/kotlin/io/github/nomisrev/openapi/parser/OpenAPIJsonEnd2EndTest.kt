package io.github.nomisrev.openapi.parser

import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.fail

class OpenAPIJsonEnd2EndTest {
    @Test
    fun petstoreJson() {
        OpenAPI.fromJson(resourceText("petstore.json"))
    }

    @Test
    // TODO Has default response, without default key??
//    @Ignore
    fun petstoreJsonIsomorphic() {
        val openAPI = OpenAPI.fromJson(resourceText("petstore.json"))
        val json = Json.encodeToString(OpenAPI.serializer(), openAPI)
        assertEquals(openAPI, OpenAPI.fromJson(json))
    }

    @Test
    fun petStoreMore() {
        OpenAPI.fromJson(resourceText("petstore_more.json"))
    }

    @Test
    fun issue1801() {
        OpenAPI.fromJson(resourceText("issue-1801.json"))
    }

    @Test
    fun issue1975() {
        OpenAPI.fromJson(resourceText("issue-1975.json"))
    }

    @Test
    fun oas31() {
        OpenAPI.fromJson(resourceText("oas_3_1_0.json"))
    }

    @Test
    fun basic() {
        OpenAPI.fromJson(resourceText("basic.json"))
    }

    @Test
    fun schemaSiblings() {
        OpenAPI.fromJson(resourceText("schemaSiblings.json"))
    }

    @Test
    fun securitySchemes() {
        OpenAPI.fromJson(resourceText("securitySchemes_3_1_0.json"))
    }
}

fun resourceText(path: String): String {
    val candidates =
        listOf(
            Path("test-specs/$path"), // when working directory is the repository root
            Path("../test-specs/$path"), // when working directory is the module directory
        )
    val path =
        candidates.firstOrNull { it.exists() }
            ?: fail("Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}")

    return path.readText()
}
