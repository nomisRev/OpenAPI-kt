package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName

@Serializable
data class CommitCommentEvent(val action: String, val comment: Comment) {
    @Serializable
    data class Comment(
        @SerialName("html_url") val htmlUrl: String? = null,
        val url: String? = null,
        val id: Long? = null,
        @SerialName("node_id") val nodeId: String? = null,
        val body: String? = null,
        val path: String? = null,
        val position: Long? = null,
        val line: Long? = null,
        @SerialName("commit_id") val commitId: String? = null,
        val user: NullableSimpleUser? = null,
        @SerialName("created_at") val createdAt: LocalDateTime? = null,
        @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
        val reactions: ReactionRollup? = null,
    )
}
