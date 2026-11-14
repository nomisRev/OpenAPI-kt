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
 * Timeline Comment Event
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TimelineCommentEvent(
  public val event: String,
  public val actor: SimpleUser,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val body: String? = null,
  @SerialName("body_text")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyText: String? = null,
  @SerialName("body_html")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bodyHtml: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val user: SimpleUser,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("issue_url")
  public val issueUrl: String,
  @SerialName("author_association")
  public val authorAssociation: AuthorAssociation,
  @SerialName("performed_via_github_app")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val performedViaGithubApp: Optional<NullableIntegration> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reactions: ReactionRollup? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pin: Optional<NullablePinnedIssueComment> = Optional.Absent,
)
