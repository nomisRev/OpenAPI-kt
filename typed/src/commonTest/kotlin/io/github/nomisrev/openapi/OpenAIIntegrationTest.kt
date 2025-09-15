package io.github.nomisrev.openapi

import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class OpenAIIntegrationTest {
  @Test
  fun openai_spec_routes_should_build() {
    // Try to locate the youtrack.json that exists at the repository root without duplicating it as
    // a resource.
    val candidates =
      listOf(
        Path("openai-api.yml"), // when working directory is the repository root
        Path("../openai-api.yml"), // when working directory is the module directory
      )
    val path =
      candidates.firstOrNull { it.exists() }
        ?: fail(
          "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}"
        )

    val json = path.readText()
    val openApi = OpenAPI.fromYaml(json)
    openApi.models()
  }
}
