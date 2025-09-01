package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.schema
import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals

class MapsTest {

  @Test
  fun `free-form objects with additionalProperties true`() {
    val actual =
      Schema(type = Type.Basic.Object, additionalProperties = AdditionalProperties.Allowed(true))
        .toModel("FreeFormTrue")

    assertEquals(Model.FreeFormJson(description = null, constraint = null), actual)
  }

  @Test
  fun `free-form objects with additionalProperties empty object`() {
    val actual =
      Schema(
          type = Type.Basic.Object,
          additionalProperties = AdditionalProperties.PSchema(value(Schema())),
        )
        .toModel("FreeFormEmptyObject")

    // {} is equivalent to additionalProperties: true per the spec
    // {} is equivalent to additionalProperties: true per the spec
    assertEquals(Model.FreeFormJson(description = null, constraint = null), actual)
  }

  @Test
  fun `dictionary with primitive value additionalProperties string`() {
    val dictSchema =
      Schema(
        type = Type.Basic.Object,
        additionalProperties = AdditionalProperties.PSchema(value(Schema(type = Type.Basic.String))),
      )

    val actual = dictSchema.toModel("StringDict")

    // EXPECTED per spec: Map<String, String>
    val expected =
      Model.Collection.Map(
        inner = Model.Primitive.String(default = null, description = null, constraint = null),
        description = null,
        constraint = null,
      )

    assertEquals(expected, actual)
  }

  @Test
  fun `dictionary with $ref value`() {
    val messageSchema =
      Schema(
        type = Type.Basic.Object,
        properties =
          mapOf(
            "code" to value(Schema(type = Type.Basic.Integer)),
            "text" to value(Schema(type = Type.Basic.String)),
          ),
      )

    val messagesSchema =
      Schema(
        type = Type.Basic.Object,
        additionalProperties = AdditionalProperties.PSchema(schema("Message")),
      )

    val api =
      testAPI.copy(
        components =
          Components(
            schemas = mapOf("Message" to value(messageSchema), "Messages" to value(messagesSchema))
          )
      )

    val models = api.models()

    // We expect both the referenced Message object and the Messages dictionary type
    val expectedMessage =
      Model.obj(
        context = NamingContext.Named("Message"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "code",
              model = Model.Primitive.Int(default = null, description = null, constraint = null),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
            Model.Object.Property(
              baseName = "text",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
          ),
        inline =
          listOf(
            Model.Primitive.Int(default = null, description = null, constraint = null),
            Model.Primitive.String(default = null, description = null, constraint = null),
          ),
      )

    val expectedMessages =
      Model.Collection.Map(
        inner = Model.Reference(NamingContext.Named("Message"), description = null),
        description = null,
        constraint = null,
      )

    assert(models.contains(expectedMessage)) { "Expected Message object model to be generated." }
    assert(models.contains(expectedMessages)) {
      "Expected Messages dictionary model (Map<String, Message>) to be generated."
    }
  }
}
