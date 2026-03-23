package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A release.
 */
@Serializable
public data class Release(
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("assets_url")
  public val assetsUrl: String,
  @SerialName("upload_url")
  public val uploadUrl: String,
  @SerialName("tarball_url")
  public val tarballUrl: String?,
  @SerialName("zipball_url")
  public val zipballUrl: String?,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("tag_name")
  public val tagName: String,
  @SerialName("target_commitish")
  public val targetCommitish: String,
  public val name: String?,
  public val body: String? = null,
  public val draft: Boolean,
  public val prerelease: Boolean,
  public val immutable: Boolean? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("published_at")
  public val publishedAt: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
  public val author: SimpleUser,
  public val assets: List<ReleaseAsset>,
  @SerialName("body_html")
  public val bodyHtml: String? = null,
  @SerialName("body_text")
  public val bodyText: String? = null,
  @SerialName("mentions_count")
  public val mentionsCount: Long? = null,
  @SerialName("discussion_url")
  public val discussionUrl: String? = null,
  public val reactions: ReactionRollup? = null,
)
