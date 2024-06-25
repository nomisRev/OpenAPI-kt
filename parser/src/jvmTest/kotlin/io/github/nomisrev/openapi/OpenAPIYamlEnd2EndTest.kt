package io.github.nomisrev.openapi

import kotlin.test.Test

class OpenAPIYamlEnd2EndTest {
  @Test
  fun petstoreYaml() {
    OpenAPI.fromYaml(resourceText("petstore.yaml"))
  }

  @Test
  fun openAIYaml() {
    OpenAPI.fromYaml(resourceText("openai.yaml"))
  }
}
