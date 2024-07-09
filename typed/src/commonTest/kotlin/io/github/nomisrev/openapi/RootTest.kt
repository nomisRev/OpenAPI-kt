package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.ktor.http.*
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class RootTest {
  @Test
  fun empty() {
    val root = testAPI.root("OpenAPI")
    assertEquals(Root("OpenAPI", emptyList(), emptyList()), root)
  }

  @Test
  fun operation() {
    val operation =
      Operation(
        operationId = "echo",
        responses =
          Responses(
            200 to
              value(
                Response(
                  content =
                    mapOf(
                      "application/json" to
                        MediaType(schema = value(Schema(type = Schema.Type.Basic.String)))
                    )
                )
              )
          )
      )
    val actual =
      testAPI
        .copy(
          paths =
            mapOf(
              "/echo" to
                PathItem(
                  get =
                    Operation(
                      operationId = "echo",
                      responses =
                        Responses(
                          200 to
                            value(
                              Response(
                                content =
                                  mapOf(
                                    "application/json" to
                                      MediaType(
                                        schema = value(Schema(type = Schema.Type.Basic.String))
                                      )
                                  )
                              )
                            )
                        )
                    )
                )
            )
        )
        .root("OpenAPI")
    val expected =
      Root(
        "OpenAPI",
        emptyList(),
        listOf(
          API(
            "echo",
            listOf(
              Route(
                operation = operation,
                path = "/echo",
                method = HttpMethod.Get,
                body = Route.Bodies(required = false, emptyMap(), emptyMap()),
                input = emptyList(),
                returnType =
                  Route.Returns(
                    HttpStatusCode.OK to
                      Route.ReturnType(Model.Primitive.String(null, null), emptyMap())
                  ),
                extensions = emptyMap(),
                nested = listOf(Model.Primitive.String(null, null))
              )
            ),
            nested = emptyList()
          )
        )
      )
    assertEquals(expected, actual)
  }
}
