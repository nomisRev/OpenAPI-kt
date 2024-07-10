package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class DefaultArguments {
  @Test
  fun nullArgumentForNonNullList() {
    val actual =
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
        .toModel("Strings")
    val expected =
      Model.obj(
        context = NamingContext.Named("Strings"),
        properties =
          listOf(
            Model.Object.property(
              "value",
              Model.Collection.list(inner = Model.Primitive.string(), default = emptyList()),
              isNullable = false,
            )
          ),
        inline = listOf(Model.Primitive.string()),
      )
    assertEquals(expected, actual)
  }

  @Test
  fun jsEmptyArrayArgumentForList() {
    val actual =
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
        .toModel("Strings")
    val expected =
      Model.obj(
        context = NamingContext.Named("Strings"),
        properties =
          listOf(
            Model.Object.property(
              "value",
              Model.Collection.list(
                inner = Model.Primitive.string(),
                default = emptyList(),
                description = null
              ),
              isNullable = false,
            )
          ),
        inline = listOf(Model.Primitive.string())
      )
    assertEquals(expected, actual)
  }
}
