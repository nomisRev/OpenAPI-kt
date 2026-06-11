package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * Base Gist
 */
@Serializable
public data class BaseGist(
  public val url: String,
  @SerialName("forks_url")
  public val forksUrl: String,
  @SerialName("commits_url")
  public val commitsUrl: String,
  public val id: String,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("git_pull_url")
  public val gitPullUrl: String,
  @SerialName("git_push_url")
  public val gitPushUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @Required
  public val files: List<Files> = emptyList(),
  public val `public`: Boolean,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val description: String?,
  public val comments: Long,
  @SerialName("comments_enabled")
  public val commentsEnabled: Boolean? = null,
  public val user: NullableSimpleUser?,
  @SerialName("comments_url")
  public val commentsUrl: String,
  public val owner: SimpleUser? = null,
  public val truncated: Boolean? = null,
  public val forks: JsonArray? = null,
  public val history: JsonArray? = null,
) {
  @Serializable
  public data class Files(
    public val filename: String? = null,
    public val type: String? = null,
    public val language: String? = null,
    @SerialName("raw_url")
    public val rawUrl: String? = null,
    public val size: Long? = null,
    public val encoding: String? = null,
  )
}
