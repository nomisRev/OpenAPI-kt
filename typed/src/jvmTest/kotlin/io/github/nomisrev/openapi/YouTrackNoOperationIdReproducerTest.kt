package io.github.nomisrev.openapi

import kotlin.test.Test

class YouTrackNoOperationIdReproducerTest {
  @Test
  fun youtrack_spec_without_operationId_should_fail_in_transformer() {
    val url =
      requireNotNull(this::class.java.getResource("/youtrack-min.json")) {
        "Test resource youtrack-min.json not found on classpath"
      }
    val json = url.readText()
    val openApi = OpenAPI.fromJson(json)
    openApi.routes()
  }
}
