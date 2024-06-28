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
        }

      maybeCreate("prepareKotlinIdeaImport").dependsOn(generateOpenApiClient)
      project.sources().forEach { source ->
        val outputDirectoryProvider: Provider<File> =
          generateOpenApiClient.map { _ -> project.output }
        // Add the source dependency on the generated code.
        // Use a Provider generated from the task to carry task dependencies
        // See https://github.com/cashapp/sqldelight/issues/2119
        source.sourceDirectorySet.srcDir(generateOpenApiClient)
        source.registerGeneratedDirectory(outputDirectoryProvider)
      }
    }
  }
}

@Suppress("DEPRECATION")
internal val Project.output
  get() = File(buildDir, "generated/openapi/src/commonMain/kotlin").also { it.mkdirs() }
