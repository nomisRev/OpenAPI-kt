package io.github.nomisrev.openapi.plugin

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.workers.WorkerExecutor

@CacheableTask
abstract class GenerateClientTask : DefaultTask() {
  init {
    description = "Generate a Kotlin client based on a configure OpenAPI Specification."
    group = "openapi"
  }

  @get:Input
  @get:SkipWhenEmpty
  @get:Option(option = "OpenApiSpec", description = "The OpenAPI configuration")
  abstract val spec: ListProperty<SpecDefinition>

  @get:OutputDirectory
  abstract val output: DirectoryProperty

  @Inject abstract fun getWorkerExecutor(): WorkerExecutor

  @TaskAction
  fun sampleAction() {
    val workQueue = getWorkerExecutor().noIsolation()
    val specPath = requireNotNull(spec.orNull) { "No OpenAPI Config found" }
    require(specPath.isNotEmpty()) { "No OpenAPI Config found" }
    specPath.forEach { spec ->
      workQueue.submit(GenerateClientAction::class.java) { parameters ->
        parameters.name.set(spec.name)
        parameters.packageName.set(spec.packageName)
        parameters.file.set(spec.file)
        parameters.output.set(project.output)
      }
    }
  }
}
