package io.github.nomisrev.openapi.plugin

import io.github.nomisrev.openapi.GenerationConfig
import io.github.nomisrev.openapi.generate
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

abstract class GenerateClientAction : WorkAction<GenerateClientAction.Parameters> {
  override fun execute() {
    generate(
      GenerationConfig(
        path = parameters.file.get().asFile.path,
        output = parameters.output.get().asFile.path,
        `package` = parameters.packageName.get() ?: "io.github.nomisrev.openapi",
        name = parameters.name.get()
      )
    )
  }

  interface Parameters : WorkParameters {
    val name: Property<String>
    val file: RegularFileProperty
    val packageName: Property<String>
    val output: DirectoryProperty
  }
}