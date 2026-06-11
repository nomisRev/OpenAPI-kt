package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateUploadRequest(
  public val filename: String,
  public val purpose: Purpose,
  public val bytes: Long,
  @SerialName("mime_type")
  public val mimeType: String,
  @SerialName("expires_after")
  public val expiresAfter: FileExpirationAfter? = null,
) {
  @Serializable
  public enum class Purpose(
    public val `value`: String,
  ) {
    @SerialName("assistants")
    Assistants("assistants"),
    @SerialName("batch")
    Batch("batch"),
    @SerialName("fine-tune")
    FineTune("fine-tune"),
    @SerialName("vision")
    Vision("vision"),
    ;
  }
}
