package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import io.github.nomisrev.openapi.parser.OpenAPI

suspend fun OpenAPI.generate(
    config: RenderConfig = RenderConfig(),
    preprocessor: OpenApiPreprocessor = OpenApiPreprocessor.None,
): List<FileSpec> =
    generateWithDiagnostics(config, preprocessor)
        .also(GenerationResult::throwOnErrors)
        .files

suspend fun OpenAPI.generateWithDiagnostics(
    config: RenderConfig = RenderConfig(),
    preprocessor: OpenApiPreprocessor = OpenApiPreprocessor.None,
): GenerationResult =
    toApiTree(preprocessor = preprocessor).renderWithDiagnostics(config)

fun ApiTree.render(config: RenderConfig): List<FileSpec> =
    renderWithDiagnostics(config)
        .also(GenerationResult::throwOnErrors)
        .files

fun ApiTree.renderWithDiagnostics(config: RenderConfig): GenerationResult {
    val diagnostics = requestBodyDiagnostics()
    val enrichedConfig = config.copy(contextSpecificNames = models.contextSpecificNames())
    val filteredTree = withSupportedRequestBodiesOnly()
    return GenerationResult(
        files = filteredTree.generateModels(enrichedConfig) + filteredTree.generateClient(enrichedConfig),
        diagnostics = diagnostics,
    )
}

fun ApiTree.generateModels(config: RenderConfig): List<FileSpec> =
    models.mapNotNull { model -> model.toFileSpecOrNull(config) } + additionalFiles(config)

private fun ApiTree.additionalFiles(config: RenderConfig): List<FileSpec> {
    val needsSerializationUtils = models.any(Model::requiresSerializationUtils)
    return if (needsSerializationUtils) {
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

private fun Model.requiresSerializationUtils(): Boolean = when (this) {
    is Model.Collection -> inner.requiresSerializationUtils()
    is Model.DiscriminatedObject ->
        abstractProperties.values.any { it.model.requiresSerializationUtils() } ||
                subtypes.any(Model.Object::requiresSerializationUtils)
    is Model.Enum,
    is Model.Reference,
    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Uuid,
    is Model.Primitive -> false
    is Model.Object ->
        additionalProperties != Model.Object.AdditionalProperties.Allowed(false) ||
                properties.values.any { it.model.requiresSerializationUtils() } ||
                ((additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.requiresSerializationUtils() == true)
    is Model.Union ->
        dispatch !is UnionDispatch.NativeDiscriminator || cases.any { it.model.requiresSerializationUtils() }
}
