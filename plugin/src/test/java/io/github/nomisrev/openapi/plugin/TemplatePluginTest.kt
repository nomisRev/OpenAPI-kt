package io.github.nomisrev.openapi.plugin

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import org.gradle.testfixtures.ProjectBuilder

class TemplatePluginTest {
  @Test
  fun `plugin is applied correctly to the project`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("io.github.nomisrev.openapi-kt-plugin")
    assert(project.tasks.getByName("generateOpenApiClient") is GenerateClientTask)
  }

  @Test
  fun `extension openApiConfig is created correctly`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("io.github.nomisrev.openapi-kt-plugin")
    assert(project.extensions.getByName("openApiConfig") is OpenApiConfig)
  }

  @Test
  fun `parameters are passed correctly from extension to task`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("io.github.nomisrev.openapi-kt-plugin")
    val input = File(project.projectDir, "input.tmp")
    (project.extensions.getByName("openApiConfig") as OpenApiConfig).apply { spec.set(input) }

    val task = project.tasks.getByName("generateOpenApiClient") as GenerateClientTask

    assertEquals(input, task.spec.get().asFile)
  }
}
