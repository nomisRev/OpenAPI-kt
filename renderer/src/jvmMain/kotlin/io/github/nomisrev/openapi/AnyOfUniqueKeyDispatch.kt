@file:Suppress("MatchingDeclarationName")

package io.github.nomisrev.openapi

internal data class AnyOfUniqueKeyDispatchAnalysis(
    val uniqueKeysByCase: Map<Model.Union.Case, Set<String>>,
) {
    fun uniqueKeysFor(case: Model.Union.Case): Set<String> =
        requireNotNull(uniqueKeysByCase[case]) { "Missing unique-key dispatch metadata for anyOf case: $case" }
}

internal fun Model.AnyOf.uniqueKeyDispatchAnalysis(): AnyOfUniqueKeyDispatchAnalysis? {
    return cases.uniqueKeyDispatchAnalysisOrNull()
}

internal fun List<Model.Union.Case>.uniqueKeyDispatchAnalysisOrNull(): AnyOfUniqueKeyDispatchAnalysis? {
    val objectCases = mapNotNull { case ->
        (case.model as? Model.Object)?.let { case to it }
    }
    if (objectCases.size != size) return null

    val uniqueKeysByCase = objectCases.associate { (case, model) ->
        val otherKeys = objectCases
            .asSequence()
            .filterNot { it.first == case }
            .flatMapTo(mutableSetOf()) { it.second.properties.keys }
        case to (model.properties.keys - otherKeys)
    }

    return uniqueKeysByCase
        .takeIf { analysis -> analysis.values.count(Set<String>::isEmpty) <= 1 }
        ?.let(::AnyOfUniqueKeyDispatchAnalysis)
}
