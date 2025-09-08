package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class OpenAPITransformerHelpersTest {
  @Test
  fun isOpenEnumeration_true_and_false_indirectly() {
    // Build an OpenAPI with a component that represents an open enumeration
    val openEnumSchema = Schema(
      anyOf = listOf(
        value(Schema(type = Type.Basic.String, enum = listOf("A", "B"))),
        value(Schema(type = Type.Basic.String))
      )
    )
    val openApiWithEnum = testAPI.withComponents(
      Components(schemas = mapOf("MyOpenEnum" to value(openEnumSchema)))
    )

    // models() should include an Enum.Open for the open enumeration component
    val models = openApiWithEnum.models()
    assertTrue(models.any { it is Model.Enum.Open })

    // Now a schema that should not be treated as open enum
    val notOpenEnumSchema = Schema(
      anyOf = listOf(
        value(Schema(type = Type.Basic.Integer)),
        value(Schema(type = Type.Basic.String))
      )
    )
    val openApiWithoutEnum = testAPI.withComponents(
      Components(schemas = mapOf("NotOpen" to value(notOpenEnumSchema)))
    )
    val models2 = openApiWithoutEnum.models()
    // Ensure it did not become an Enum.Open (could be Union or other)
    assertFalse(models2.any { it is Model.Enum.Open })
  }

  @Test
  fun boolean_multiple_defaults_not_supported_indirectly() {
    // A boolean schema with multiple defaults should throw during model conversion
    val bad = Schema(type = Type.Basic.Boolean, default = ExampleValue.Multiple(listOf("true", "false")))
    val api = testAPI.withComponents(Components(schemas = mapOf("Flag" to value(bad))))

    assertFailsWith<IllegalStateException> {
      api.models() // triggers conversion of components.schemas -> models
    }
  }
}
