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
}