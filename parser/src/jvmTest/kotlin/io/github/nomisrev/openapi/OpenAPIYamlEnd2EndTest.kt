package io.github.nomisrev.openapi

import kotlin.test.Test

class OpenAPIYamlEnd2EndTest {
  @Test
  fun petstoreJson() {
    OpenAPI.fromYaml(resourceText("petstore.yaml"))
  }
}
