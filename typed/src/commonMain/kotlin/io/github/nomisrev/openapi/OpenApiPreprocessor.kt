package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.OpenAPI

fun interface OpenApiPreprocessor {
    fun preprocess(openApi: OpenAPI): OpenAPI

    companion object {
        val None: OpenApiPreprocessor = OpenApiPreprocessor { it }
    }
}

infix fun OpenApiPreprocessor.then(next: OpenApiPreprocessor): OpenApiPreprocessor =
    OpenApiPreprocessor { openApi -> next.preprocess(preprocess(openApi)) }

object OpenApiPreprocessors {
    fun chain(vararg preprocessors: OpenApiPreprocessor): OpenApiPreprocessor =
        preprocessors.fold(OpenApiPreprocessor.None) { current, next -> current.then(next) }

    fun excludePaths(paths: Set<String>): OpenApiPreprocessor = OpenApiPreprocessor { openApi ->
        if (paths.isEmpty()) openApi
        else openApi.copy(paths = openApi.paths.filterKeys { path -> path !in paths })
    }
}

fun OpenAPI.preprocessedBy(preprocessor: OpenApiPreprocessor = OpenApiPreprocessor.None): OpenAPI =
    preprocessor.preprocess(this)

fun OpenAPI.preprocessedBy(preprocessors: Iterable<OpenApiPreprocessor>): OpenAPI =
    preprocessors.fold(this) { current, preprocessor -> preprocessor.preprocess(current) }
