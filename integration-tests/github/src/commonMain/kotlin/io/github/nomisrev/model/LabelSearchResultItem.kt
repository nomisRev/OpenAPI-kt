package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LabelSearchResultItem(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val name: String,
    val color: String,
    val default: Boolean,
    val description: String?,
    val score: Double,
    @SerialName("text_matches") val textMatches: SearchResultTextMatches? = null,
)
