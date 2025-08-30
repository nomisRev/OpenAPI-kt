package io.github.nomisrev.openapi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class CompositeExampleYamlTest {
  @Test
  fun `object example under schema is parsed as string`() {
    val openAPI = OpenAPI.fromYaml(resourceText("issue-composite-example.yaml"))

    val path = openAPI.paths["/hello-world"] ?: fail("Path '/hello-world' not found")
    val put = path.put ?: fail("PUT operation not found")
    val requestBody = put.requestBody?.valueOrNull() ?: fail("RequestBody should be inline value")
    val media =
      requestBody.content["application/json"] ?: fail("MediaType 'application/json' not found")
    val schema = media.schema?.valueOrNull() ?: fail("Schema should be inline value")

    val example = schema.example ?: fail("Schema.example should not be null")
    when (example) {
      is ExampleValue.Single -> assertEquals("{\"name\":\"Foo\"}", example.value)
      else -> fail("Expected ExampleValue.Single, but was $example")
    }
  }
}
