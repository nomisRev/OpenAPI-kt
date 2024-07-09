package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.assertThrows

class PrimitiveTest {
  @Test
  fun double() {
    primitive(
      Schema(type = Type.Basic.Number, default = ExampleValue("1"), description = "My Desc"),
      Primitive.Double(1.0, description = "My Desc")
    )
  }

  @Test
  fun doubleIncorrectDefault() {
    primitive(
      Schema(
        type = Type.Basic.Number,
        default = ExampleValue("Nonsense Value"),
        description = "My Desc"
      ),
      Primitive.Double(null, description = "My Desc")
    )
  }

  @Test
  fun doubleIncorrectDefaultMultiple() {
    primitive(
      Schema(
        type = Type.Basic.Number,
        default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
        description = "My Desc"
      ),
      Primitive.Double(null, description = "My Desc")
    )
  }

  @Test
  fun boolean() {
    primitive(
      Schema(type = Type.Basic.Boolean, default = ExampleValue("true"), description = "My Desc"),
      Primitive.Boolean(true, description = "My Desc")
    )
  }

  @Test
  fun booleanIncorrectDefault() {
    primitive(
      Schema(
        type = Type.Basic.Boolean,
        default = ExampleValue("Nonsense Value"),
        description = "My Desc"
      ),
      Primitive.Boolean(null, description = "My Desc")
    )
  }

  @Test
  fun booleanIncorrectDefaultMultiple() {
    primitive(
      Schema(
        type = Type.Basic.Boolean,
        default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
        description = "My Desc"
      ),
      Primitive.Boolean(null, description = "My Desc")
    )
  }

  @Test
  fun integer() {
    primitive(
      Schema(type = Type.Basic.Integer, default = ExampleValue("2"), description = "My Desc"),
      Primitive.Int(2, description = "My Desc")
    )
  }

  @Test
  fun integerIncorrectDefault() {
    primitive(
      Schema(
        type = Type.Basic.Integer,
        default = ExampleValue("Nonsense Value"),
        description = "My Desc"
      ),
      Primitive.Int(null, description = "My Desc")
    )
  }

  @Test
  fun integerIncorrectDefaultMultiple() {
    primitive(
      Schema(
        type = Type.Basic.Integer,
        default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
        description = "My Desc"
      ),
      Primitive.Int(null, description = "My Desc")
    )
  }

  @Test
  fun string() {
    primitive(
      Schema(
        type = Type.Basic.String,
        default = ExampleValue("Some Text"),
        description = "My Desc"
      ),
      Primitive.String("Some Text", description = "My Desc")
    )
  }

  @Test
  fun stringIntegerDefaultRemainsString() {
    primitive(
      Schema(type = Type.Basic.String, default = ExampleValue("999"), description = "My Desc"),
      Primitive.String("999", description = "My Desc")
    )
  }

  @Test
  fun stringIncorrectDefaultMultiple() {
    primitive(
      Schema(
        type = Type.Basic.String,
        default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
        description = "My Desc"
      ),
      Primitive.String("Nonsense, Value", description = "My Desc")
    )
  }

  @Test
  fun nullNotSupported() {
    assertThrows<NotImplementedError> {
      primitive(
        Schema(
          type = Type.Basic.Null,
          default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
          description = "My Desc"
        ),
        Primitive.String("Nonsense, Value", description = "My Desc")
      )
    }
  }

  private fun primitive(schema: Schema, model: Model) {
    val actual =
      testAPI.copy(components = Components(schemas = mapOf("Primitive" to value(schema)))).models()
    assertEquals(setOf(model), actual)
  }
}
