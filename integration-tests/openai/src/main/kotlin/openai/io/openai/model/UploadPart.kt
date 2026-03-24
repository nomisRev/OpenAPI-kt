package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The upload Part represents a chunk of bytes we can add to an Upload object.
 *
 */
@Serializable
public data class UploadPart(
  public val id: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("upload_id")
  public val uploadId: String,
  public val `object`: Object,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("upload.part")
    UploadPart("upload.part"),
    ;
  }
}
