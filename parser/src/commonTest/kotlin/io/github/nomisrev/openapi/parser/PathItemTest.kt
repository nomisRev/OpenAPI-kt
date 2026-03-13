package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PathItemTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): PathItem =
        OpenAPI.Json.decodeFromString(PathItem.serializer(), json)

    // ── Empty / defaults ──────────────────────────────────────────────────────

    @Test
    fun `empty json object deserializes to PathItem with all null defaults`() {
        val pathItem = decode("{}")
        assertNull(pathItem.ref)
        assertNull(pathItem.summary)
        assertNull(pathItem.description)
        assertNull(pathItem.get)
        assertNull(pathItem.put)
        assertNull(pathItem.post)
        assertNull(pathItem.delete)
        assertNull(pathItem.options)
        assertNull(pathItem.head)
        assertNull(pathItem.patch)
        assertNull(pathItem.trace)
        assertNull(pathItem.servers)
        assertTrue(pathItem.parameters.isEmpty())
        assertTrue(pathItem.extensions.isEmpty())
    }

    // ── Single method — GET only ──────────────────────────────────────────────

    @Test
    fun `path with only GET operation deserializes`() {
        val json = """
            {
              "get": {
                "operationId": "listPets",
                "responses": {"200": {"description": "OK"}}
              }
            }
        """.trimIndent()

        val pathItem = decode(json)
        val get = assertNotNull(pathItem.get)
        assertEquals("listPets", get.operationId)
        assertNull(pathItem.put)
        assertNull(pathItem.post)
        assertNull(pathItem.delete)
        assertNull(pathItem.patch)
    }

    // ── All HTTP methods populated ─────────────────────────────────────────────

    @Test
    fun `path item with all HTTP methods deserializes`() {
        val responses = """{"200":{"description":"OK"}}"""
        val json = """
            {
              "get":     {"operationId": "getOp",     "responses": $responses},
              "put":     {"operationId": "putOp",     "responses": $responses},
              "post":    {"operationId": "postOp",    "responses": $responses},
              "delete":  {"operationId": "deleteOp",  "responses": $responses},
              "options": {"operationId": "optionsOp", "responses": $responses},
              "head":    {"operationId": "headOp",    "responses": $responses},
              "patch":   {"operationId": "patchOp",   "responses": $responses},
              "trace":   {"operationId": "traceOp",   "responses": $responses}
            }
        """.trimIndent()

        val pathItem = decode(json)
        assertEquals("getOp",     assertNotNull(pathItem.get).operationId)
        assertEquals("putOp",     assertNotNull(pathItem.put).operationId)
        assertEquals("postOp",    assertNotNull(pathItem.post).operationId)
        assertEquals("deleteOp",  assertNotNull(pathItem.delete).operationId)
        assertEquals("optionsOp", assertNotNull(pathItem.options).operationId)
        assertEquals("headOp",    assertNotNull(pathItem.head).operationId)
        assertEquals("patchOp",   assertNotNull(pathItem.patch).operationId)
        assertEquals("traceOp",   assertNotNull(pathItem.trace).operationId)
    }

    // ── summary and description ───────────────────────────────────────────────

    @Test
    fun `path item summary and description deserialize`() {
        val json = """
            {
              "summary": "Pet endpoints",
              "description": "Operations related to pets",
              "get": {"responses": {"200": {"description": "OK"}}}
            }
        """.trimIndent()

        val pathItem = decode(json)
        assertEquals("Pet endpoints", pathItem.summary)
        assertEquals("Operations related to pets", pathItem.description)
    }

    // ── $ref at path level ────────────────────────────────────────────────────

    @Test
    fun `path item with ref field deserializes`() {
        // PathItem.ref maps to JSON key "ref" (no SerialName override)
        val json = """{"ref": "./pets.yaml#/paths/~1pets"}"""
        val pathItem = decode(json)
        assertEquals("./pets.yaml#/paths/~1pets", pathItem.ref)
    }

    @Test
    fun `path item with ref alongside operations deserializes`() {
        val json = """
            {
              "ref": "./common.yaml#/paths/~1base",
              "get": {"responses": {"200": {"description": "OK"}}}
            }
        """.trimIndent()

        val pathItem = decode(json)
        assertEquals("./common.yaml#/paths/~1base", pathItem.ref)
        assertNotNull(pathItem.get)
    }

    // ── shared parameters ─────────────────────────────────────────────────────

    @Test
    fun `path item with shared inline parameters deserializes`() {
        val json = """
            {
              "parameters": [
                {"name": "petId", "in": "path", "required": true, "schema": {"type": "string"}},
                {"name": "version", "in": "header", "schema": {"type": "string"}}
              ],
              "get": {"responses": {"200": {"description": "OK"}}}
            }
        """.trimIndent()

        val pathItem = decode(json)
        assertEquals(2, pathItem.parameters.size)

        val petIdParam = assertIs<ReferenceOr.Value<Parameter>>(pathItem.parameters[0]).value
        assertEquals("petId", petIdParam.name)
        assertEquals(Parameter.Input.Path, petIdParam.input)
        assertTrue(petIdParam.required)

        val versionParam = assertIs<ReferenceOr.Value<Parameter>>(pathItem.parameters[1]).value
        assertEquals("version", versionParam.name)
        assertEquals(Parameter.Input.Header, versionParam.input)
    }

    @Test
    fun `path item with dollar-ref parameters deserializes`() {
        val json = """
            {
              "parameters": [
                {"${'$'}ref": "#/components/parameters/PetId"},
                {"${'$'}ref": "#/components/parameters/ApiVersion"}
              ],
              "get": {"responses": {"200": {"description": "OK"}}}
            }
        """.trimIndent()

        val pathItem = decode(json)
        assertEquals(2, pathItem.parameters.size)
        val ref0 = assertIs<ReferenceOr.Reference>(pathItem.parameters[0])
        assertEquals("#/components/parameters/PetId", ref0.ref)
        val ref1 = assertIs<ReferenceOr.Reference>(pathItem.parameters[1])
        assertEquals("#/components/parameters/ApiVersion", ref1.ref)
    }

    @Test
    fun `path item with mixed inline and ref parameters deserializes`() {
        val json = """
            {
              "parameters": [
                {"${'$'}ref": "#/components/parameters/PetId"},
                {"name": "format", "in": "query", "schema": {"type": "string"}}
              ],
              "get": {"responses": {"200": {"description": "OK"}}}
            }
        """.trimIndent()

        val pathItem = decode(json)
        assertEquals(2, pathItem.parameters.size)
        assertIs<ReferenceOr.Reference>(pathItem.parameters[0])
        val inline = assertIs<ReferenceOr.Value<Parameter>>(pathItem.parameters[1]).value
        assertEquals("format", inline.name)
    }

    // ── servers ───────────────────────────────────────────────────────────────

    @Test
    fun `path item with servers deserializes`() {
        val json = """
            {
              "servers": [
                {"url": "https://api.example.com/v1"},
                {"url": "https://staging.example.com/v1", "description": "Staging"}
              ],
              "get": {"responses": {"200": {"description": "OK"}}}
            }
        """.trimIndent()

        val pathItem = decode(json)
        val servers = assertNotNull(pathItem.servers)
        assertEquals(2, servers.size)
        assertEquals("https://api.example.com/v1", servers[0].url)
        assertEquals("https://staging.example.com/v1", servers[1].url)
        assertEquals("Staging", servers[1].description)
    }

    // ── extensions ────────────────────────────────────────────────────────────

    @Test
    fun `path item extensions are captured`() {
        val json = """
            {
              "get": {"responses": {"200": {"description": "OK"}}},
              "extensions": {"x-internal": "true", "x-deprecated-on": "2025-01-01"}
            }
        """.trimIndent()

        // PathItem uses plain @Serializable — extensions are stored under the "extensions" key
        val pathItem = decode(json)
        assertEquals(JsonPrimitive("true"), pathItem.extensions["x-internal"])
        assertEquals(JsonPrimitive("2025-01-01"), pathItem.extensions["x-deprecated-on"])
    }

    @Test
    fun `path item without extensions has empty extensions map`() {
        val json = """{"get": {"responses": {"200": {"description": "OK"}}}}"""
        val pathItem = decode(json)
        assertTrue(pathItem.extensions.isEmpty())
    }

    // ── unknown fields ignored ────────────────────────────────────────────────

    @Test
    fun `unknown fields in path item are silently ignored`() {
        val json = """
            {
              "get": {"responses": {"200": {"description": "OK"}}},
              "unknownField": "value"
            }
        """.trimIndent()

        val pathItem = decode(json)
        assertNotNull(pathItem.get)
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml path item with get only deserializes`() {
        val yaml = """
            get:
              operationId: listPets
              responses:
                "200":
                  description: OK
        """.trimIndent()

        val pathItem = OpenAPI.Yaml.decodeFromString(PathItem.serializer(), yaml)
        val get = assertNotNull(pathItem.get)
        assertEquals("listPets", get.operationId)
        assertNull(pathItem.post)
    }

    @Test
    fun `yaml path item with shared parameters deserializes`() {
        val yaml = """
            parameters:
              - name: petId
                in: path
                required: true
                schema:
                  type: string
            get:
              responses:
                "200":
                  description: OK
        """.trimIndent()

        val pathItem = OpenAPI.Yaml.decodeFromString(PathItem.serializer(), yaml)
        assertEquals(1, pathItem.parameters.size)
        val param = assertIs<ReferenceOr.Value<Parameter>>(pathItem.parameters[0]).value
        assertEquals("petId", param.name)
        assertEquals(Parameter.Input.Path, param.input)
    }
}
