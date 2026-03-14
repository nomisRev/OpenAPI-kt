package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Tag(
    val name: String,
    val commit: Commit,
    @SerialName("zipball_url") val zipballUrl: String,
    @SerialName("tarball_url") val tarballUrl: String,
    @SerialName("node_id") val nodeId: String,
) {
    @Serializable
    data class Commit(val sha: String, val url: String)
}
