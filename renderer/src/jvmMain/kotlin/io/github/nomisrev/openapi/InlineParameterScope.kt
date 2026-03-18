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
        .filter { (_, indexes) -> indexes.size > 1 }
        .map { (inline, _) -> inline }
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
): ClassName? {
    val nestedModel = type.nestedOrNull() ?: return null
    val context = (nestedModel as? Model.ContextHolder)?.context ?: return null
    if (context.head !is NamingContext.Path) return null
    val ownerClassName = if (InlineParameterModel(name, nestedModel).sharingKey() in sharedInlineParameterKeys) {
        pathClassName
    } else {
        methodClassName
    }
    return ownerClassName.nestedClass(name.toPascalCase())
}

internal fun Route.Input.inlineParameterTypeName(
    config: RenderConfig,
    pathClassName: ClassName,
    methodClassName: ClassName,
    sharedInlineParameterKeys: Set<String>,
): TypeName? {
    val nestedModel = type.nestedOrNull() ?: return null
    val context = (nestedModel as? Model.ContextHolder)?.context ?: return null
    if (context.head !is NamingContext.Path) return null
    val sourceClassName = context.toClassName(config)
    val targetClassName = inlineParameterClassName(
        pathClassName = pathClassName,
        methodClassName = methodClassName,
        sharedInlineParameterKeys = sharedInlineParameterKeys,
    ) ?: return null
    return type.toTypeName(config).remapNestedClassName(sourceClassName, targetClassName)
}
