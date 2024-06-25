package io.github.nomisrev.openapi.plugin

import io.github.nomisrev.openapi.generate
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class GenerateClientTask : DefaultTask() {
  init {
    description = "A task to generate a Kotlin client based on OpenAPI"
    group = BasePlugin.ASSEMBLE_TASK_NAME
  }

  @get:InputFile
  @get:Option(
    option = "spec",
    description = "The OpenAPI json file. Future will be supported soon."
  )
  abstract val spec: RegularFileProperty

  @TaskAction
  fun sampleAction() {
    val specPath =
      requireNotNull(spec.orNull?.asFile?.toPath()?.toString()) {
        "No OpenAPI Specification specified. Please provide a spec file."
      }
    val output =
      project.layout.buildDirectory
        .dir("generated/openapi/src/commonMain/kotlin")
        .get()
        .asFile
        .also { it.mkdirs() }
        .path
    generate(specPath, output)
  }
}
