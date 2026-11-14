package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * Gist Simple
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GistSimple(
  public val forks: List<Forks>? = null,
  public val history: List<GistHistory>? = null,
  @SerialName("fork_of")
  public val forkOf: ForkOf? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @SerialName("forks_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val forksUrl: String? = null,
  @SerialName("commits_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val commitsUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @SerialName("node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nodeId: String? = null,
  @SerialName("git_pull_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val gitPullUrl: String? = null,
  @SerialName("git_push_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val gitPushUrl: String? = null,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val files: Map<String, Files?>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `public`: Boolean? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: String? = null,
  @SerialName("updated_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updatedAt: String? = null,
  public val description: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val comments: Long? = null,
  @SerialName("comments_enabled")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val commentsEnabled: Boolean? = null,
  public val user: String? = null,
  @SerialName("comments_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val commentsUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val owner: SimpleUser? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val truncated: Boolean? = null,
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
    public val truncated: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val content: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val encoding: String? = null,
  )

  /**
   * Gist
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ForkOf(
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
    public val owner: NullableSimpleUser? = null,
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
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Forks(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val user: PublicUser? = null,
    @SerialName("created_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updatedAt: Instant? = null,
  )
}
