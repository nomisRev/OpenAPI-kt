package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Label(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val name: String,
    val description: String?,
    val color: String,
    val default: Boolean,
)
