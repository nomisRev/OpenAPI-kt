package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.datetime.LocalDateTime

@Serializable
data class CommitSearchResultItem(
    val url: String,
    val sha: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("comments_url") val commentsUrl: String,
    val commit: Commit,
    val author: NullableSimpleUser?,
    val committer: NullableGitUser?,
    val parents: List<Parents>,
    val repository: MinimalRepository,
    val score: Double,
    @SerialName("node_id") val nodeId: String,
    @SerialName("text_matches") val textMatches: SearchResultTextMatches? = null,
) {
    @Serializable
    data class Commit(
        val author: Author,
        val committer: NullableGitUser?,
        @SerialName("comment_count") val commentCount: Long,
        val message: String,
        val tree: Tree,
        val url: String,
        val verification: Verification? = null,
    ) {
        @Serializable
        data class Author(val name: String, val email: String, val date: LocalDateTime)

        @Serializable
        data class Tree(val sha: String, val url: String)
    }

    @Serializable
    data class Parents(
        val url: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        val sha: String? = null,
    )
}
