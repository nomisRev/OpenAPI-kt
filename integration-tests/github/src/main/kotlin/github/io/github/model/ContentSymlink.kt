package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An object describing a symlink
 */
@Serializable
public data class ContentSymlink(
  public val type: Type,
  public val target: String,
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

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("symlink")
    Symlink("symlink"),
    ;
  }
}
