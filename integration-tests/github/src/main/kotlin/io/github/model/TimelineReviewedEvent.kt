package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Timeline Reviewed Event
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TimelineReviewedEvent(
  public val event: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val user: SimpleUser,
  public val body: String?,
  public val state: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("pull_request_url")
  public val pullRequestUrl: String,
  @SerialName("_links")
  public val links: Links,
  @SerialName("submitted_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val submittedAt: Instant? = null,
  @SerialName("updated_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updatedAt: Optional<Instant> = Optional.Absent,
  @SerialName("commit_id")
  public val commitId: String,
  @SerialName("body_html")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyHtml: String? = null,
  @SerialName("body_text")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyText: String? = null,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
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
