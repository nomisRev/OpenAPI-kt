package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class DefaultArguments {
  @Test
  fun nullArgumentForNonNullList() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "Strings" to
                    value(
                      Schema(
                        type = Type.Basic.Object,
                        properties =
                          mapOf(
                            "value" to
                              value(
                                Schema(
                                  type = Type.Basic.Array,
                                  items = value(Schema(type = Type.Basic.String)),
                                  default = ExampleValue("null"),
                                  nullable = false
                                )
                              )
                          )
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Object(
        context = NamingContext.Named("Strings"),
        properties =
          listOf(
            Model.Object.Property(
              "value",
              Model.Collection.List(
                inner = Model.Primitive.String(default = null, description = null),
                default = emptyList(),
                description = null
              ),
              isRequired = false,
              isNullable = false,
              description = null
            )
          ),
        description = null,
        inline = listOf(Model.Primitive.String(default = null, description = null))
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun jsEmptyArrayArgumentForList() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "Strings" to
                    value(
                      Schema(
                        type = Type.Basic.Object,
                        properties =
                          mapOf(
                            "value" to
                              value(
                                Schema(
                                  type = Type.Basic.Array,
                                  items = value(Schema(type = Type.Basic.String)),
                                  default = ExampleValue("[]"),
                                  nullable = false
                                )
                              )
                          )
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Object(
        context = NamingContext.Named("Strings"),
        properties =
          listOf(
            Model.Object.Property(
              "value",
              Model.Collection.List(
                inner = Model.Primitive.String(default = null, description = null),
                default = emptyList(),
                description = null
              ),
              isRequired = false,
              isNullable = false,
              description = null
            )
          ),
        description = null,
        inline = listOf(Model.Primitive.String(default = null, description = null))
      )
    assertEquals(setOf(expected), actual)
  }
}
