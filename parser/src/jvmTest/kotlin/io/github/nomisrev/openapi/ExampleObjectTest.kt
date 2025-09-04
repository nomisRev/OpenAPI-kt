package io.github.nomisrev.openapi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ExampleValueObjectTest {
  @Test
  fun `object example value is parsed as string`() {
    val json =
      """
      {
        "openapi": "3.1.0",
        "info": { "title": "t", "version": "1.0.0" },
        "components": {
          "examples": {
            "objExample": {
              "value": { "a": 1, "b": { "c": "d" } }
            }
          }
        }
      }
      """
        .trimIndent()

    val openAPI = OpenAPI.fromJson(json)
    val example =
      openAPI.components.examples["objExample"]?.valueOrNull()
        ?: fail("Example not found or was a reference")

    val value = example.value ?: fail("Example.value should not be null")
    when (value) {
      is ExampleValue.Single -> assertEquals("{\"a\":1,\"b\":{\"c\":\"d\"}}", value.value)
      else -> fail("Expected ExampleValue.Single, but was $value")
    }
  }

  @Test
  fun `array of primitives example value is parsed as multiple`() {
    val json =
      """
      {
        "openapi": "3.1.0",
        "info": { "title": "t", "version": "1.0.0" },
        "components": {
          "examples": {
            "arrPrimitives": {
              "value": ["a", "b", "c"]
            }
          }
        }
      }
      """
        .trimIndent()

    val openAPI = OpenAPI.fromJson(json)
    val example =
      openAPI.components.examples["arrPrimitives"]?.valueOrNull()
        ?: fail("Example not found or was a reference")

    val value = example.value ?: fail("Example.value should not be null")
    when (value) {
      is ExampleValue.Multiple -> assertEquals(listOf("a", "b", "c"), value.values)
      else -> fail("Expected ExampleValue.Multiple, but was $value")
    }
  }

  @Test
  fun `array of objects example value is parsed as string`() {
    val json =
      """
      {
        "openapi": "3.1.0",
        "info": { "title": "t", "version": "1.0.0" },
        "components": {
          "examples": {
            "arrObjects": {
              "value": [ { "a": 1 }, { "b": { "c": "d" } } ]
            }
          }
        }
      }
      """
        .trimIndent()

    val openAPI = OpenAPI.fromJson(json)
    val example =
      openAPI.components.examples["arrObjects"]?.valueOrNull()
        ?: fail("Example not found or was a reference")

    val value = example.value ?: fail("Example.value should not be null")
    when (value) {
      is ExampleValue.Single -> assertEquals("[{\"a\":1},{\"b\":{\"c\":\"d\"}}]", value.value)
      else -> fail("Expected ExampleValue.Single, but was $value")
    }
  }
}
