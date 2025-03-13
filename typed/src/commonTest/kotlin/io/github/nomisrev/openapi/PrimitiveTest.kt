package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.assertThrows

class PrimitiveTest {
  @Test
  fun double() {
    val actual =
      Schema(
          type = Type.Basic.Number,
          default = ExampleValue("1"),
          description = ReferenceOr.value("My Desc")
        )
        .toModel("Double")
    val expected = Primitive.double(default = 1.0, description = "My Desc")
    assertEquals(expected, actual)
  }

  @Test
  fun doubleIncorrectDefault() {
    val e =
      assertThrows<IllegalStateException> {
        Schema(
            type = Type.Basic.Number,
            default = ExampleValue("Nonsense Value"),
            description = ReferenceOr.value("My Desc")
          )
          .toModel("Primitive")
      }
    assertEquals("Default value Nonsense Value is not a Number.", e.message)
  }

  @Test
  fun doubleIncorrectDefaultMultiple() {
    val e =
      assertThrows<IllegalStateException> {
        Schema(
            type = Type.Basic.Number,
            default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
            description = ReferenceOr.value("My Desc")
          )
          .toModel("Primitive")
      }
    assertEquals("Multiple default values not supported for Number.", e.message)
  }

  @Test
  fun boolean() {
    val actual =
      Schema(
          type = Type.Basic.Boolean,
          default = ExampleValue("true"),
          description = ReferenceOr.value("My Desc")
        )
        .toModel("Primitive")
    assertEquals(Primitive.Boolean(true, description = "My Desc"), actual)
  }

  @Test
  fun booleanIncorrectDefault() {
    val e =
      assertThrows<IllegalStateException> {
        Schema(
            type = Type.Basic.Boolean,
            default = ExampleValue("Nonsense Value"),
            description = ReferenceOr.value("My Desc")
          )
          .toModel("Primitive")
      }
    assertEquals("Default value Nonsense Value is not a Boolean.", e.message)
  }

  @Test
  fun booleanIncorrectDefaultMultiple() {
    val e =
      assertThrows<IllegalStateException> {
        Schema(
            type = Type.Basic.Boolean,
            default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
            description = ReferenceOr.value("My Desc")
          )
          .toModel("Primitive")
      }
    assertEquals("Multiple default values not supported for Boolean.", e.message)
  }

  @Test
  fun integer() {
    val actual =
      Schema(
          type = Type.Basic.Integer,
          default = ExampleValue("2"),
          description = ReferenceOr.value("My Desc")
        )
        .toModel("Primitive")
    assertEquals(Primitive.int(default = 2, description = "My Desc"), actual)
  }

  @Test
  fun integerIncorrectDefault() {
    val e =
      assertThrows<IllegalStateException> {
        Schema(
            type = Type.Basic.Integer,
            default = ExampleValue("Nonsense Value"),
            description = ReferenceOr.value("My Desc")
          )
          .toModel("Primitive")
      }
    assertEquals("Default value Nonsense Value is not a Integer.", e.message)
  }

  @Test
  fun integerIncorrectDefaultMultiple() {
    val e =
      assertThrows<IllegalStateException> {
        Schema(
            type = Type.Basic.Integer,
            default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
            description = ReferenceOr.value("My Desc")
          )
          .toModel("Primitive")
      }
    assertEquals("Multiple default values not supported for Integer.", e.message)
  }

  @Test
  fun string() {
    val actual =
      Schema(
          type = Type.Basic.String,
          default = ExampleValue("Some Text"),
          description = ReferenceOr.value("My Desc")
        )
        .toModel("Primitive")
    assertEquals(Primitive.string(default = "Some Text", description = "My Desc"), actual)
  }

  @Test
  fun stringIntegerDefaultRemainsString() {
    val actual =
      Schema(
          type = Type.Basic.String,
          default = ExampleValue("999"),
          description = ReferenceOr.value("My Desc")
        )
        .toModel("Primitive")
    assertEquals(Primitive.string(default = "999", description = "My Desc"), actual)
  }

  @Test
  fun stringIncorrectDefaultMultiple() {
    val actual =
      Schema(
          type = Type.Basic.String,
          default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
          description = ReferenceOr.value("My Desc")
        )
        .toModel("Primitive")
    assertEquals(Primitive.string(default = "Nonsense, Value", description = "My Desc"), actual)
  }

  @Test
  fun nullNotSupported() {
    assertThrows<NotImplementedError> {
      Schema(
          type = Type.Basic.Null,
          default = ExampleValue.Multiple(listOf("Nonsense", "Value")),
          description = ReferenceOr.value("My Desc")
        )
        .toModel("Primitive")
    }
  }
}
