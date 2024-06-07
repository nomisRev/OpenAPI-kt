package io.github.nomisrev.openapi.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class OpenAPIPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val extension = project.extensions.create("openApiConfig", OpenApiConfig::class.java, project)

    project.tasks.register("generateOpenApiClient", GenerateClientTask::class.java) {
      it.spec.set(extension.spec)
      it.outputDir.set(extension.output)
    }
  }
}
