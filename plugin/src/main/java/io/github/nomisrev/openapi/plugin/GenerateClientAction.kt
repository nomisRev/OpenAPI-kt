package io.github.nomisrev.openapi.plugin

import io.github.nomisrev.openapi.generation.GenerationConfig
import io.github.nomisrev.openapi.generation.generate
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
        `package` = parameters.packageName.getOrElse("io.github.nomisrev.openapi"),
        name = parameters.name.get(),
        isK2 = parameters.k2.get(),
      )
    )
  }

  interface Parameters : WorkParameters {
    val name: Property<String>
    val file: RegularFileProperty
    val packageName: Property<String>
    val output: DirectoryProperty
    // isK2 results in runtime Gradle error..
    val k2: Property<Boolean>
  }
}
