package io.github.nomisrev.openapi.parser

import com.goncalossilva.resources.Resource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class IntegrationTest {

    private fun loadResource(path: String): String =
        Resource("specs/$path").readText()

    @Test
    fun keycloak() {
        OpenAPI.fromYaml(loadResource("keycloak.yml"))
    }

    // ── Petstore 3.0.x ────────────────────────────────────────────────────────

    @Test
    fun `petstore 3_0 parses successfully`() {
        val spec = OpenAPI.fromYaml(loadResource("petstore-3.0.yaml"))

        assertTrue(spec.info.title.isNotBlank())
        assertTrue(spec.info.version.isNotBlank())
        assertEquals("Swagger Petstore", spec.info.title)
        assertEquals("1.0.0", spec.info.version)
        assertEquals("3.0.0", spec.openapi)

        assertTrue(spec.paths.isNotEmpty())
        assertEquals(2, spec.paths.size)
        assertTrue(spec.paths.containsKey("/pets"))
        assertTrue(spec.paths.containsKey("/pets/{petId}"))

        val schemas = spec.components.schemas
        assertTrue(schemas.isNotEmpty())
        assertEquals(3, schemas.size)
        assertTrue(schemas.containsKey("Pet"))
        assertTrue(schemas.containsKey("Pets"))
        assertTrue(schemas.containsKey("Error"))

        val listPets = spec.paths["/pets"]?.get
        assertNotNull(listPets)
        assertEquals("listPets", listPets.operationId)
        assertEquals(1, listPets.parameters.size)

        val getPetById = spec.paths["/pets/{petId}"]?.get
        assertNotNull(getPetById)
        assertEquals("showPetById", getPetById.operationId)

        val createPets = spec.paths["/pets"]?.post
        assertNotNull(createPets)
        assertEquals("createPets", createPets.operationId)
        assertNotNull(createPets.requestBody)
    }

    // ── Stripe 3.0.0 ──────────────────────────────────────────────────────────

    @Test
    fun `stripe parses successfully`() {
        val spec = OpenAPI.fromJson(loadResource("stripe.json"))

        assertTrue(spec.info.title.isNotBlank())
        assertTrue(spec.info.version.isNotBlank())
        assertEquals("Stripe API", spec.info.title)
        assertEquals("3.0.0", spec.openapi)

        assertTrue(spec.paths.isNotEmpty())
        assertTrue(spec.paths.size > 400)

        val schemas = spec.components.schemas
        assertTrue(schemas.isNotEmpty())
        assertTrue(schemas.size > 1000)
    }

    // ── GitHub 3.0.3 ──────────────────────────────────────────────────────────

    @Test
    fun `github parses successfully`() {
        val spec = OpenAPI.fromJson(loadResource("github.json"))

        assertTrue(spec.info.title.isNotBlank())
        assertTrue(spec.info.version.isNotBlank())
        assertEquals("GitHub v3 REST API", spec.info.title)
        assertEquals("3.0.3", spec.openapi)

        assertTrue(spec.paths.isNotEmpty())
        assertTrue(spec.paths.size > 700)

        val schemas = spec.components.schemas
        assertTrue(schemas.isNotEmpty())
        assertTrue(schemas.size > 900)
    }

    // ── YouTrack 3.0.1 ────────────────────────────────────────────────────────

    @Test
    fun `youtrack parses successfully`() {
        val spec = OpenAPI.fromJson(loadResource("youtrack.json"))

        assertTrue(spec.info.title.isNotBlank())
        assertTrue(spec.info.version.isNotBlank())
        assertEquals("YouTrack REST API", spec.info.title)
        assertEquals("3.0.1", spec.openapi)

        assertTrue(spec.paths.isNotEmpty())
        assertTrue(spec.paths.size > 100)

        val schemas = spec.components.schemas
        assertTrue(schemas.isNotEmpty())
        assertTrue(schemas.size > 200)
    }

    // ── OpenAI 3.1.0 ──────────────────────────────────────────────────────────

    @Test
    fun `openai parses successfully`() {
        val spec = OpenAPI.fromYaml(loadResource("openai.yaml"))

        assertTrue(spec.info.title.isNotBlank())
        assertTrue(spec.info.version.isNotBlank())
        assertEquals("OpenAI API", spec.info.title)
        assertEquals("3.1.0", spec.openapi)

        assertTrue(spec.paths.isNotEmpty())
        assertTrue(spec.paths.size > 50)

        val schemas = spec.components.schemas
        assertTrue(schemas.isNotEmpty())
    }

    @Test
    fun supabase() {
        val spec = OpenAPI.fromJson(loadResource("supabase.json"))
        assertEquals("Supabase API (v1)", spec.info.title)
    }

//   kotlinx.serialization.json.internal.JsonDecodingException:
//   Expected JsonPrimitive, but had JsonArray as the serialized body of string at element: $.0
//JSON input: []
//	at kotlinx.serialization.json.internal.JsonExceptionsKt.JsonDecodingException(JsonExceptions.kt:24)
//	at kotlinx.serialization.json.internal.JsonExceptionsKt.JsonDecodingException(JsonExceptions.kt:32)
//	at kotlinx.serialization.json.internal.AbstractJsonTreeDecoder.decodeTaggedString(TreeJsonDecoder.kt:583)
//    @Test
//    fun cloudfare() {
//        val spec = OpenAPI.fromJson(loadResource("cloudflare.json"))
//    }

// SnakeYaml chokes:
// The incoming YAML document exceeds the limit: 3145728 code points.
// it.krzeminski.snakeyaml.engine.kmp.exceptions.YamlEngineException:
// The incoming YAML document exceeds the limit: 3145728 code points.
//	at it.krzeminski.snakeyaml.engine.kmp.scanner.ScannerImpl.fetchMoreTokens(ScannerImpl.kt:200)
//    @Test
//    fun mongodb() {
//        val spec = OpenAPI.fromYaml(loadResource("mongodb.yaml"))
//        assertEquals("Supabase API (v1)", spec.info.title)
//    }

    @Test
    fun flyIO() {
        val spec = OpenAPI.fromJson(loadResource("fly-io.json"))
        assertEquals("Machines API", spec.info.title)
    }

    @Test
    fun turso() {
        val spec = OpenAPI.fromJson(loadResource("turso.json"))
        assertEquals("Turso Platform API", spec.info.title)
    }
}
