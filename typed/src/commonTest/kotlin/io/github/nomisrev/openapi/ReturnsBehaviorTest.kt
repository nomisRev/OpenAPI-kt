package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ReturnsBehaviorTest {

  @Test
  fun `selects lowest 2xx as success and includes default, supporting multiple media types`() {
    val responses =
      Responses(
        // Provide a default response
        default =
          value(
            Response(
              content =
                mapOf(
                  "application/json" to
                    MediaType(schema = value(Schema(type = Type.Basic.String)))
                )
            )
          ),
        // Explicit responses
        responses =
          mapOf(
            201 to
              value(
                Response(
                  description = "Created",
                  content =
                    mapOf(
                      "application/json" to
                        MediaType(schema = value(Schema(type = Type.Basic.Integer)))
                    ),
                )
              ),
            200 to
              value(
                Response(
                  description = "OK",
                  content =
                    mapOf(
                      // Multiple media types on the same response
                      "application/json" to
                        MediaType(schema = value(Schema(type = Type.Basic.String))),
                      "text/plain" to
                        MediaType(schema = value(Schema(type = Type.Basic.Integer))),
                    ),
                )
              ),
          ),
      )

    val api =
      Operation(operationId = "multi", responses = responses)
        .toAPI("/multi")

    val route = api.routes.single()
    val returns = route.returnType

    // success picks the lowest 2xx response (200 over 201)
    assertEquals(returns.entries[HttpStatusCode.OK], returns.success)

    // default response is propagated
    val default = assertNotNull(returns.default)
    assertEquals(
      Model.Primitive.string(),
      default.types.getValue("application/json"),
    )

    // multiple media types are preserved on the selected response
    val ok = assertNotNull(returns.entries[HttpStatusCode.OK])
    assertEquals(
      setOf("application/json", "text/plain"),
      ok.types.keys,
    )
    assertEquals(Model.Primitive.string(), ok.types.getValue("application/json"))
    assertEquals(Model.Primitive.int(), ok.types.getValue("text/plain"))
  }

  @Test
  fun `no 2xx responses yields null success`() {
    val responses =
      Responses(
        responses =
          mapOf(
            400 to
              value(
                Response(
                  content =
                    mapOf(
                      "application/json" to
                        MediaType(schema = value(Schema(type = Type.Basic.String)))
                    )
                )
              ),
            404 to
              value(
                Response(
                  content =
                    mapOf(
                      "application/json" to
                        MediaType(schema = value(Schema(type = Type.Basic.String)))
                    )
                )
              ),
          ),
      )

    val api = Operation(operationId = "errors", responses = responses).toAPI("/errors")
    val returns = api.routes.single().returnType

    assertEquals(null, returns.success)
    assertEquals(setOf(HttpStatusCode.BadRequest, HttpStatusCode.NotFound), returns.entries.keys)
    assertEquals(null, returns.default)
  }

  @Test
  fun `204 with no content keeps empty media map but is success`() {
    val responses = Responses(204, Response(description = "No Content", content = emptyMap()))
    val api = Operation(operationId = "no-content", responses = responses).toAPI("/no-content")
    val returns = api.routes.single().returnType

    val noContent = assertNotNull(returns.entries[HttpStatusCode.NoContent])
    assertEquals(noContent, returns.success)
    assertEquals(emptySet(), noContent.types.keys)
  }

  @Test
  fun `default only is exposed and success is null`() {
    val responses =
      Responses(
        default =
          value(
            Response(
              content =
                mapOf(
                  "application/json" to
                    MediaType(schema = value(Schema(type = Type.Basic.String))),
                  "text/plain" to MediaType(schema = value(Schema(type = Type.Basic.Integer))),
                )
            )
          ),
        responses = emptyMap(),
      )

    val api = Operation(operationId = "default-only", responses = responses).toAPI("/default")
    val returns = api.routes.single().returnType

    assertEquals(null, returns.success)
    assertEquals(emptyMap(), returns.entries)
    val def = assertNotNull(returns.default)
    assertEquals(setOf("application/json", "text/plain"), def.types.keys)
    assertEquals(Model.Primitive.string(), def.types.getValue("application/json"))
    assertEquals(Model.Primitive.int(), def.types.getValue("text/plain"))
  }

  @Test
  fun `response content without schema maps to free-form json`() {
    val responses =
      Responses(
        responses =
          mapOf(
            200 to value(Response(content = mapOf("application/json" to MediaType(schema = null)))),
          ),
      )

    val api = Operation(operationId = "free-form", responses = responses).toAPI("/free-form")
    val returns = api.routes.single().returnType

    val ok = assertNotNull(returns.entries[HttpStatusCode.OK])
    assertEquals(Model.FreeFormJson(description = null, constraint = null), ok.types["application/json"])
  }
}
