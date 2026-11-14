package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReleaseEvent(
  public val action: String,
  public val release: Release,
) {
  /**
   * A release.
   */
  @OptIn(ExperimentalSerializationApi::class)
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
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val body: Optional<String> = Optional.Absent,
    public val draft: Boolean,
    public val prerelease: Boolean,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val immutable: Boolean? = null,
    @SerialName("created_at")
    public val createdAt: Instant,
    @SerialName("published_at")
    public val publishedAt: Instant?,
    @SerialName("updated_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updatedAt: Optional<Instant> = Optional.Absent,
    public val author: SimpleUser,
    public val assets: List<ReleaseAsset>,
    @SerialName("body_html")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bodyHtml: String? = null,
    @SerialName("body_text")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bodyText: String? = null,
    @SerialName("mentions_count")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val mentionsCount: Long? = null,
    @SerialName("discussion_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val discussionUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reactions: ReactionRollup? = null,
    @SerialName("is_short_description_html_truncated")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val isShortDescriptionHtmlTruncated: Boolean? = null,
    @SerialName("short_description_html")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shortDescriptionHtml: String? = null,
  )
}
