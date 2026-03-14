package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GitRef(
    val ref: String,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    @SerialName("object") val `object`: Object,
) {
    @Serializable
    data class Object(val type: String, val sha: String, val url: String)
}
