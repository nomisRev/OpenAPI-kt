package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CallbackTest {

    private fun decode(json: String): Callback =
        OpenAPI.Json.decodeFromString(Callback.serializer(), json)

    @Test
    fun `callback with expression keys mapping to path items deserializes`() {
        val json = """
            {
              "{${'$'}request.body#/successUrl}": {
                "post": {
                  "operationId": "onSuccess",
                  "responses": {"200": {"description": "Success callback accepted"}}
                }
              },
              "{${'$'}request.body#/failureUrl}": {
                "get": {
                  "operationId": "onFailure",
                  "responses": {"202": {"description": "Failure callback queued"}}
                }
              }
            }
        """.trimIndent()

        val callback = decode(json)
        assertEquals(2, callback.value.size)

        val successPath = assertNotNull(callback.value["{\$request.body#/successUrl}"])
        val successPost = assertNotNull(successPath.post)
        assertEquals("onSuccess", successPost.operationId)
        assertTrue(successPost.responses.responses.containsKey(200))

        val failurePath = assertNotNull(callback.value["{\$request.body#/failureUrl}"])
        val failureGet = assertNotNull(failurePath.get)
        assertEquals("onFailure", failureGet.operationId)
        assertTrue(failureGet.responses.responses.containsKey(202))
    }
}
