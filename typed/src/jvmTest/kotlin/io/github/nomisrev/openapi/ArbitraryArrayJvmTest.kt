package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals

class ArbitraryArrayJvmTest {

  @Test
  fun component_array_with_items_empty_schema_is_list_of_free_form_json() {
    val api =
      testAPI.copy(
        components =
          Components(
            schemas =
              mapOf(
                "ArbitraryArray" to value(Schema(type = Type.Basic.Array, items = value(Schema())))
              )
          )
      )

    val models = api.models()

    val expected =
      setOf(
        Model.Collection.List(
          inner = Model.FreeFormJson(description = null, constraint = null),
          default = null,
          description = null,
          constraint = null,
        )
      )

    assertEquals(expected, models)
  }

  @Test
  fun response_array_with_items_empty_schema_is_list_of_free_form_json() {
    val operation =
      Operation(
        operationId = "getAnyArray",
        summary = "Returns an array of arbitrary values",
        responses =
          Responses(
            200 to
              value(
                Response(
                  content =
                    mapOf(
                      "application/json" to
                        MediaType(
                          schema = value(Schema(type = Type.Basic.Array, items = value(Schema())))
                        )
                    )
                )
              )
          ),
      )

    val api = testAPI.copy(paths = mapOf("/any-array" to PathItem(get = operation)))
    val route = api.root("OpenAPI").endpoints.single().routes.single()

    // Expect the 200 OK response type to be List<FreeFormJson>
    val expectedReturnType =
      Route.ReturnType(
        types =
          mapOf(
            "application/json" to
              Model.Collection.List(
                inner = Model.FreeFormJson(description = null, constraint = null),
                default = null,
                description = null,
                constraint = null,
              )
          ),
        extensions = emptyMap(),
      )

    assertEquals(HttpMethod.Get, route.method)
    assertEquals("/any-array", route.path)
    assertEquals("getAnyArray", route.operationId)

    val ok = HttpStatusCode.OK
    val actualReturnType = route.returnType.entries.getValue(ok)
    assertEquals(expectedReturnType, actualReturnType)
  }
}
