package io.github.nomisrev.openapi.plugin

import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import javax.inject.Inject

const val DEFAULT_OUTPUT_DIR = "generated"

@Suppress("UnnecessaryAbstractClass")
abstract class OpenApiConfig @Inject constructor(project: Project) {
  private val objects = project.objects

  val spec: RegularFileProperty =
    objects.fileProperty()

  val output: RegularFileProperty =
    objects.fileProperty()
      .convention(project.layout.buildDirectory.file(DEFAULT_OUTPUT_DIR))
}
