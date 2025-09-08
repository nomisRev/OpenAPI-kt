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
    val actual =
      Operation(
          operationId = "echo",
          summary = "Echoes the input",
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
            ),
        )
        .toAPI("/echo")
    val expected =
      API(
        "echo",
        listOf(
          Route(
            operationId = "echo",
            summary = "Echoes the input",
            path = "/echo",
            method = HttpMethod.Get,
            body = Route.Bodies(required = false, emptyMap(), emptyMap()),
            input = emptyList(),
            returnType =
              Route.Returns(
                HttpStatusCode.OK to
                  Route.ReturnType(mapOf("application/json" to Model.Primitive.string(null, null)), emptyMap())
              ),
            extensions = emptyMap(),
            nested = listOf(Model.Primitive.string(null, null)),
          )
        ),
        nested = emptyList(),
      )
    assertEquals(expected, actual)
  }
}

fun Operation.toAPI(path: String): API =
  testAPI.copy(paths = mapOf(path to PathItem(get = this))).root("OpenAPI").endpoints.single()
