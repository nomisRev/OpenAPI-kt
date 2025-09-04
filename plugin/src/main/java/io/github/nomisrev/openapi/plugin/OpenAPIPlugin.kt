package io.github.nomisrev.openapi.plugin

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider

abstract class OpenAPIPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val extension = project.extensions.create("openApiConfig", OpenApiConfig::class.java, project)
    with(project.tasks) {
      val generateOpenApiClient =
        register("generateOpenApiClient", GenerateClientTask::class.java) {
          it.spec.set(extension.specs)
          it.output.set(
            project.layout.buildDirectory.dir("generated/openapi/src/commonMain/kotlin")
          )
        }

      maybeCreate("prepareKotlinIdeaImport").dependsOn(generateOpenApiClient)
      project.sources().forEach { source ->
        val outputDirectoryProvider: Provider<File> =
          project.layout.buildDirectory.dir("generated/openapi/src/commonMain/kotlin").map {
            it.asFile
          }
        // Add the source dependency on the generated code.
        // Use the task itself to carry task dependencies for compilation
        // See https://github.com/cashapp/sqldelight/issues/2119
        source.sourceDirectorySet.srcDir(generateOpenApiClient)
        source.registerGeneratedDirectory(outputDirectoryProvider)
      }
    }
  }
}
