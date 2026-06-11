package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data related to a release.
 */
@Serializable
public data class ReleaseAsset(
  public val url: String,
  @SerialName("browser_download_url")
  public val browserDownloadUrl: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  public val label: String?,
  public val state: State,
  @SerialName("content_type")
  public val contentType: String,
  public val size: Long,
  public val digest: String?,
  @SerialName("download_count")
  public val downloadCount: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val uploader: NullableSimpleUser?,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("uploaded")
    Uploaded("uploaded"),
    @SerialName("open")
    Open("open"),
    ;
  }
}
