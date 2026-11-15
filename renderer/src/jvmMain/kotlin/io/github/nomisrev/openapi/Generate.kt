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
    val enrichedConfig = config.copy(contextSpecificNames = models.contextSpecificNames())
    val diagnostics = generationDiagnostics(enrichedConfig)
    val filteredTree = withSupportedRequestBodiesOnly()
    return GenerationResult(
        files = filteredTree.generateModels(enrichedConfig) + filteredTree.generateClient(enrichedConfig),
        diagnostics = diagnostics,
    )
}

fun ApiTree.generateModels(config: RenderConfig): List<FileSpec> {
    val modelsConfig = config.withOptionalTypeName(this).withNamedModels(models).withPathClassNames(this)
    return modelFiles(modelsConfig).map(ModelFile::file) + additionalFiles(modelsConfig)
}

/** A file of a model, with the shape of the model, which tells whether another model with the same file name is equal. */
internal data class ModelFile(val file: FileSpec, val shape: String) {
    val key: String get() = "${file.packageName}.${file.name}"
}

/**
 * The files of the models, one per class name and shape: the `Read` and `Write` variants of a schema that are equal are
 * rendered under one name, and are one file. Models of different shape under one name are not both emitted, the first
 * one is ([modelFileCollisionDiagnostics] reports the others).
 */
internal fun ApiTree.modelFiles(config: RenderConfig): List<ModelFile> =
    allModelFiles(config).distinctBy(ModelFile::key)

private fun ApiTree.allModelFiles(config: RenderConfig): List<ModelFile> =
    models.mapNotNull { model ->
        model.toFileSpecOrNull(config)?.let { file -> ModelFile(file, model.contextComparisonKey(config.contextSpecificNames)) }
    }

/** An error for each class name that two models of a different shape would be rendered under. */
internal fun ApiTree.modelFileCollisionDiagnostics(config: RenderConfig): List<GenerationDiagnostic> =
    allModelFiles(config.withOptionalTypeName(this).withNamedModels(models).withPathClassNames(this))
        .groupBy(ModelFile::key)
        .filterValues { files -> files.map(ModelFile::shape).distinct().size > 1 }
        .keys.sorted()
        .map { name ->
            GenerationDiagnostic(
                GenerationDiagnosticSeverity.Error,
                "Two different models would be generated as '$name'. Only the first is emitted; report this schema."
            )
        }

private fun ApiTree.additionalFiles(config: RenderConfig): List<FileSpec> {
    val needsSerializationUtils = models.any(Model::requiresSerializationUtils)
    val utils = if (needsSerializationUtils) listOf(generateSerializationUtils(config)) else emptyList()
    val modelPackage = config.modelPackage.toPackageName()
    val optionalPackages = buildSet { models.forEach { it.collectOptionalPackages(config, this) } }
    val optional = if (optionalPackages.isNotEmpty()) listOf(generateOptionalSupport(config, modelPackage)) else emptyList()
    return utils + optional
}

private fun Model.toFileSpecOrNull(config: RenderConfig): FileSpec? = when (this) {
    is Model.Collection -> inner.toFileSpecOrNull(config)
    is Model.Dictionary -> inner.toFileSpecOrNull(config)
    is Model.DiscriminatedObject -> toFileSpec(config)
    is Model.Enum -> toFileSpec(config)
    is Model.Object -> toFileSpec(config)
    is Model.Union -> toFileSpec(config)
    is Model.Reference,
    is Model.ByteArray,
    is Model.RawResponse,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Uuid,
    is Model.Primitive -> null
}

private fun Model.requiresSerializationUtils(): Boolean = when (this) {
    is Model.Collection -> inner.requiresSerializationUtils()
    is Model.Dictionary -> inner.requiresSerializationUtils()
    is Model.DiscriminatedObject ->
        abstractProperties.values.any { it.model.requiresSerializationUtils() } ||
                subtypes.any(Model.Object::requiresSerializationUtils)
    is Model.Enum,
    is Model.Reference,
    is Model.ByteArray,
    is Model.RawResponse,
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
