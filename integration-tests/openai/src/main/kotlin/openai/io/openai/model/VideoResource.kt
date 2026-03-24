package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Structured information describing a generated video job.
 */
@Serializable
public data class VideoResource(
  public val id: String,
  @Required
  public val `object`: Object = Object.Video,
  public val model: VideoModel,
  public val status: VideoStatus,
  public val progress: Long,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("completed_at")
  public val completedAt: Long?,
  @SerialName("expires_at")
  public val expiresAt: Long?,
  public val prompt: String?,
  public val size: VideoSize,
  public val seconds: String,
  @SerialName("remixed_from_video_id")
  public val remixedFromVideoId: String?,
  public val error: Error2?,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("video")
    Video("video"),
    ;
  }
}
