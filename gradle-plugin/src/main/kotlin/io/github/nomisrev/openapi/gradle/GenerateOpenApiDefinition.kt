package io.github.nomisrev.openapi.gradle

import gratatouille.tasks.GInputFile
import gratatouille.tasks.GOutputDirectory
import gratatouille.tasks.GTask
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.parser.OpenAPI
import kotlinx.coroutines.runBlocking
import kotlin.collections.forEach
import kotlin.collections.mapTo
import kotlin.io.extension
import kotlin.io.readText
import kotlin.text.lowercase
import kotlin.text.uppercase

@GTask(description = "Generates Kotlin source code from an OpenAPI specification", group = "openapi")
fun generateOpenApi(
    specFile: GInputFile,
    modelPackage: String,
    apiPackage: String,
    targets: Set<String>,
    outputDirectory: GOutputDirectory,
) {
    val spec = specFile.readText()
    val openApi = when (specFile.extension.lowercase()) {
        "yaml", "yml" -> OpenAPI.fromYaml(spec)
        "json" -> OpenAPI.fromJson(spec)
        else -> error("Unsupported OpenAPI spec extension '${specFile.extension}'. Use .json, .yaml, or .yml")
    }

    val renderTargets = targets.mapTo(linkedSetOf()) {
        KmpTarget.valueOf(it.uppercase())
    }
    val config = RenderConfig(
        modelPackage = modelPackage,
        apiPackage = apiPackage,
        targets = renderTargets,
    )

    runBlocking {
        openApi.generate(config).forEach { file ->
            file.writeTo(outputDirectory)
        }
    }
}
