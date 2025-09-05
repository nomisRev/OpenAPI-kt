package io.github.nomisrev.openapi

import io.ktor.utils.io.readText
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

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
      candidates.firstOrNull { SystemFileSystem.exists(it) }
        ?: fail(
          "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}"
        )

    val json = SystemFileSystem.source(path).buffered().use { it.readText() }
    val openApi = OpenAPI.fromJson(json)

    val routes = openApi.routes()
    assertTrue(routes.isNotEmpty(), "Expected YouTrack spec to produce at least one route")
  }
}
