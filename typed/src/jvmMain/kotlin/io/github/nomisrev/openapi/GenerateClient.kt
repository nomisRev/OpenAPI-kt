package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.render.attemptDeserialize
import io.github.nomisrev.openapi.routes.toApiModel
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * CLI entry point for generating OpenAPI clients.
 *
 * Usage:
 *   --spec <path>     Path to the OpenAPI spec file
 *   --format <format> Format: "json" or "yaml"
 *   --name <name>     Client name (used for root class)
 *   --output <path>   Output directory for generated code
 *   --package <name>  Package name (default: io.github.nomisrev)
 */
fun main(args: Array<String>): Unit = runBlocking {
    val argsMap = args.toList().chunked(2).associate { it[0] to it[1] }

    val specPath = requireNotNull(argsMap["--spec"]) { "Missing --spec argument" }
    val format = argsMap["--format"] ?: if (specPath.endsWith(".yaml") || specPath.endsWith(".yml")) "yaml" else "json"
    val name = argsMap["--name"] ?: File(specPath).nameWithoutExtension.replaceFirstChar { it.uppercase() }
    val outputPath = requireNotNull(argsMap["--output"]) { "Missing --output argument" }
    val packageName = argsMap["--package"] ?: "io.github.nomisrev"

    println("Generating client from: $specPath")
    println("  Format: $format")
    println("  Name: $name")
    println("  Output: $outputPath")
    println("  Package: $packageName")

    val specContent = File(specPath).readText()
    val spec = when (format.lowercase()) {
        "yaml", "yml" -> OpenAPI.fromYaml(specContent)
        "json" -> OpenAPI.fromJson(specContent)
        else -> error("Unknown format: $format. Use 'json' or 'yaml'")
    }

    val apiModel = spec.toApiModel()
    val modelFiles = apiModel.models.generate(packageName)
    val root = apiModel.routes.sort(spec.info.title, spec.servers)
    val clientFiles = root.generateClient(packageName)

    val allFiles = modelFiles + clientFiles + KFile(
        "AttemptDeserialize.kt",
        "$packageName.model",
        attemptDeserialize(packageName)
    )

    val outputDir = File(outputPath)
    outputDir.deleteRecursively()

    for (kFile in allFiles) {
        val packageDir = File(outputDir, kFile.packageName.replace('.', '/'))
        packageDir.mkdirs()
        val targetFile = File(packageDir, kFile.name)
        targetFile.writeText(kFile.content)
    }

    println("Generated ${allFiles.size} files")
}
