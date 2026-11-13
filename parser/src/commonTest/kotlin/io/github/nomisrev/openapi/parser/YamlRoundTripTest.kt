package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive

class YamlRoundTripTest {
    @Test
    fun `extension serializer writes YAML directly`() {
        val original = MediaType(
            schema = ReferenceOr.Value(Schema(type = Schema.Type.Basic.String)),
            extensions = mapOf(
                "x-meta" to Json.parseToJsonElement(
                    """{"enabled":true,"count":42,"ratio":1.5,"absent":null,"tags":["alpha","beta"]}"""
                ),
            ),
        )

        val yaml = OpenAPI.Yaml.encodeToString(MediaType.serializer(), original)

        assertFalse(yaml.contains("extensions:"))
        assertEquals(original, OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml))
    }

    @Test
    fun `responses serializer writes YAML directly`() {
        val original = Responses(
            default = ReferenceOr.Value(Response(description = "Fallback")),
            responses = mapOf(
                StatusCodeOrRange.Code(200) to ReferenceOr.Value(Response(description = "OK")),
                StatusCodeOrRange.Range("5XX") to ReferenceOr.Value(Response(description = "Error")),
            ),
        )

        val yaml = OpenAPI.Yaml.encodeToString(Responses.serializer(), original)

        assertEquals(original, OpenAPI.Yaml.decodeFromString(Responses.serializer(), yaml))
    }

    @Test
    fun `expression and arbitrary values serialize directly to YAML`() {
        val expression = ExpressionOrValue.Expression("${'$'}response.body#/cursor")
        val yaml = OpenAPI.Yaml.encodeToString(ExpressionOrValue.serializer(), expression)
        assertEquals(expression, OpenAPI.Yaml.decodeFromString(ExpressionOrValue.serializer(), yaml))

        val value = Json.parseToJsonElement("""{"count":42,"items":[true,null,"text"]}""")
        val valueYaml = OpenAPI.Yaml.encodeToString(
            ExpressionOrValue.serializer(), ExpressionOrValue.Value(value),
        )
        assertEquals(value, OpenAPI.Yaml.decodeFromString(JsonElementWithYamlSerializer, valueYaml))
    }

    @Test
    fun `JSON encoder configuration is retained`() {
        val json = Json { encodeDefaults = true }
        val original = MediaType(extensions = mapOf("x-vendor" to JsonPrimitive("value")))
        val encoded = json.encodeToString(MediaType.serializer(), original)

        assertEquals(
            Json.parseToJsonElement("""{"schema":null,"example":null,"examples":{},"encoding":{},"itemSchema":null,"itemEncoding":null,"prefixEncoding":[],"x-vendor":"value"}"""),
            json.parseToJsonElement(encoded),
        )
    }

    @Test
    fun `minimal document round trips through YAML`() {
        val api = OpenAPI(info = Info(title = "Test", version = "1"))

        assertEquals(api, OpenAPI.fromYaml(api.toYaml()))
    }

    @Test
    fun `YAML output preserves nested models references and extensions`() {
        val api = OpenAPI.fromYaml(
            """
            openapi: 3.1.0
            info:
              title: Test
              version: "1"
              x-meta:
                enabled: true
                count: 42
                ratio: 1.5
                absent: null
                tags: [alpha, beta]
            paths:
              x-owner: platform
              /items:
                get:
                  x-internal: true
                  responses:
                    "200":
                      description: OK
                      x-response: success
                      content:
                        application/json:
                          schema:
                            type: string
                          x-media: custom
                      links:
                        next:
                          operationId: listItems
                          parameters:
                            cursor: "${'$'}response.body#/cursor"
                          x-link: next
                    5XX:
                      ${'$'}ref: '#/components/responses/Error'
                    default:
                      description: Fallback
            components:
              responses:
                Error:
                  description: Error
            """.trimIndent()
        )

        val yaml = api.toYaml()

        assertFalse(yaml.trimStart().startsWith("{"), "Expected YAML mapping output")
        assertFalse(yaml.contains("extensions:"), "Extensions must be flattened")
        assertEquals(api, OpenAPI.fromYaml(yaml))
    }
}
