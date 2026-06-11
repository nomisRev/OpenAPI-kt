package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Content File
 */
@Serializable
public data class ContentFile(
  public val type: Type,
  public val encoding: String,
  public val size: Long,
  public val name: String,
  public val path: String,
  public val content: String,
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
  public val target: String? = null,
  @SerialName("submodule_git_url")
  public val submoduleGitUrl: String? = null,
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
    @SerialName("file")
    File("file"),
    ;
  }
}
