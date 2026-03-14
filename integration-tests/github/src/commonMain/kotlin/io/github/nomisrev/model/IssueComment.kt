package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IssueComment(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val body: String? = null,
    @SerialName("body_text") val bodyText: String? = null,
    @SerialName("body_html") val bodyHtml: String? = null,
    @SerialName("html_url") val htmlUrl: String,
    val user: NullableSimpleUser?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("issue_url") val issueUrl: String,
    @SerialName("author_association") val authorAssociation: AuthorAssociation? = null,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration? = null,
    val reactions: ReactionRollup? = null,
    val pin: NullablePinnedIssueComment? = null,
)
