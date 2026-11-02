package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

/** OpenAPI 3.2 sequential media types: `itemSchema`, `itemEncoding` and `prefixEncoding`. */
class StreamingMediaTypeTest {

    private val streamingSpecYaml =
        """
        openapi: 3.2.0
        info:
          title: Streaming
          version: '1'
        paths:
          /logs:
            get:
              responses:
                '200':
                  description: A JSON Lines stream of log entries
                  content:
                    application/jsonl:
                      itemSchema:
                        ${'$'}ref: '#/components/schemas/LogEntry'
          /events:
            get:
              responses:
                '200':
                  description: A Server-Sent Events stream
                  content:
                    text/event-stream:
                      itemSchema:
                        type: object
                        required: [event, data]
                        properties:
                          event:
                            type: string
                          data:
                            type: string
                          id:
                            type: string
                          retry:
                            type: integer
                        oneOf:
                          - properties:
                              event:
                                const: message
                              data:
                                contentMediaType: application/json
                                contentSchema:
                                  ${'$'}ref: '#/components/schemas/Message'
                          - properties:
                              event:
                                const: done
                              data:
                                type: string
          /upload:
            post:
              requestBody:
                content:
                  multipart/mixed:
                    schema:
                      type: array
                    prefixEncoding:
                      - contentType: application/json
                      - contentType: image/png
                        headers:
                          Content-Disposition:
                            ${'$'}ref: '#/components/headers/ContentDisposition'
                    itemEncoding:
                      contentType: application/octet-stream
                      x-chunked: true
              responses:
                '204':
                  description: Uploaded
        components:
          schemas:
            LogEntry:
              type: object
              properties:
                level:
                  type: string
                message:
                  type: string
            Message:
              type: object
              properties:
                text:
                  type: string
        """.trimIndent()

    private val streamingSpecJson: String =
        OpenAPI.Json.encodeToString(JsonElement.serializer(), yamlToJson(streamingSpecYaml))

    /** Round-trip the YAML spec through the parser model and back to a JSON tree. */
    private fun roundTrip(api: OpenAPI): JsonObject = Json.parseToJsonElement(api.toJson()).jsonObject

    private fun JsonObject.media(path: String, method: String, contentType: String, status: String? = null): JsonObject {
        val operation = getValue("paths").jsonObject.getValue(path).jsonObject.getValue(method).jsonObject
        val content =
            if (status == null) operation.getValue("requestBody").jsonObject.getValue("content")
            else operation.getValue("responses").jsonObject.getValue(status).jsonObject.getValue("content")
        return content.jsonObject.getValue(contentType).jsonObject
    }

    private fun OpenAPI.responseMedia(path: String, contentType: String, status: Int): MediaType {
        val response = paths.paths.getValue(path).get!!.responses.responses.getValue(StatusCodeOrRange.Code(status))
        return assertIs<ReferenceOr.Value<Response>>(response).value.content.getValue(contentType)
    }

    private fun OpenAPI.requestMedia(path: String, contentType: String): MediaType {
        val body = paths.paths.getValue(path).post!!.requestBody
        return assertIs<ReferenceOr.Value<RequestBody>>(body).value.content.getValue(contentType)
    }

    private fun decodeBoth(): List<OpenAPI> = listOf(OpenAPI.fromYaml(streamingSpecYaml), OpenAPI.fromJson(streamingSpecJson))

    // ── Decoding ──────────────────────────────────────────────────────────────

    @Test
    fun `3_2 openapi version decodes`() {
        decodeBoth().forEach { assertEquals("3.2.0", it.openapi) }
    }

    @Test
    fun `3_2 JSONL itemSchema reference decodes`() {
        decodeBoth().forEach { api ->
            val media = api.responseMedia("/logs", "application/jsonl", 200)
            assertNull(media.schema)
            val itemSchema = assertIs<ReferenceOr.Reference>(media.itemSchema)
            assertEquals("#/components/schemas/LogEntry", itemSchema.ref)
        }
    }

    @Test
    fun `3_2 SSE itemSchema with oneOf event consts and contentSchema decodes`() {
        decodeBoth().forEach { api ->
            val media = api.responseMedia("/events", "text/event-stream", 200)
            val itemSchema = assertIs<ReferenceOr.Value<Schema>>(media.itemSchema).value
            assertEquals(Schema.Type.Basic.Object, itemSchema.type)
            assertEquals(listOf("event", "data"), itemSchema.required)
            assertEquals(setOf("event", "data", "id", "retry"), itemSchema.properties?.keys)
            val retry = assertIs<ReferenceOr.Value<Schema>>(itemSchema.properties?.get("retry")).value
            assertEquals(Schema.Type.Basic.Integer, retry.type)

            val variants = assertNotNull(itemSchema.oneOf).map { assertIs<ReferenceOr.Value<Schema>>(it).value }
            assertEquals(2, variants.size)
            val events = variants.map { variant ->
                assertIs<ReferenceOr.Value<Schema>>(variant.properties?.get("event")).value.const
            }
            assertEquals(listOf(JsonPrimitive("message"), JsonPrimitive("done")), events)

            val messageData = assertIs<ReferenceOr.Value<Schema>>(variants[0].properties?.get("data")).value
            assertEquals("application/json", messageData.contentMediaType)
            val contentSchema = assertIs<ReferenceOr.Reference>(messageData.contentSchema)
            assertEquals("#/components/schemas/Message", contentSchema.ref)

            val doneData = assertIs<ReferenceOr.Value<Schema>>(variants[1].properties?.get("data")).value
            assertNull(doneData.contentMediaType)
            assertNull(doneData.contentSchema)
        }
    }

    @Test
    fun `3_2 itemEncoding and prefixEncoding decode`() {
        decodeBoth().forEach { api ->
            val media = api.requestMedia("/upload", "multipart/mixed")
            assertTrue(media.encoding.isEmpty())

            val itemEncoding = assertNotNull(media.itemEncoding)
            assertEquals("application/octet-stream", itemEncoding.contentType)
            assertEquals(mapOf("x-chunked" to JsonPrimitive(true)), itemEncoding.extensions)

            assertEquals(listOf("application/json", "image/png"), media.prefixEncoding.map { it.contentType })
            val disposition = assertIs<ReferenceOr.Reference>(media.prefixEncoding[1].headers["Content-Disposition"])
            assertEquals("#/components/headers/ContentDisposition", disposition.ref)
        }
    }

    @Test
    fun `media type without streaming fields defaults them`() {
        val media = OpenAPI.Json.decodeFromString(MediaType.serializer(), """{"schema":{"type":"string"}}""")
        assertNull(media.itemSchema)
        assertNull(media.itemEncoding)
        assertTrue(media.prefixEncoding.isEmpty())
        val json = Json.parseToJsonElement(OpenAPI.Json.encodeToString(MediaType.serializer(), media)).jsonObject
        assertEquals(setOf("schema"), json.keys)
    }

    // ── Round-trip (encode/decode) ────────────────────────────────────────────

    @Test
    fun `3_2 JSONL itemSchema survives round-trip`() {
        val media = roundTrip(OpenAPI.fromYaml(streamingSpecYaml)).media("/logs", "get", "application/jsonl", "200")
        assertEquals(
            Json.parseToJsonElement("""{"${'$'}ref":"#/components/schemas/LogEntry"}"""),
            media["itemSchema"],
        )
    }

    @Test
    fun `3_2 SSE itemSchema with contentMediaType and contentSchema survives round-trip`() {
        val media = roundTrip(OpenAPI.fromYaml(streamingSpecYaml)).media("/events", "get", "text/event-stream", "200")
        val itemSchema = media["itemSchema"]?.jsonObject
        val messageData: JsonElement? =
            itemSchema?.get("oneOf")?.jsonArray?.get(0)?.jsonObject
                ?.getValue("properties")?.jsonObject?.getValue("data")?.jsonObject
        assertEquals(
            Json.parseToJsonElement(
                """{"contentMediaType":"application/json","contentSchema":{"${'$'}ref":"#/components/schemas/Message"}}"""
            ),
            messageData,
        )
    }

    @Test
    fun `3_2 itemEncoding and prefixEncoding survive round-trip`() {
        val media = roundTrip(OpenAPI.fromYaml(streamingSpecYaml)).media("/upload", "post", "multipart/mixed")
        assertEquals(
            Json.parseToJsonElement("""{"contentType":"application/octet-stream","x-chunked":true}"""),
            media["itemEncoding"],
        )
        assertEquals(
            Json.parseToJsonElement(
                """
                [
                  {"contentType":"application/json"},
                  {"contentType":"image/png","headers":{"Content-Disposition":{"${'$'}ref":"#/components/headers/ContentDisposition"}}}
                ]
                """
            ),
            media["prefixEncoding"],
        )
    }

    @Test
    fun `3_2 streaming document decodes identically from JSON and YAML and survives JSON round-trip`() {
        val fromYaml = OpenAPI.fromYaml(streamingSpecYaml)
        val fromJson = OpenAPI.fromJson(streamingSpecJson)
        assertEquals(fromYaml, fromJson)
        assertEquals(fromYaml, OpenAPI.fromJson(fromYaml.toJson()))
        // The JSON tree must not lose any of the streaming fields compared to the source document.
        assertEquals(
            yamlToJson(streamingSpecYaml).jsonObject.getValue("paths"),
            roundTrip(fromYaml).getValue("paths"),
        )
    }

    private fun yamlToJson(yaml: String): JsonElement =
        OpenAPI.Yaml.decodeFromString(JsonElementWithYamlSerializer, yaml)
}
