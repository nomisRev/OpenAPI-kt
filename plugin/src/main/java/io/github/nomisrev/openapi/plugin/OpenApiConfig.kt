package io.github.nomisrev.openapi.plugin

import java.io.File
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty

@Suppress("UnnecessaryAbstractClass")
abstract class OpenApiConfig @Inject constructor(private val project: Project) {
  internal val specs: ListProperty<SpecDefinition> =
    project.objects.listProperty(SpecDefinition::class.java).empty()

  fun spec(name: String, file: File, configure: OpenApiBuilder.() -> Unit = {}) {
    val builder = OpenApiBuilder().apply(configure)
    specs.add(SpecDefinition(name, file, builder.packageName))
  }
}

data class OpenApiBuilder(var packageName: String? = null)

data class SpecDefinition(val name: String, val file: File, val packageName: String?)
