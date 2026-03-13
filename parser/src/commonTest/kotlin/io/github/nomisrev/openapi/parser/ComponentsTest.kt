package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ComponentsTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Components =
        OpenAPI.Json.decodeFromString(Components.serializer(), json)

    // ── Empty / defaults ──────────────────────────────────────────────────────

    @Test
    fun `empty json object deserializes to Components with all empty maps`() {
        val components = decode("{}")
        assertTrue(components.schemas.isEmpty())
        assertTrue(components.responses.isEmpty())
        assertTrue(components.parameters.isEmpty())
        assertTrue(components.examples.isEmpty())
        assertTrue(components.requestBodies.isEmpty())
        assertTrue(components.headers.isEmpty())
        assertTrue(components.links.isEmpty())
        assertTrue(components.callbacks.isEmpty())
        assertTrue(components.pathItems.isEmpty())
        assertTrue(components.extensions.isEmpty())
    }

    // ── schemas ───────────────────────────────────────────────────────────────

    @Test
    fun `components with inline schemas deserializes`() {
        val json = """
            {
              "schemas": {
                "Pet": {"type": "object"},
                "Error": {"type": "object"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(2, components.schemas.size)
        assertIs<ReferenceOr.Value<Schema>>(assertNotNull(components.schemas["Pet"]))
        assertIs<ReferenceOr.Value<Schema>>(assertNotNull(components.schemas["Error"]))
    }

    @Test
    fun `components schemas with dollar-ref deserializes`() {
        val json = """
            {
              "schemas": {
                "Pet": {"${'$'}ref": "#/components/schemas/BasePet"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(components.schemas["Pet"]))
        assertEquals("#/components/schemas/BasePet", ref.ref)
    }

    // ── responses ─────────────────────────────────────────────────────────────

    @Test
    fun `components with inline responses deserializes`() {
        val json = """
            {
              "responses": {
                "NotFound": {"description": "The specified resource was not found"},
                "Unauthorized": {"description": "Unauthorized"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(2, components.responses.size)
        val notFound = assertIs<ReferenceOr.Value<Response>>(assertNotNull(components.responses["NotFound"])).value
        assertEquals("The specified resource was not found", notFound.description)
        val unauthorized = assertIs<ReferenceOr.Value<Response>>(assertNotNull(components.responses["Unauthorized"])).value
        assertEquals("Unauthorized", unauthorized.description)
    }

    @Test
    fun `components responses with dollar-ref deserializes`() {
        val json = """
            {
              "responses": {
                "NotFound": {"${'$'}ref": "#/components/responses/BaseNotFound"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(components.responses["NotFound"]))
        assertEquals("#/components/responses/BaseNotFound", ref.ref)
    }

    // ── parameters ────────────────────────────────────────────────────────────

    @Test
    fun `components with inline parameters deserializes`() {
        val json = """
            {
              "parameters": {
                "LimitParam": {"name": "limit", "in": "query", "schema": {"type": "integer"}},
                "OffsetParam": {"name": "offset", "in": "query", "schema": {"type": "integer"}}
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(2, components.parameters.size)
        val limit = assertIs<ReferenceOr.Value<Parameter>>(assertNotNull(components.parameters["LimitParam"])).value
        assertEquals("limit", limit.name)
        assertEquals(Parameter.Input.Query, limit.input)
        val offset = assertIs<ReferenceOr.Value<Parameter>>(assertNotNull(components.parameters["OffsetParam"])).value
        assertEquals("offset", offset.name)
    }

    @Test
    fun `components parameters with dollar-ref deserializes`() {
        val json = """
            {
              "parameters": {
                "LimitParam": {"${'$'}ref": "#/components/parameters/BaseLimit"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(components.parameters["LimitParam"]))
        assertEquals("#/components/parameters/BaseLimit", ref.ref)
    }

    // ── examples ──────────────────────────────────────────────────────────────

    @Test
    fun `components with inline examples deserializes`() {
        val json = """
            {
              "examples": {
                "ActivePet": {"summary": "An active pet", "value": "active"},
                "InactivePet": {"summary": "An inactive pet", "value": "inactive"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(2, components.examples.size)
        val activePet = assertIs<ReferenceOr.Value<Example>>(assertNotNull(components.examples["ActivePet"])).value
        assertEquals("An active pet", activePet.summary)
    }

    @Test
    fun `components examples with dollar-ref deserializes`() {
        val json = """
            {
              "examples": {
                "ActivePet": {"${'$'}ref": "#/components/examples/BaseActivePet"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(components.examples["ActivePet"]))
        assertEquals("#/components/examples/BaseActivePet", ref.ref)
    }

    // ── requestBodies ─────────────────────────────────────────────────────────

    @Test
    fun `components with inline requestBodies deserializes`() {
        val json = """
            {
              "requestBodies": {
                "PetBody": {
                  "description": "A pet object",
                  "content": {
                    "application/json": {"schema": {"type": "object"}}
                  },
                  "required": true
                }
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(1, components.requestBodies.size)
        val petBody = assertIs<ReferenceOr.Value<RequestBody>>(assertNotNull(components.requestBodies["PetBody"])).value
        assertEquals("A pet object", petBody.description)
        assertTrue(petBody.required)
        assertTrue(petBody.content.containsKey("application/json"))
    }

    @Test
    fun `components requestBodies with dollar-ref deserializes`() {
        val json = """
            {
              "requestBodies": {
                "PetBody": {"${'$'}ref": "#/components/requestBodies/BasePetBody"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(components.requestBodies["PetBody"]))
        assertEquals("#/components/requestBodies/BasePetBody", ref.ref)
    }

    // ── headers ───────────────────────────────────────────────────────────────

    @Test
    fun `components with inline headers deserializes`() {
        val json = """
            {
              "headers": {
                "X-Rate-Limit": {"description": "Calls per hour allowed by the user"},
                "X-Expires-After": {"description": "Date in UTC when token expires"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(2, components.headers.size)
        val rateLimit = assertIs<ReferenceOr.Value<Header>>(assertNotNull(components.headers["X-Rate-Limit"])).value
        assertEquals("Calls per hour allowed by the user", rateLimit.description)
        val expiresAfter = assertIs<ReferenceOr.Value<Header>>(assertNotNull(components.headers["X-Expires-After"])).value
        assertEquals("Date in UTC when token expires", expiresAfter.description)
    }

    @Test
    fun `components headers with dollar-ref deserializes`() {
        val json = """
            {
              "headers": {
                "X-Rate-Limit": {"${'$'}ref": "#/components/headers/BaseRateLimit"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(components.headers["X-Rate-Limit"]))
        assertEquals("#/components/headers/BaseRateLimit", ref.ref)
    }

    // ── links ─────────────────────────────────────────────────────────────────

    @Test
    fun `components with empty links map deserializes`() {
        val json = """{"links": {}}"""
        val components = decode(json)
        assertTrue(components.links.isEmpty())
    }

    // ── callbacks ─────────────────────────────────────────────────────────────

    @Test
    fun `components with callbacks deserializes`() {
        val json = """
            {
              "callbacks": {
                "MyEvent": {
                  "{${'$'}url}": {
                    "post": {
                      "responses": {"200": {"description": "Callback received"}}
                    }
                  }
                }
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(1, components.callbacks.size)
        val callback = assertNotNull(components.callbacks["MyEvent"])
        assertTrue(callback.value.containsKey("{${"\$"}url}"))
    }

    // ── pathItems ─────────────────────────────────────────────────────────────

    @Test
    fun `components with inline pathItems deserializes`() {
        val json = """
            {
              "pathItems": {
                "/pets": {
                  "get": {
                    "operationId": "listPets",
                    "responses": {"200": {"description": "A list of pets"}}
                  }
                }
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(1, components.pathItems.size)
        val pathItem = assertIs<ReferenceOr.Value<PathItem>>(assertNotNull(components.pathItems["/pets"])).value
        assertEquals("listPets", assertNotNull(pathItem.get).operationId)
    }

    @Test
    fun `components pathItems with dollar-ref deserializes`() {
        val json = """
            {
              "pathItems": {
                "/pets": {"${'$'}ref": "#/components/pathItems/BasePets"}
              }
            }
        """.trimIndent()

        val components = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(components.pathItems["/pets"]))
        assertEquals("#/components/pathItems/BasePets", ref.ref)
    }

    // ── extensions ────────────────────────────────────────────────────────────

    @Test
    fun `components extensions are captured`() {
        val json = """
            {
              "x-internal": "true",
              "x-version": 2
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(JsonPrimitive("true"), components.extensions["x-internal"])
        assertEquals(JsonPrimitive(2), components.extensions["x-version"])
    }

    @Test
    fun `components without extensions has empty extensions map`() {
        val components = decode("{}")
        assertTrue(components.extensions.isEmpty())
    }

    @Test
    fun `components with multiple extensions deserializes`() {
        val json = """
            {
              "schemas": {"Pet": {"type": "object"}},
              "x-owner": "platform-team",
              "x-reviewed": true,
              "x-priority": 1
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(JsonPrimitive("platform-team"), components.extensions["x-owner"])
        assertEquals(JsonPrimitive(true), components.extensions["x-reviewed"])
        assertEquals(JsonPrimitive(1), components.extensions["x-priority"])
        assertEquals(1, components.schemas.size)
    }

    // ── all component maps populated ──────────────────────────────────────────

    @Test
    fun `components with all maps populated deserializes`() {
        val json = """
            {
              "schemas": {
                "Pet": {"type": "object"}
              },
              "responses": {
                "NotFound": {"description": "Not found"}
              },
              "parameters": {
                "LimitParam": {"name": "limit", "in": "query"}
              },
              "examples": {
                "ActivePet": {"summary": "Active pet"}
              },
              "requestBodies": {
                "PetBody": {"content": {"application/json": {}}, "required": false}
              },
              "headers": {
                "X-Rate-Limit": {"description": "Rate limit header"}
              },
              "callbacks": {
                "MyEvent": {"{${'$'}url}": {"post": {"responses": {"200": {"description": "OK"}}}}}
              },
              "pathItems": {
                "/pets": {"get": {"responses": {"200": {"description": "OK"}}}}
              }
            }
        """.trimIndent()

        val components = decode(json)
        assertEquals(1, components.schemas.size)
        assertEquals(1, components.responses.size)
        assertEquals(1, components.parameters.size)
        assertEquals(1, components.examples.size)
        assertEquals(1, components.requestBodies.size)
        assertEquals(1, components.headers.size)
        assertEquals(1, components.callbacks.size)
        assertEquals(1, components.pathItems.size)
    }

    // ── unknown fields ignored ────────────────────────────────────────────────

    @Test
    fun `unknown fields in components are silently ignored`() {
        val json = """{"unknownField":"value","anotherUnknown":42}"""
        val components = decode(json)
        assertTrue(components.schemas.isEmpty())
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml empty components deserializes`() {
        val yaml = "{}"
        val components = OpenAPI.Yaml.decodeFromString(Components.serializer(), yaml)
        assertTrue(components.schemas.isEmpty())
        assertTrue(components.responses.isEmpty())
        assertTrue(components.extensions.isEmpty())
    }

    @Test
    fun `yaml components with schemas deserializes`() {
        val yaml = """
            schemas:
              Pet:
                type: object
              Error:
                type: object
        """.trimIndent()

        val components = OpenAPI.Yaml.decodeFromString(Components.serializer(), yaml)
        assertEquals(2, components.schemas.size)
        assertIs<ReferenceOr.Value<Schema>>(assertNotNull(components.schemas["Pet"]))
        assertIs<ReferenceOr.Value<Schema>>(assertNotNull(components.schemas["Error"]))
    }
}
