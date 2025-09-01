package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.NamingContext.Nested
import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DiscriminatorSpec {
  @Test
  fun oneOf_inline_objects_use_discriminator_property_for_case_naming() {
    val schema =
      Schema(
        oneOf =
          listOf(
            value(
              Schema(
                type = Type.Basic.Object,
                required = listOf("objectType"),
                properties =
                  mapOf(
                    "objectType" to value(Schema(type = Type.Basic.String, enum = listOf("Simple"))),
                    "value" to value(Schema(type = Type.Basic.String)),
                  ),
              )
            ),
            value(Schema(type = Type.Basic.Integer)),
          ),
        discriminator = Schema.Discriminator(propertyName = "objectType"),
      )

    val model = schema.toModel("TypeOrObject")
    val union = assertIs<Model.Union>(model)

    // The object case should be most complex and thus first
    val case0 = union.cases[0]
    assertEquals(Nested(Named("Simple"), Named("TypeOrObject")), case0.context)

    // The primitive int case remains as is
    val case1 = union.cases[1]
    assertEquals(Named("TypeOrObject"), case1.context)
  }

  @Test
  fun anyOf_inline_objects_use_discriminator_property_for_case_naming() {
    val schema =
      Schema(
        anyOf =
          listOf(
            value(
              Schema(
                type = Type.Basic.Object,
                required = listOf("objectType"),
                properties =
                  mapOf(
                    "objectType" to value(Schema(type = Type.Basic.String, enum = listOf("Complex"))),
                    "another" to value(Schema(type = Type.Basic.Integer)),
                  ),
              )
            ),
            value(Schema(type = Type.Basic.String)),
          ),
        discriminator = Schema.Discriminator(propertyName = "objectType"),
      )

    val model = schema.toModel("StringOrObject")
    val union = assertIs<Model.Union>(model)

    // The object case should be most complex and thus first
    val case0 = union.cases[0]
    assertEquals(Nested(Named("Complex"), Named("StringOrObject")), case0.context)

    // The primitive string case remains as is
    val case1 = union.cases[1]
    assertEquals(Named("StringOrObject"), case1.context)
  }
}
