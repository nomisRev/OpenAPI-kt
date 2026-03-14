package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime

@Serializable
data class GitCommit(
    val sha: String,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val author: Author,
    val committer: Committer,
    val message: String,
    val tree: Tree,
    val parents: List<Parents>,
    val verification: Verification,
    @SerialName("html_url") val htmlUrl: String,
) {
    @Serializable
    data class Author(val date: LocalDateTime, val email: String, val name: String)

    @Serializable
    data class Committer(val date: LocalDateTime, val email: String, val name: String)

    @Serializable
    data class Tree(val sha: String, val url: String)

    @Serializable
    data class Parents(val sha: String, val url: String, @SerialName("html_url") val htmlUrl: String)

    @Serializable
    data class Verification(
        val verified: Boolean,
        val reason: String,
        val signature: String?,
        val payload: String?,
        @SerialName("verified_at") val verifiedAt: String?,
    )
}
