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
    val unions = models.filterIsInstance<Model.Union>()
    val discriminatedObjects = models.filterIsInstance<Model.DiscriminatedObject>()
    val nonDiscriminatedUnions = unions.filter { it.discriminator == null }
    val serializationUtils = if (nonDiscriminatedUnions.isNotEmpty()) {
        listOf(generateSerializationUtils(config))
    } else emptyList()
    return models.filterIsInstance<Model.Enum>().map { it.toFileSpec(config) } +
        unions.map { it.toFileSpec(config) } +
        discriminatedObjects.map { it.toFileSpec(config) } +
        collections.map { it.toCollectionFileSpec(config) } +
        regularObjects.map { it.toFileSpec(config) } +
        serializationUtils
}

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> = emptyList()
