package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class GitTree(val sha: String, val url: String? = null, val truncated: Boolean, val tree: List<Tree>) {
    @Serializable
    data class Tree(
        val path: String,
        val mode: String,
        val type: String,
        val sha: String,
        val size: Long? = null,
        val url: String? = null,
    )
}
