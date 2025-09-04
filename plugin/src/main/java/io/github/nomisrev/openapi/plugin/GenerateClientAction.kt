package io.github.nomisrev.openapi.plugin

import GenerationConfig as NewGenerationConfig
import generate as newGenerate
import io.github.nomisrev.openapi.GenerationConfig
import io.github.nomisrev.openapi.generate
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

abstract class GenerateClientAction : WorkAction<GenerateClientAction.Parameters> {
  override fun execute() {
    if (!parameters.newCodegen.get())
      generate(
        GenerationConfig(
          path = parameters.file.get().asFile.path,
          output = parameters.output.get().asFile.path,
          `package` = parameters.packageName.getOrElse("io.github.nomisrev.openapi"),
          name = parameters.name.get(),
          isK2 = true,
        )
      )
    else
      newGenerate(
        NewGenerationConfig(
          path = parameters.file.get().asFile.path,
          output = parameters.output.get().asFile.path,
          `package` = parameters.packageName.getOrElse("io.github.nomisrev.openapi"),
          name = parameters.name.get(),
        )
      )
  }

  interface Parameters : WorkParameters {
    val name: Property<String>
    val file: RegularFileProperty
    val packageName: Property<String>
    val output: DirectoryProperty

    @Deprecated("Will be removed soon with removal of KotlinPoet") val newCodegen: Property<Boolean>
  }
}
