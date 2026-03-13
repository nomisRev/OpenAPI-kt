package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OpenAPITest {

    @Test
    fun `minimal valid 3_0 spec deserializes`() {
        val spec = OpenAPI.fromJson(
            """
            {
              "openapi": "3.0.3",
              "info": {"title": "Pets API", "version": "1.0.0"},
              "paths": {}
            }
            """.trimIndent()
        )

        assertEquals("3.0.3", spec.openapi)
        assertEquals("Pets API", spec.info.title)
        assertEquals("1.0.0", spec.info.version)
        assertTrue(spec.paths.isEmpty())
        assertTrue(spec.webhooks.isEmpty())
    }

    @Test
    fun `minimal valid 3_1 spec deserializes`() {
        val spec = OpenAPI.fromJson(
            """
            {
              "openapi": "3.1.0",
              "info": {"title": "Pets API", "version": "1.0.0"},
              "paths": {}
            }
            """.trimIndent()
        )

        assertEquals("3.1.0", spec.openapi)
        assertEquals("Pets API", spec.info.title)
        assertEquals("1.0.0", spec.info.version)
        assertTrue(spec.paths.isEmpty())
    }

    @Test
    fun `spec with all top level fields deserializes`() {
        val spec = OpenAPI.fromJson(
            """
            {
              "openapi": "3.1.0",
              "info": {
                "title": "Example API",
                "version": "2026-01-01",
                "description": "Demo API"
              },
              "servers": [{"url": "https://api.example.com"}],
              "paths": {
                "/pets": {
                  "get": {
                    "operationId": "listPets",
                    "responses": {"200": {"description": "ok"}}
                  }
                }
              },
              "webhooks": {
                "petCreated": {
                  "post": {
                    "responses": {
                      "200": {"description": "received"}
                    }
                  }
                }
              },
              "components": {
                "schemas": {
                  "Pet": {"type": "object"}
                },
                "responses": {
                  "NotFound": {"description": "not found"}
                },
                "parameters": {
                  "LimitParam": {"name": "limit", "in": "query", "schema": {"type": "integer"}}
                },
                "examples": {
                  "PetExample": {"summary": "A pet", "value": {"name": "Milo"}}
                },
                "requestBodies": {
                  "PetBody": {
                    "content": {
                      "application/json": {"schema": {"${'$'}ref": "#/components/schemas/Pet"}}
                    }
                  }
                },
                "headers": {
                  "X-Trace-Id": {"description": "trace id"}
                },
                "links": {
                  "PetById": {"operationId": "getPetById"}
                },
                "callbacks": {
                  "OnPetEvent": {
                    "{${'$'}request.body#/callbackUrl}": {
                      "post": {"responses": {"200": {"description": "ok"}}}
                    }
                  }
                },
                "pathItems": {
                  "PetsPath": {"${'$'}ref": "#/paths/~1pets"}
                }
              },
              "security": [{"apiKey": []}],
              "tags": [{"name": "pets"}],
              "externalDocs": {"url": "https://example.com/docs"},
              "x-trace": "enabled"
            }
            """.trimIndent()
        )

        assertEquals("3.1.0", spec.openapi)
        assertEquals("Example API", spec.info.title)
        assertEquals("https://api.example.com", spec.servers.single().url)
        assertTrue(spec.paths.containsKey("/pets"))
        assertTrue(spec.webhooks.containsKey("petCreated"))
        assertTrue(spec.components.schemas.containsKey("Pet"))
        assertTrue(spec.components.responses.containsKey("NotFound"))
        assertTrue(spec.components.parameters.containsKey("LimitParam"))
        assertTrue(spec.components.examples.containsKey("PetExample"))
        assertTrue(spec.components.requestBodies.containsKey("PetBody"))
        assertTrue(spec.components.headers.containsKey("X-Trace-Id"))
        assertTrue(spec.components.links.containsKey("PetById"))
        assertTrue(spec.components.callbacks.containsKey("OnPetEvent"))
        val pathItemRef = assertIs<ReferenceOr.Reference>(assertNotNull(spec.components.pathItems["PetsPath"]))
        assertEquals("#/paths/~1pets", pathItemRef.ref)
        assertEquals(1, spec.security.size)
        assertEquals(setOf(Tag(name = "pets")), spec.tags)
        assertEquals("https://example.com/docs", assertNotNull(spec.externalDocs).url)
        assertTrue(spec.extensions.isEmpty())
    }

    @Test
    fun `webhooks in 3_1 yaml deserializes`() {
        val spec = OpenAPI.fromYaml(
            """
            openapi: 3.1.0
            info:
              title: Webhooks API
              version: 1.0.0
            paths: {}
            webhooks:
              orderCreated:
                post:
                  responses:
                    "200":
                      description: ok
            """.trimIndent()
        )

        assertEquals("3.1.0", spec.openapi)
        val webhook = assertIs<ReferenceOr.Value<PathItem>>(assertNotNull(spec.webhooks["orderCreated"])).value
        assertNotNull(webhook.post)
    }
}
