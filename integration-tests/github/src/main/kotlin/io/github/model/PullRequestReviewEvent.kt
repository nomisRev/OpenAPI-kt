package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequestReviewEvent(
  public val action: String,
  public val review: Review,
  @SerialName("pull_request")
  public val pullRequest: PullRequestMinimal,
) {
  @Serializable
  public data class Review(
    public val id: Long? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val user: NullableSimpleUser? = null,
    public val body: String? = null,
    @SerialName("commit_id")
    public val commitId: String? = null,
    @SerialName("submitted_at")
    public val submittedAt: String? = null,
    public val state: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
    @SerialName("pull_request_url")
    public val pullRequestUrl: String? = null,
    @SerialName("_links")
    public val links: Links? = null,
    @SerialName("updated_at")
    public val updatedAt: String? = null,
  ) {
    @Serializable
    public data class Links(
      public val html: Html,
      @SerialName("pull_request")
      public val pullRequest: PullRequest,
    ) {
      @JvmInline
      @Serializable
      public value class Html(
        public val href: String,
      )

      @JvmInline
      @Serializable
      public value class PullRequest(
        public val href: String,
      )
    }
  }
}
