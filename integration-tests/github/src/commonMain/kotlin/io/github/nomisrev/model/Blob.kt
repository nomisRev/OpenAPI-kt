package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Blob(
    val content: String,
    val encoding: String,
    val url: String,
    val sha: String,
    val size: Long?,
    @SerialName("node_id") val nodeId: String,
    @SerialName("highlighted_content") val highlightedContent: String? = null,
)
