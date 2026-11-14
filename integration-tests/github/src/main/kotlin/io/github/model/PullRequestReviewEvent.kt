package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequestReviewEvent(
  public val action: String,
  public val review: Review,
  @SerialName("pull_request")
  public val pullRequest: PullRequestMinimal,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Review(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @SerialName("node_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nodeId: String? = null,
    public val user: NullableSimpleUser? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val body: String? = null,
    @SerialName("commit_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val commitId: String? = null,
    @SerialName("submitted_at")
    public val submittedAt: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val state: String? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    @SerialName("pull_request_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val pullRequestUrl: String? = null,
    @SerialName("_links")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val links: Links? = null,
    @SerialName("updated_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updatedAt: String? = null,
  ) {
    @Serializable
    public data class Links(
      public val html: Html,
      @SerialName("pull_request")
      public val pullRequest: PullRequest,
    ) {
      @Serializable
      public data class Html(
        public val href: String,
      )

      @Serializable
      public data class PullRequest(
        public val href: String,
      )
    }
  }
}
