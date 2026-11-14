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
 * Comments provide a way for people to collaborate on an issue.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NullableIssueComment(
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
  public val user: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("issue_url")
  public val issueUrl: String,
  @SerialName("author_association")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val authorAssociation: AuthorAssociation? = null,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: NullableIntegration? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reactions: ReactionRollup? = null,
  public val pin: NullablePinnedIssueComment? = null,
)
