package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import io.github.nomisrev.openapi.parser.OpenAPI

suspend fun OpenAPI.generate(config: RenderConfig = RenderConfig()): List<FileSpec> =
    toApiTree().render(config)

fun ApiTree.render(config: RenderConfig): List<FileSpec> =
    generateModels(config) + generateClient(config)

fun ApiTree.generateModels(config: RenderConfig): List<FileSpec> =
    models.filterIsInstance<Model.Enum>().map { it.toFileSpec(config) }

fun ApiTree.generateClient(config: RenderConfig): List<FileSpec> = emptyList()
