package io.github.nomisrev.openapi.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class GenerateClientTask : DefaultTask() {
    init {
        description = "A task to generate a Kotlin client based on OpenAPI"
        group = BasePlugin.ASSEMBLE_TASK_NAME
    }

    @get:InputFile
    @get:Option(option = "spec", description = "The OpenAPI json file. Future will be supported soon.")
//    @get:Optional  // Property<String>
    abstract val spec: RegularFileProperty

    @get:OutputFile
    abstract val outputDir: RegularFileProperty

    @TaskAction
    fun sampleAction() {

        outputDir.get().asFile.writeText("${spec.orNull}")
    }
}
