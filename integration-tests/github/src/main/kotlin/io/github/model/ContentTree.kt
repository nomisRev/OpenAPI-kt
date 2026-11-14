package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Content Tree
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ContentTree(
  public val type: String,
  public val size: Long,
  public val name: String,
  public val path: String,
  public val sha: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val content: String? = null,
  public val url: String,
  @SerialName("git_url")
  public val gitUrl: String?,
  @SerialName("html_url")
  public val htmlUrl: String?,
  @SerialName("download_url")
  public val downloadUrl: String?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val entries: List<Entries>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val encoding: String? = null,
  @SerialName("_links")
  public val links: Links,
) {
  @Serializable
  public data class Entries(
    public val type: String,
    public val size: Long,
    public val name: String,
    public val path: String,
    public val sha: String,
    public val url: String,
    @SerialName("git_url")
    public val gitUrl: String?,
    @SerialName("html_url")
    public val htmlUrl: String?,
    @SerialName("download_url")
    public val downloadUrl: String?,
    @SerialName("_links")
    public val links: Links,
  ) {
    @Serializable
    public data class Links(
      public val git: String?,
      public val html: String?,
      public val self: String,
    )
  }

  @Serializable
  public data class Links(
    public val git: String?,
    public val html: String?,
    public val self: String,
  )
}
