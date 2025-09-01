package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SchemaEdgeCasesTest {

  @Test
  fun anyOf_with_single_object_flattens_and_keeps_top_level_description() {
    val inner =
      Schema(
        type = Type.Basic.Object,
        properties = mapOf("id" to value(Schema(type = Type.Basic.String))),
      )

    val schema = Schema(description = value("Top-level description"), anyOf = listOf(value(inner)))

    val model = schema.toModel("SingleObject")

    val expected =
      Model.obj(
        context = NamingContext.Named("SingleObject"),
        description = "Top-level description",
        properties =
          listOf(
            Model.Object.Property(
              baseName = "id",
              model = Model.Primitive.String(null, null, null),
              isRequired = false,
              isNullable = true,
              description = null,
            )
          ),
        inline = listOf(Model.Primitive.String(null, null, null)),
      )

    assertEquals(expected, model)
  }

  @Test
  fun null_type_with_properties_is_treated_as_object() {
    val schema =
      Schema(
        // Explicitly no type
        properties = mapOf("name" to value(Schema(type = Type.Basic.String)))
      )

    val model = schema.toModel("NoTypeObject")

    val expected =
      Model.obj(
        context = NamingContext.Named("NoTypeObject"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "name",
              model = Model.Primitive.String(null, null, null),
              isRequired = false,
              isNullable = true,
              description = null,
            )
          ),
        inline = listOf(Model.Primitive.String(null, null, null)),
      )

    assertEquals(expected, model)
  }

  @Test
  fun integer_multiple_default_values_are_not_supported() {
    val schema =
      Schema(type = Type.Basic.Integer, default = ExampleValue.Multiple(listOf("1", "2")))

    assertFailsWith<IllegalStateException> { schema.toModel("BadDefault") }
  }
}
