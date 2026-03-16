package io.github.nomisrev.openapi.gradle

import gratatouille.GratatouilleBuildService
import java.io.File
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.api.services.ServiceReference
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkerExecutor

fun Project.registerGenerateOpenApiTask(
    taskName: String = "generateOpenApi",
    taskDescription: String? = "Generates Kotlin source code from an OpenAPI specification",
    taskGroup: String? = "openapi",
    extraClasspath: FileCollection? = null,
    specFile: Provider<RegularFile>,
    modelPackage: Provider<String>,
    apiPackage: Provider<String>,
    targets: Provider<Set<String>>,
): TaskProvider<GenerateOpenApiTask> {
    val classpathConfiguration = configurations.named("gratatouille")
    gradle.sharedServices.registerIfAbsent("gratatouille2", GratatouilleBuildService::class.java) {}

    return tasks.register(taskName, GenerateOpenApiTask::class.java) {
        it.group = taskGroup
        it.description = taskDescription
        it.classpath.from(classpathConfiguration)
        if (extraClasspath != null) {
            it.classpath.from(extraClasspath)
        }
        it.specFile.set(specFile)
        it.modelPackage.set(modelPackage)
        it.apiPackage.set(apiPackage)
        it.targets.set(targets)
        it.outputDirectory.set(layout.buildDirectory.dir("gtask/$taskName/outputDirectory"))
    }
}

@CacheableTask
abstract class GenerateOpenApiTask : DefaultTask() {
    @get:Classpath
    abstract val classpath: ConfigurableFileCollection

    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputFile
    abstract val specFile: org.gradle.api.file.RegularFileProperty

    @get:Input
    abstract val modelPackage: Property<String>

    @get:Input
    abstract val apiPackage: Property<String>

    @get:Input
    abstract val targets: SetProperty<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @ServiceReference("gratatouille2")
    abstract fun getGratatouilleBuildService(): Property<GratatouilleBuildService>

    @Inject
    abstract fun getWorkerExecutor(): WorkerExecutor

    @TaskAction
    fun taskAction() {
        getWorkerExecutor().noIsolation().submit(GenerateOpenApiWorkAction::class.java) {
            it.gratatouilleBuildService.set(getGratatouilleBuildService())
            it.classpath = classpath.files
            it.specFile = specFile.asFile.get()
            it.modelPackage = modelPackage.get()
            it.apiPackage = apiPackage.get()
            it.targets = targets.get()
            it.outputDirectory = outputDirectory.asFile.get()
        }
    }
}

private interface GenerateOpenApiWorkParameters : WorkParameters {
    var classpath: Set<File>
    val gratatouilleBuildService: Property<GratatouilleBuildService>
    var specFile: File
    var modelPackage: String
    var apiPackage: String
    var targets: Set<String>
    var outputDirectory: File
}

private abstract class GenerateOpenApiWorkAction : WorkAction<GenerateOpenApiWorkParameters> {
    override fun execute() {
        with(parameters) {
            gratatouilleBuildService.get().classloader(classpath)
                .loadClass("io.github.nomisrev.openapi.gradle.GenerateOpenApiEntryPoint")
                .declaredMethods.single()
                .invoke(null, specFile, modelPackage, apiPackage, targets, outputDirectory)
        }
    }
}
