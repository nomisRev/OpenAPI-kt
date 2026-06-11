package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ParameterTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Parameter =
        OpenAPI.Json.decodeFromString(Parameter.serializer(), json)

    // ── Input types ───────────────────────────────────────────────────────────

    @Test
    fun `query parameter deserializes`() {
        val param = decode("""{"name":"limit","in":"query"}""")
        assertEquals("limit", param.name)
        assertEquals(Parameter.Input.Query, param.input)
        assertFalse(param.required)
    }

    @Test
    fun `header parameter deserializes`() {
        val param = decode("""{"name":"X-Api-Key","in":"header"}""")
        assertEquals("X-Api-Key", param.name)
        assertEquals(Parameter.Input.Header, param.input)
        assertFalse(param.required)
    }

    @Test
    fun `path parameter deserializes and required defaults to true`() {
        val param = decode("""{"name":"petId","in":"path","required":true}""")
        assertEquals("petId", param.name)
        assertEquals(Parameter.Input.Path, param.input)
        assertTrue(param.required)
    }

    @Test
    fun `cookie parameter deserializes`() {
        val param = decode("""{"name":"session","in":"cookie"}""")
        assertEquals("session", param.name)
        assertEquals(Parameter.Input.Cookie, param.input)
        assertFalse(param.required)
    }

    // ── required field ────────────────────────────────────────────────────────

    @Test
    fun `query parameter with explicit required true deserializes`() {
        val param = decode("""{"name":"filter","in":"query","required":true}""")
        assertTrue(param.required)
    }

    @Test
    fun `query parameter with explicit required false deserializes`() {
        val param = decode("""{"name":"filter","in":"query","required":false}""")
        assertFalse(param.required)
    }

    @Test
    fun `path parameter without required field uses default true`() {
        // When "in" is "path", required defaults to true even if omitted
        val param = decode("""{"name":"id","in":"path"}""")
        assertTrue(param.required)
    }

    // ── defaults ──────────────────────────────────────────────────────────────

    @Test
    fun `minimal query parameter has correct defaults`() {
        val param = decode("""{"name":"q","in":"query"}""")
        assertNull(param.description)
        assertFalse(param.required)
        assertFalse(param.deprecated)
        assertFalse(param.allowEmptyValue)
        assertFalse(param.allowReserved)
        assertNull(param.schema)
        assertNull(param.style)
        assertNull(param.explode)
        assertNull(param.example)
        assertTrue(param.examples?.isEmpty() ?: true)
    }

    // ── description ───────────────────────────────────────────────────────────

    @Test
    fun `parameter with description deserializes`() {
        val param = decode("""{"name":"limit","in":"query","description":"Maximum number of results"}""")
        assertEquals("Maximum number of results", param.description)
    }

    // ── deprecated ────────────────────────────────────────────────────────────

    @Test
    fun `parameter defaults to not deprecated`() {
        val param = decode("""{"name":"q","in":"query"}""")
        assertFalse(param.deprecated)
    }

    @Test
    fun `deprecated parameter deserializes`() {
        val param = decode("""{"name":"q","in":"query","deprecated":true}""")
        assertTrue(param.deprecated)
    }

    // ── allowEmptyValue and allowReserved ─────────────────────────────────────

    @Test
    fun `allowEmptyValue true deserializes`() {
        val param = decode("""{"name":"q","in":"query","allowEmptyValue":true}""")
        assertTrue(param.allowEmptyValue)
    }

    @Test
    fun `allowReserved true deserializes`() {
        val param = decode("""{"name":"q","in":"query","allowReserved":true}""")
        assertTrue(param.allowReserved)
    }

    // ── schema ────────────────────────────────────────────────────────────────

    @Test
    fun `parameter with inline schema deserializes`() {
        val json = """{"name":"limit","in":"query","schema":{"type":"integer"}}"""
        val param = decode(json)
        val schema = assertIs<ReferenceOr.Value<Schema>>(assertNotNull(param.schema)).value
        assertEquals(Schema.Type.Basic.Integer, schema.type)
    }

    @Test
    fun `parameter with dollar-ref schema deserializes`() {
        val json = """{"name":"limit","in":"query","schema":{"${'$'}ref":"#/components/schemas/Limit"}}"""
        val param = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(param.schema))
        assertEquals("#/components/schemas/Limit", ref.ref)
    }

    // ── style ─────────────────────────────────────────────────────────────────

    @Test
    fun `parameter with style simple deserializes`() {
        val param = decode("""{"name":"id","in":"path","required":true,"style":"simple"}""")
        assertEquals(Style.simple, param.style)
    }

    @Test
    fun `parameter with style form deserializes`() {
        val param = decode("""{"name":"filter","in":"query","style":"form"}""")
        assertEquals(Style.form, param.style)
    }

    @Test
    fun `parameter with style spaceDelimited deserializes`() {
        val param = decode("""{"name":"tags","in":"query","style":"spaceDelimited"}""")
        assertEquals(Style.spaceDelimited, param.style)
    }

    @Test
    fun `parameter with style pipeDelimited deserializes`() {
        val param = decode("""{"name":"tags","in":"query","style":"pipeDelimited"}""")
        assertEquals(Style.pipeDelimited, param.style)
    }

    @Test
    fun `parameter with style deepObject deserializes`() {
        val param = decode("""{"name":"obj","in":"query","style":"deepObject"}""")
        assertEquals(Style.deepObject, param.style)
    }

    @Test
    fun `parameter with style matrix deserializes`() {
        val param = decode("""{"name":"id","in":"path","required":true,"style":"matrix"}""")
        assertEquals(Style.matrix, param.style)
    }

    @Test
    fun `parameter with style label deserializes`() {
        val param = decode("""{"name":"id","in":"path","required":true,"style":"label"}""")
        assertEquals(Style.label, param.style)
    }

    // ── explode ───────────────────────────────────────────────────────────────

    @Test
    fun `parameter with explode true deserializes`() {
        val param = decode("""{"name":"tags","in":"query","explode":true}""")
        assertEquals(true, param.explode)
    }

    @Test
    fun `parameter with explode false deserializes`() {
        val param = decode("""{"name":"tags","in":"query","explode":false}""")
        assertEquals(false, param.explode)
    }

    // ── example ───────────────────────────────────────────────────────────────

    @Test
    fun `parameter with string example deserializes`() {
        val json = """{"name":"status","in":"query","example":"active"}"""
        val param = decode(json)
        val example = assertIs<ExampleValue.Single>(assertNotNull(param.example))
        assertEquals("active", example.value)
    }

    // ── examples ─────────────────────────────────────────────────────────────

    @Test
    fun `parameter with examples map defaults to empty map`() {
        val param = decode("""{"name":"q","in":"query"}""")
        assertTrue(param.examples?.isEmpty() ?: true)
    }

    @Test
    fun `parameter with inline examples map deserializes`() {
        val json = """
            {
              "name": "status",
              "in": "query",
              "examples": {
                "active": {"summary": "Active status", "value": "active"},
                "inactive": {"summary": "Inactive status", "value": "inactive"}
              }
            }
        """.trimIndent()
        val param = decode(json)
        val examples = assertNotNull(param.examples)
        assertEquals(2, examples.size)
        val activeExample = assertIs<ReferenceOr.Value<Example>>(assertNotNull(examples["active"])).value
        assertEquals("Active status", activeExample.summary)
        val inactiveExample = assertIs<ReferenceOr.Value<Example>>(assertNotNull(examples["inactive"])).value
        assertEquals("Inactive status", inactiveExample.summary)
    }

    @Test
    fun `parameter with dollar-ref example deserializes`() {
        val json = """
            {
              "name": "status",
              "in": "query",
              "examples": {
                "activeStatus": {"${'$'}ref": "#/components/examples/ActiveStatus"}
              }
            }
        """.trimIndent()
        val param = decode(json)
        val examples = assertNotNull(param.examples)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(examples["activeStatus"]))
        assertEquals("#/components/examples/ActiveStatus", ref.ref)
    }

    // ── full parameter ────────────────────────────────────────────────────────

    @Test
    fun `full query parameter with all fields deserializes`() {
        val json = """
            {
              "name": "limit",
              "in": "query",
              "description": "Maximum number of results to return",
              "required": false,
              "deprecated": false,
              "allowEmptyValue": false,
              "allowReserved": false,
              "schema": {"type": "integer"},
              "style": "form",
              "explode": false,
              "example": 10
            }
        """.trimIndent()

        val param = decode(json)
        assertEquals("limit", param.name)
        assertEquals(Parameter.Input.Query, param.input)
        assertEquals("Maximum number of results to return", param.description)
        assertFalse(param.required)
        assertFalse(param.deprecated)
        assertFalse(param.allowEmptyValue)
        assertFalse(param.allowReserved)
        assertIs<ReferenceOr.Value<Schema>>(assertNotNull(param.schema))
        assertEquals(Style.form, param.style)
        assertEquals(false, param.explode)
        assertIs<ExampleValue.Single>(assertNotNull(param.example))
    }

    @Test
    fun `full path parameter with all fields deserializes`() {
        val json = """
            {
              "name": "petId",
              "in": "path",
              "description": "ID of the pet",
              "required": true,
              "schema": {"type": "string"},
              "style": "simple",
              "explode": false
            }
        """.trimIndent()

        val param = decode(json)
        assertEquals("petId", param.name)
        assertEquals(Parameter.Input.Path, param.input)
        assertEquals("ID of the pet", param.description)
        assertTrue(param.required)
        assertIs<ReferenceOr.Value<Schema>>(assertNotNull(param.schema))
        assertEquals(Style.simple, param.style)
        assertEquals(false, param.explode)
    }

    // ── unknown fields ignored ────────────────────────────────────────────────

    @Test
    fun `unknown fields in parameter are silently ignored`() {
        val json = """{"name":"q","in":"query","unknownField":"value","anotherUnknown":42}"""
        val param = decode(json)
        assertEquals("q", param.name)
        assertEquals(Parameter.Input.Query, param.input)
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml query parameter deserializes`() {
        val yaml = """
            name: limit
            in: query
            description: Maximum number of results
            required: false
            schema:
              type: integer
        """.trimIndent()

        val param = OpenAPI.Yaml.decodeFromString(Parameter.serializer(), yaml)
        assertEquals("limit", param.name)
        assertEquals(Parameter.Input.Query, param.input)
        assertEquals("Maximum number of results", param.description)
        assertFalse(param.required)
        assertNotNull(param.schema)
    }

    @Test
    fun `yaml path parameter deserializes`() {
        val yaml = """
            name: petId
            in: path
            required: true
            schema:
              type: string
        """.trimIndent()

        val param = OpenAPI.Yaml.decodeFromString(Parameter.serializer(), yaml)
        assertEquals("petId", param.name)
        assertEquals(Parameter.Input.Path, param.input)
        assertTrue(param.required)
    }

    // ── content ───────────────────────────────────────────────────────────────

    @Test
    fun `parameter with content deserializes`() {
        val yaml = """
            name: metadata
            in: query
            required: false
            content:
              application/json:
                schema:
                  anyOf:
                  - type: object
                    additionalProperties: true
                  - type: 'null'
                  title: Metadata
        """.trimIndent()

        val param = OpenAPI.Yaml.decodeFromString(Parameter.serializer(), yaml)
        assertEquals("metadata", param.name)
        val content = assertNotNull(param.content["application/json"])
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(content.schema)
        assertEquals("Metadata", schemaValue.value.title)
    }

    @Test
    fun `parameter with both schema and content fails`() {
        val yaml = """
            name: both
            in: query
            schema:
              type: string
            content:
              application/json:
                schema:
                  type: string
        """.trimIndent()

        kotlin.test.assertFailsWith<IllegalArgumentException> {
            OpenAPI.Yaml.decodeFromString(Parameter.serializer(), yaml)
        }
    }
}
