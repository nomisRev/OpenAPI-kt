package io.github.nomisrev.openapi.plugin

import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty

@Suppress("UnnecessaryAbstractClass")
abstract class OpenApiConfig @Inject constructor(project: Project) {
  private val objects = project.objects

  val spec: RegularFileProperty = objects.fileProperty()
}
