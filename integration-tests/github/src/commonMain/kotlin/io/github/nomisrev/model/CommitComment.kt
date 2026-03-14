package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CommitComment(
    @SerialName("html_url") val htmlUrl: String,
    val url: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val body: String,
    val path: String?,
    val position: Long?,
    val line: Long?,
    @SerialName("commit_id") val commitId: String,
    val user: NullableSimpleUser?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("author_association") val authorAssociation: AuthorAssociation,
    val reactions: ReactionRollup? = null,
)
