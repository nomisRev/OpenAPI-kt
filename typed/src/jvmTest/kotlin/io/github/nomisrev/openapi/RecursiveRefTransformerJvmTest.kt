package io.github.nomisrev.openapi

import java.io.BufferedReader
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RecursiveRefTransformerJvmTest {
  @Test
  fun transformer_handles_recursiveRef() {
    val yaml = resourceText("recursive-ref.yml")
    val api = OpenAPI.fromYaml(yaml)

    // Should not throw; transformer should resolve $recursiveRef '#' against current anchor
    val models = api.models()
    kotlin.test.assertTrue(models.size >= 2, "Expected at least the two top-level models to be produced")
  }
}

private fun resourceText(path: String): String =
  requireNotNull(
    RecursiveRefTransformerJvmTest::class
      .java
      .classLoader
      .getResourceAsStream(path)
      ?.bufferedReader()
      ?.use(BufferedReader::readText)
  ) {
    "Resource not found: $path"
  }
