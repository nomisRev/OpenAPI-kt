package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FileCommit(val content: Content?, val commit: Commit) {
    @Serializable
    data class Content(
        val name: String? = null,
        val path: String? = null,
        val sha: String? = null,
        val size: Long? = null,
        val url: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        @SerialName("git_url") val gitUrl: String? = null,
        @SerialName("download_url") val downloadUrl: String? = null,
        val type: String? = null,
        @SerialName("_links") val links: Links? = null,
    ) {
        @Serializable
        data class Links(val self: String? = null, val git: String? = null, val html: String? = null)
    }

    @Serializable
    data class Commit(
        val sha: String? = null,
        @SerialName("node_id") val nodeId: String? = null,
        val url: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        val author: Author? = null,
        val committer: Committer? = null,
        val message: String? = null,
        val tree: Tree? = null,
        val parents: List<Parents>? = null,
        val verification: Verification? = null,
    ) {
        @Serializable
        data class Author(val date: String? = null, val name: String? = null, val email: String? = null)

        @Serializable
        data class Committer(val date: String? = null, val name: String? = null, val email: String? = null)

        @Serializable
        data class Tree(val url: String? = null, val sha: String? = null)

        @Serializable
        data class Parents(
            val url: String? = null,
            @SerialName("html_url") val htmlUrl: String? = null,
            val sha: String? = null,
        )

        @Serializable
        data class Verification(
            val verified: Boolean? = null,
            val reason: String? = null,
            val signature: String? = null,
            val payload: String? = null,
            @SerialName("verified_at") val verifiedAt: String? = null,
        )
    }
}
