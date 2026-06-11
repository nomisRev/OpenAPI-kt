package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Confirmation payload returned after deleting a video.
 */
@Serializable
public data class DeletedVideoResource(
  @Required
  public val `object`: Object = Object.VideoDeleted,
  public val deleted: Boolean,
  public val id: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("video.deleted")
    VideoDeleted("video.deleted"),
    ;
  }
}
