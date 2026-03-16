package io.github.nomisrev.openapi.gradle

import gratatouille.GPlugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@GPlugin(id = "io.github.nomisrev.openapi")
fun openApiPlugin(project: Project) {
    val extension = project.extensions.create("openApi", OpenApiExtension::class.java)

    extension.modelPackage.convention("io.github.nomisrev.openapi.generated.model")
    extension.apiPackage.convention("io.github.nomisrev.openapi.generated.api")
    extension.targets.convention(setOf("JVM", "JS"))

    val generateTask = project.registerGenerateOpenApiTask(
        taskName = "generateOpenApi",
        specFile = extension.specFile,
        modelPackage = extension.modelPackage,
        apiPackage = extension.apiPackage,
        targets = extension.targets,
    )
    val outputDirectory = project.layout.buildDirectory.dir("gtask/generateOpenApi/outputDirectory")

    project.plugins.withId("org.jetbrains.kotlin.jvm") {
        project.extensions.configure(KotlinJvmProjectExtension::class.java) { kotlin ->
            kotlin.sourceSets.named("main").configure { sourceSet ->
                sourceSet.kotlin.srcDir(outputDirectory)
            }
        }
    }

    project.plugins.withId("org.jetbrains.kotlin.multiplatform") {
        project.extensions.configure(KotlinMultiplatformExtension::class.java) { kotlin ->
            kotlin.sourceSets.named("commonMain").configure { sourceSet ->
                sourceSet.kotlin.srcDir(outputDirectory)
            }
        }
    }

    project.tasks.matching { task -> task.name == "compileKotlin" }.configureEach { task ->
        task.dependsOn(generateTask)
    }
    project.tasks.matching { task -> task.name == "compileCommonMainKotlinMetadata" }.configureEach { task ->
        task.dependsOn(generateTask)
    }
}
