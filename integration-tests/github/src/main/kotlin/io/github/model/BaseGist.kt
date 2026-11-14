package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * Base Gist
 */
@OptIn(ExperimentalSerializationApi::class)
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
  public val files: Map<String, Files>,
  public val `public`: Boolean,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val description: String?,
  public val comments: Long,
  @SerialName("comments_enabled")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val commentsEnabled: Boolean? = null,
  public val user: NullableSimpleUser?,
  @SerialName("comments_url")
  public val commentsUrl: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val owner: SimpleUser? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val truncated: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val forks: JsonArray? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val history: JsonArray? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Files(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val filename: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val language: String? = null,
    @SerialName("raw_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val rawUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val size: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val encoding: String? = null,
  )
}
