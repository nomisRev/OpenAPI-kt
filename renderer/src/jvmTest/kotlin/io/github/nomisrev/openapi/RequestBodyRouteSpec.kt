package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.toApiModel
import io.ktor.http.ContentType
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class RequestBodyRouteSpec {

    @Test
    fun `optional schema-less request body is ignored`() = runBlocking {
        val spec = openApiWithRequestBody(
            """
            {
              "required": false,
              "content": {
                "application/json": {
                  "examples": {
                    "ignored-example": {
                      "summary": "No schema available"
                    }
                  }
                }
              }
            }
            """.trimIndent()
        )

        val route = spec.toApiModel().routes.single()
        assertNull(route.body)
    }

    @Test
    fun `required schema-less request body fails with endpoint context`() {
        val spec = openApiWithRequestBody(
            """
            {
              "required": true,
              "content": {
                "application/json": {
                  "examples": {
                    "ignored-example": {
                      "summary": "No schema available"
                    }
                  }
                }
              }
            }
            """.trimIndent()
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            runBlocking { spec.toApiModel() }
        }

        val message = exception.message.orEmpty()
        assertTrue(message.contains("GET /content"))
        assertTrue(message.contains("(getContent)"))
        assertTrue(message.contains("application/json"))
    }

    @Test
    fun `required request body keeps only schema-backed media types`() = runBlocking {
        val spec = openApiWithRequestBody(
            """
            {
              "required": true,
              "content": {
                "application/json": {
                  "schema": {
                    "type": "string"
                  }
                },
                "text/plain": {
                  "examples": {
                    "ignored-example": {
                      "summary": "No schema available"
                    }
                  }
                }
              }
            }
            """.trimIndent()
        )

        val route = spec.toApiModel().routes.single()
        val body = assertNotNull(route.body)
        assertTrue(body.required)
        assertEquals(1, body.types.size)
        assertTrue(body.types.containsKey(ContentType.Application.Json))
        assertFalse(body.types.keys.any { ContentType.Text.Plain.match(it) })

        val jsonBody = body.types.getValue(ContentType.Application.Json)
        assertIs<Route.Body.SetBody>(jsonBody)
    }

    private fun openApiWithRequestBody(requestBody: String): OpenAPI =
        OpenAPI.fromJson(
            """
            {
              "openapi": "3.0.3",
              "info": {
                "title": "Request Body Spec",
                "version": "1.0.0"
              },
              "paths": {
                "/content": {
                  "get": {
                    "operationId": "getContent",
                    "requestBody": $requestBody,
                    "responses": {
                      "200": {
                        "description": "OK",
                        "content": {
                          "application/json": {
                            "schema": {
                              "type": "string"
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            """.trimIndent()
        )
}
