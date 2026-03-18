package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import io.github.nomisrev.openapi.parser.OpenAPI

suspend fun OpenAPI.generate(config: RenderConfig = RenderConfig()): List<FileSpec> =
    toApiTree().render(config)

fun ApiTree.render(config: RenderConfig): List<FileSpec> =
    generateModels(config) + generateClient(config)

fun ApiTree.generateModels(config: RenderConfig): List<FileSpec> =
    models.mapNotNull { model -> model.toFileSpecOrNull(config) } + additionalFiles(config)

private fun ApiTree.additionalFiles(config: RenderConfig): List<FileSpec> {
    val hasCustomObjectSerializer = models.filterIsInstance<Model.Object>()
        .any { it.additionalProperties != Model.Object.AdditionalProperties.Allowed(false) }
    val hasCustomUnionSerializer =
        models.filterIsInstance<Model.Union>().any { it.discriminator == null }
    return if (hasCustomObjectSerializer || hasCustomUnionSerializer) {
        listOf(generateSerializationUtils(config))
    } else emptyList()
}

private fun Model.toFileSpecOrNull(config: RenderConfig): FileSpec? = when (this) {
    is Model.Collection -> inner.toFileSpecOrNull(config)
    is Model.DiscriminatedObject -> toFileSpec(config)
    is Model.Enum -> toFileSpec(config)
    is Model.Object -> toFileSpec(config)
    is Model.Union -> toFileSpec(config)
    is Model.Reference,
    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Uuid,
    is Model.Primitive -> null
}

