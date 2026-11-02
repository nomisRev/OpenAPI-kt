package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Stream.JsonSequence.Framing
import io.github.nomisrev.openapi.routes.Stream.ServerSentEvents.EventData
import io.github.nomisrev.openapi.routes.Stream.ServerSentEvents.EventType
import io.github.nomisrev.openapi.toApiTree
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private fun streamingSpec(paths: String, schemas: String = ""): OpenAPI =
    OpenAPI.fromYaml(
        """
        |openapi: 3.2.0
        |info:
        |  title: Streaming
        |  version: '1'
        |paths:
        |${paths.trimIndent().prependIndent("  ")}
        |components:
        |  schemas:
        |    LogEntry:
        |      type: object
        |      properties:
        |        level:
        |          type: string
        |        message:
        |          type: string
        |    Message:
        |      type: object
        |      properties:
        |        text:
        |          type: string
        |${schemas.trimIndent().prependIndent("    ")}
        """.trimMargin()
    )

private val jsonlSpec = streamingSpec(
    """
    /logs:
      get:
        responses:
          '200':
            description: A JSON Lines stream of log entries
            content:
              application/jsonl:
                itemSchema:
                  ${'$'}ref: '#/components/schemas/LogEntry'
    """
)

private val sseSpec = streamingSpec(
    """
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
    """
)

private val sseWithWholeSchemaSpec = streamingSpec(
    """
    /chat:
      get:
        responses:
          '200':
            description: SSE described both as a whole document and per event
            content:
              text/event-stream:
                schema:
                  type: string
                itemSchema:
                  type: object
                  properties:
                    data:
                      contentMediaType: application/json
                      contentSchema:
                        ${'$'}ref: '#/components/schemas/Message'
    """
)

private suspend fun OpenAPI.singleRoute(): Route =
    Registry(this).use { registry -> with(registry) { toRoutes() } }.single()

private suspend fun OpenAPI.okStream(contentType: String): Stream {
    val returnType = singleRoute().returns.responses.getValue(HttpStatusCode.OK)
    return assertNotNull(returnType.streams[ContentType.parse(contentType)], "No stream in $returnType")
}

private fun Model.referenceName(): String? =
    ((this as? Model.ContextHolder)?.context?.head as? NamingContext.Reference)?.name

private fun jsonSequenceSpec(contentType: String, itemSchema: String = "${'$'}ref: '#/components/schemas/LogEntry'") =
    streamingSpec(
        """
        /logs:
          get:
            responses:
              '200':
                description: A stream
                content:
                  $contentType:
                    itemSchema:
${itemSchema.trimIndent().prependIndent("                      ")}
        """
    )

private fun sseItemSchemaSpec(itemSchema: String, schemas: String = "") =
    streamingSpec(
        """
        /events:
          get:
            responses:
              '200':
                description: A Server-Sent Events stream
                content:
                  text/event-stream:
                    itemSchema:
${itemSchema.trimIndent().prependIndent("                      ")}
        """,
        schemas,
    )

private fun List<Model>.referenceNames(): Set<String> =
    mapNotNullTo(mutableSetOf()) { ((it as? Model.ContextHolder)?.context?.head as? NamingContext.Reference)?.name }

val StreamingResponseSpec by testSuite {
    test("JSONL itemSchema response is resolved and its item model is collected") {
        val tree = jsonlSpec.toApiTree()
        assertTrue("LogEntry" in tree.models.referenceNames(), "LogEntry missing from ${tree.models.referenceNames()}")
    }

    test("SSE itemSchema response is resolved and contentSchema models are collected") {
        val tree = sseSpec.toApiTree()
        assertTrue("Message" in tree.models.referenceNames(), "Message missing from ${tree.models.referenceNames()}")
    }

    test("itemSchema next to a whole-content schema is not dropped") {
        val tree = sseWithWholeSchemaSpec.toApiTree()
        assertTrue("Message" in tree.models.referenceNames(), "Message missing from ${tree.models.referenceNames()}")
    }

    test("JSONL itemSchema becomes a newline-delimited JSON sequence of the referenced item, not whole-content") {
        val route = jsonlSpec.singleRoute()
        val returnType = route.returns.responses.getValue(HttpStatusCode.OK)
        assertTrue(returnType.types.isEmpty(), "itemSchema must not be modelled as the whole content: ${returnType.types}")
        val stream = assertIs<Stream.JsonSequence>(returnType.streams.getValue(ContentType.parse("application/jsonl")))
        assertEquals(Framing.NewlineDelimited, stream.framing)
        assertEquals("LogEntry", stream.item.referenceName())
    }

    test("JSON sequence framing follows the media type") {
        assertEquals(
            Framing.NewlineDelimited,
            assertIs<Stream.JsonSequence>(jsonSequenceSpec("application/x-ndjson").okStream("application/x-ndjson")).framing,
        )
        assertEquals(
            Framing.RecordSeparated,
            assertIs<Stream.JsonSequence>(jsonSequenceSpec("application/json-seq").okStream("application/json-seq")).framing,
        )
        assertEquals(
            Framing.RecordSeparated,
            assertIs<Stream.JsonSequence>(
                jsonSequenceSpec("application/geo+json-seq").okStream("application/geo+json-seq")
            ).framing,
        )
    }

    test("inline JSONL item object is an inline response model of the route") {
        val api = jsonSequenceSpec(
            "application/jsonl",
            """
            type: object
            required: [line]
            properties:
              line:
                type: integer
            """,
        )
        val route = api.singleRoute()
        val stream = assertIs<Stream.JsonSequence>(route.returns.responses.getValue(HttpStatusCode.OK).streams.values.single())
        val item = assertIs<Model.Object>(stream.item)
        assertEquals(setOf("line"), item.properties.keys)
        assertEquals(NamingContext.Response, item.context.nested.single())
        assertTrue(item in route.nested, "Inline item must be a nested model of the route")
    }

    test("SSE oneOf over event consts becomes a closed set of named events with their own data") {
        val stream = assertIs<Stream.ServerSentEvents>(sseSpec.okStream("text/event-stream"))
        assertTrue(stream.isClosed)
        assertEquals(
            listOf(EventType.Named("message"), EventType.Named("done")),
            stream.events.map { it.type },
        )
        val [message, done] = stream.events

        val messageData = assertIs<EventData.Json>(message.data)
        assertEquals("application/json", messageData.contentMediaType)
        assertEquals("Message", messageData.model.referenceName())
        assertTrue(messageData.isRequired)

        val doneData = assertIs<EventData.Text>(done.data)
        assertIs<Model.Primitive.String>(doneData.model)
        assertNull(doneData.contentMediaType)
        assertTrue(doneData.isRequired)

        stream.events.forEach { event ->
            val id = assertNotNull(event.id)
            assertIs<Model.Primitive.String>(id.model)
            assertEquals(false, id.isRequired)
            val retry = assertNotNull(event.retry)
            assertIs<Model.Primitive.Long>(retry.model)
            assertEquals(false, retry.isRequired)
        }
    }

    test("SSE without event kinds is a single unconstrained event with raw text data") {
        val stream = assertIs<Stream.ServerSentEvents>(
            sseItemSchemaSpec(
                """
                type: object
                required: [data]
                properties:
                  data:
                    type: string
                """
            ).okStream("text/event-stream")
        )
        assertEquals(false, stream.isClosed)
        val event = stream.events.single()
        val type = assertIs<EventType.Unconstrained>(event.type)
        assertIs<Model.Primitive.String>(type.model)
        assertEquals(false, type.isRequired)
        val data = assertIs<EventData.Text>(event.data)
        assertIs<Model.Primitive.String>(data.model)
        assertTrue(data.isRequired)
        assertNull(event.id)
        assertNull(event.retry)
    }

    test("SSE with JSON data and free-form JSON when contentSchema is absent") {
        val stream = assertIs<Stream.ServerSentEvents>(
            sseItemSchemaSpec(
                """
                type: object
                properties:
                  data:
                    type: string
                    contentMediaType: application/vnd.api+json
                """
            ).okStream("text/event-stream")
        )
        val data = assertIs<EventData.Json>(stream.events.single().data)
        assertEquals("application/vnd.api+json", data.contentMediaType)
        assertIs<Model.FreeFormJson>(data.model)
        assertEquals(false, data.isRequired)
    }

    test("SSE data described directly by a non-string schema is JSON without a declared contentMediaType") {
        val stream = assertIs<Stream.ServerSentEvents>(
            sseItemSchemaSpec(
                """
                type: object
                properties:
                  data:
                    ${'$'}ref: '#/components/schemas/Message'
                """
            ).okStream("text/event-stream")
        )
        val data = assertIs<EventData.Json>(stream.events.single().data)
        assertNull(data.contentMediaType)
        assertEquals("Message", data.model.referenceName())
    }

    test("SSE event enum yields one named event per value") {
        val stream = assertIs<Stream.ServerSentEvents>(
            sseItemSchemaSpec(
                """
                type: object
                properties:
                  event:
                    enum: [ping, keepalive]
                  data:
                    type: string
                """
            ).okStream("text/event-stream")
        )
        assertEquals(listOf(EventType.Named("ping"), EventType.Named("keepalive")), stream.events.map { it.type })
        assertTrue(stream.isClosed)
    }

    test("SSE referenced item schema with discriminator on event and referenced branches") {
        val api = sseItemSchemaSpec(
            """
            ${'$'}ref: '#/components/schemas/ChatEvent'
            """,
            schemas = """
            ChatEvent:
              type: object
              required: [event, data]
              properties:
                event:
                  type: string
                id:
                  type: string
              oneOf:
                - ${'$'}ref: '#/components/schemas/DeltaEvent'
                - ${'$'}ref: '#/components/schemas/ErrorEvent'
              discriminator:
                propertyName: event
                mapping:
                  delta: '#/components/schemas/DeltaEvent'
            DeltaEvent:
              type: object
              properties:
                data:
                  contentMediaType: application/json
                  contentSchema:
                    ${'$'}ref: '#/components/schemas/Delta'
            ErrorEvent:
              type: object
              properties:
                data:
                  contentMediaType: application/json
                  contentSchema:
                    type: object
                    properties:
                      code:
                        type: integer
            Delta:
              type: object
              properties:
                content:
                  type: string
            """,
        )
        val stream = assertIs<Stream.ServerSentEvents>(api.okStream("text/event-stream"))
        assertEquals(listOf(EventType.Named("delta"), EventType.Named("ErrorEvent")), stream.events.map { it.type })
        val [delta, error] = stream.events
        assertEquals("Delta", assertIs<EventData.Json>(delta.data).model.referenceName())
        assertTrue(delta.data.isRequired)
        assertIs<Model.Primitive.String>(assertNotNull(delta.id).model)
        assertNull(delta.retry)

        val errorData = assertIs<Model.Object>(assertIs<EventData.Json>(error.data).model)
        assertEquals(setOf("code"), errorData.properties.keys)
        assertEquals(
            listOf(NamingContext.Response, NamingContext.UnionCase("ErrorEvent"), NamingContext.ObjectProperty("data")),
            errorData.context.nested,
        )

        val modelNames = api.toApiTree().models.referenceNames()
        assertTrue("Delta" in modelNames, "Delta missing from $modelNames")
        // The SSE envelopes are described by the stream itself and are not JSON-decodable models.
        assertTrue(modelNames.none { it in setOf("ChatEvent", "DeltaEvent", "ErrorEvent") }, "$modelNames")
    }

    test("whole-content schema and itemSchema of one media type are both retained") {
        val returnType = sseWithWholeSchemaSpec.singleRoute().returns.responses.getValue(HttpStatusCode.OK)
        val contentType = ContentType.parse("text/event-stream")
        assertIs<Model.Primitive.String>(returnType.types.getValue(contentType))
        val stream = assertIs<Stream.ServerSentEvents>(returnType.streams.getValue(contentType))
        assertEquals("Message", assertIs<EventData.Json>(stream.events.single().data).model.referenceName())
    }

    test("itemSchema on a media type without known item framing is kept as unsupported stream") {
        val stream = jsonSequenceSpec("multipart/mixed").okStream("multipart/mixed")
        assertEquals("LogEntry", assertIs<Stream.Unsupported>(stream).item.referenceName())
    }

    test("media type without schema and itemSchema still fails") {
        val api = streamingSpec(
            """
            /logs:
              get:
                responses:
                  '200':
                    description: nothing
                    content:
                      application/jsonl: {}
            """
        )
        assertFailsWith<IllegalArgumentException> { api.singleRoute() }
    }
}

/** Request bodies with `itemSchema`/`itemEncoding`/`prefixEncoding` are out of scope; characterize them. */
val StreamingRequestBodySpec by testSuite {
    test("multipart/mixed body with prefixEncoding and itemEncoding resolves its whole-content schema") {
        val api = streamingSpec(
            """
            /upload:
              post:
                requestBody:
                  content:
                    multipart/mixed:
                      schema:
                        type: array
                      prefixEncoding:
                        - contentType: application/json
                      itemEncoding:
                        contentType: application/octet-stream
                responses:
                  '204':
                    description: Uploaded
            """
        )
        val body = api.singleRoute().body?.types?.get(ContentType.parse("multipart/mixed"))
        assertIs<Model.Collection>(assertIs<Route.Body.SetBody>(body).type)
    }

    test("optional itemSchema-only body is not modelled (dropped)") {
        val api = streamingSpec(
            """
            /ingest:
              post:
                requestBody:
                  content:
                    application/jsonl:
                      itemSchema:
                        ${'$'}ref: '#/components/schemas/LogEntry'
                responses:
                  '204':
                    description: Ingested
            """
        )
        assertNull(api.singleRoute().body)
    }

    test("required itemSchema-only body fails with a diagnostic") {
        val api = streamingSpec(
            """
            /ingest:
              post:
                requestBody:
                  required: true
                  content:
                    application/jsonl:
                      itemSchema:
                        ${'$'}ref: '#/components/schemas/LogEntry'
                responses:
                  '204':
                    description: Ingested
            """
        )
        val failure = assertFailsWith<IllegalArgumentException> { api.singleRoute() }
        assertTrue(failure.message.orEmpty().contains("application/jsonl"), failure.message)
    }
}
