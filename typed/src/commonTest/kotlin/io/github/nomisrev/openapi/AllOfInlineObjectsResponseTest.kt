package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals

class AllOfInlineObjectsResponseTest {

  @Test
  fun `allOf with two inline objects in response yields merged object`() {
    // Build the OpenAPI spec as described in the issue
    val responseSchema =
      Schema(
        allOf =
          listOf(
            value(
              Schema(
                type = Type.Basic.Object,
                properties = mapOf("id" to value(Schema(type = Type.Basic.Integer))),
              )
            ),
            value(
              Schema(
                type = Type.Basic.Object,
                properties = mapOf("username" to value(Schema(type = Type.Basic.String))),
              )
            ),
          )
      )

    val openApi =
      OpenAPI(
        info = Info(title = "Hello World API", version = "1.0"),
        paths =
          mapOf(
            "/hello-world" to
              PathItem(
                get =
                  Operation(
                    operationId = "hello-world",
                    responses =
                      Responses(
                        responses =
                          mapOf(
                            200 to
                              value(
                                Response(
                                  description = "updated",
                                  content =
                                    mapOf(
                                      "application/json" to
                                        MediaType(schema = value(responseSchema))
                                    ),
                                )
                              )
                          )
                      ),
                  )
              )
          ),
      )

    assertEquals(
      Model.Object(
        NamingContext.Nested(
          NamingContext.RouteBody("hello-world", postfix = "Response"),
          outer = NamingContext.Named("hello-world"),
        ),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              "id",
              Model.Primitive.int(),
              isRequired = true,
              isNullable = false,
              description = null,
            ),
            Model.Object.Property(
              "username",
              Model.Primitive.string(),
              isRequired = true,
              isNullable = false,
              description = null,
            ),
          ),
        inline = emptyList(),
      ),
      openApi
        .routes()
        .single()
        .returnType
        .entries[HttpStatusCode.OK]
        ?.types
        ?.get("application/json"),
    )
  }
}
