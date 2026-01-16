package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SearchSuggestionsResponse(
    val id: String? = null,
    val caret: Int? = null,
    val ignoreUnresolvedSetting: Boolean? = null,
    val query: String? = null,
    val suggestions: List<SuggestionResponse>? = null,
    val folders: List<IssueFolderResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
