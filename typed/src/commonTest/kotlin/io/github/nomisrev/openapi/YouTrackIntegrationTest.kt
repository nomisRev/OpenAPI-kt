package io.github.nomisrev.openapi

import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class YouTrackIntegrationTest {
  @Test
  fun youtrack_spec_routes_should_build() {
    // Try to locate the youtrack.json that exists at the repository root without duplicating it as
    // a resource.
    val candidates =
      listOf(
          Path("youtrack.json"), // when working directory is the repository root
          Path("../youtrack.json"), // when working directory is the module directory
      )
    val path =
      candidates.firstOrNull { it.exists() }
        ?: fail(
            "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}"
        )

    val json = path.readText()
    val openApi = OpenAPI.fromJson(json)

    val routes = openApi.routes()
      assertTrue(routes.isNotEmpty(), "Expected YouTrack spec to produce at least one route")
  }
}