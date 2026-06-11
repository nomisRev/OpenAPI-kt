package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * License Content
 */
@Serializable
public data class LicenseContent(
  public val name: String,
  public val path: String,
  public val sha: String,
  public val size: Long,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String?,
  @SerialName("git_url")
  public val gitUrl: String?,
  @SerialName("download_url")
  public val downloadUrl: String?,
  public val type: String,
  public val content: String,
  public val encoding: String,
  @SerialName("_links")
  public val links: Links,
  public val license: NullableLicenseSimple?,
) {
  @Serializable
  public data class Links(
    public val git: String?,
    public val html: String?,
    public val self: String,
  )
}
