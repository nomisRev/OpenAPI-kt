package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import io.github.nomisrev.openapi.parser.OpenAPI

suspend fun OpenAPI.generate(config: RenderConfig = RenderConfig()): List<FileSpec> =
    toApiTree().render(config)

fun ApiTree.render(config: RenderConfig): List<FileSpec> =
    generateModels(config) + generateClient(config)

fun ApiTree.generateModels(config: RenderConfig): List<FileSpec> {
    val objects = models.filterIsInstance<Model.Object>()
    val (collections, regularObjects) = objects.partition { it.isTopLevelCollectionWrapper() }
    return models.filterIsInstance<Model.Enum>().map { it.toFileSpec(config) } +
        collections.map { it.toCollectionFileSpec(config) } +
        regularObjects.map { it.toFileSpec(config) }
}

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> = emptyList()
