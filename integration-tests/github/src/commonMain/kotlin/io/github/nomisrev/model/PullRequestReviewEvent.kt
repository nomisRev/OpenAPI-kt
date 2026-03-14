package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class PullRequestReviewEvent(
    val action: String,
    val review: Review,
    @SerialName("pull_request") val pullRequest: PullRequestMinimal,
) {
    @Serializable
    data class Review(
        val id: Long? = null,
        @SerialName("node_id") val nodeId: String? = null,
        val user: NullableSimpleUser? = null,
        val body: String? = null,
        @SerialName("commit_id") val commitId: String? = null,
        @SerialName("submitted_at") val submittedAt: String? = null,
        val state: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
        @SerialName("pull_request_url") val pullRequestUrl: String? = null,
        @SerialName("_links") val links: Links? = null,
        @SerialName("updated_at") val updatedAt: String? = null,
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
}
