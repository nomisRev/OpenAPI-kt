package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.transformers.nestedOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal data class InlineParameterModel(
    val name: String,
    val model: Model,
) {
    val simpleName: String
        get() = name.toPascalCase()
}

private val InlineParameterSharingJson = Json { encodeDefaults = true }
private val MethodPattern = Regex("\"method\":\"[^\"]+\"")

internal fun InlineParameterModel.sharingKey(): String =
    "$name:${InlineParameterSharingJson.encodeToString(model).replace(MethodPattern, "\"method\":\"*\"")}"

internal fun Route.inlineParameterModels(): List<InlineParameterModel> =
    parameters
        .filter { it.input != Parameter.Input.Path }
        .mapNotNull { input ->
            input.type.nestedOrNull()?.let { nested ->
                InlineParameterModel(input.name, nested)
            }
        }

internal fun Iterable<Route>.sharedInlineParameterModels(): List<InlineParameterModel> {
    val shared = linkedMapOf<String, Pair<InlineParameterModel, MutableSet<Int>>>()
    forEachIndexed { index, route ->
        route.inlineParameterModels()
            .distinctBy(InlineParameterModel::sharingKey)
            .forEach { inline ->
                val key = inline.sharingKey()
                val entry = shared.getOrPut(key) { inline to linkedSetOf() }
                entry.second.add(index)
            }
    }
    return shared
        .values
        .filter { [_, indexes] -> indexes.size > 1 }
        .map { [inline, _] -> inline }
        .toList()
}

internal fun Route.routeSpecificInlineParameterModels(
    sharedInlineParameterKeys: Set<String>,
): List<InlineParameterModel> =
    inlineParameterModels()
        .distinctBy(InlineParameterModel::sharingKey)
        .filterNot { it.sharingKey() in sharedInlineParameterKeys }

internal fun Route.Input.inlineParameterClassName(
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): ClassName? =
    type.nestedOrNull()
        ?.takeIf { (it as? Model.ContextHolder)?.context?.head is NamingContext.Path }
        ?.let { nested ->
            val ownerClassName =
                if (InlineParameterModel(name, nested).sharingKey() in sharedInlineParameterKeys) {
                    pathClassName
                } else {
                    methodClassName
                }
            ownerClassName.nestedClass(name.toPascalCase())
        }

internal fun Route.Input.inlineParameterTypeName(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): TypeName? =
    type.nestedOrNull()
        ?.takeIf { (it as? Model.ContextHolder)?.context?.head is NamingContext.Path }
        ?.let { nested ->
            val context = nested as Model.ContextHolder
            inlineParameterClassName(
                pathClassName = pathClassName,
                methodClassName = methodClassName,
                sharedInlineParameterKeys = sharedInlineParameterKeys,
            )?.let { targetClassName ->
                type.toTypeName(config).remapNestedClassName(
                    context.context.toClassName(config),
                    targetClassName,
                )
            }
        }
