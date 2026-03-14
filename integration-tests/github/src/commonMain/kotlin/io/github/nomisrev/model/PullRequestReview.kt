package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class PullRequestReview(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val user: NullableSimpleUser?,
    val body: String,
    val state: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("pull_request_url") val pullRequestUrl: String,
    @SerialName("_links") val links: Links,
    @SerialName("submitted_at") val submittedAt: LocalDateTime? = null,
    @SerialName("commit_id") val commitId: String?,
    @SerialName("body_html") val bodyHtml: String? = null,
    @SerialName("body_text") val bodyText: String? = null,
    @SerialName("author_association") val authorAssociation: AuthorAssociation,
) {
    @Serializable
    data class Links(val html: Html, @SerialName("pull_request") val pullRequest: PullRequest) {
        @Serializable
        @JvmInline
        value class Html(val href: String)

        @Serializable
        @JvmInline
        value class PullRequest(val href: String)
    }
}
