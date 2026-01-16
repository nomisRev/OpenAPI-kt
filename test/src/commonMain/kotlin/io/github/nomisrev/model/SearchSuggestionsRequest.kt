package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchSuggestionsRequest(
    val caret: Int? = null,
    val ignoreUnresolvedSetting: Boolean? = null,
    val query: String? = null,
    val folders: List<IssueFolderRequest>? = null,
)
