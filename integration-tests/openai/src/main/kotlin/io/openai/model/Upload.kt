package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The Upload object can accept byte chunks in the form of Parts.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Upload(
  public val id: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val filename: String,
  public val bytes: Long,
  public val purpose: String,
  public val status: Status,
  @SerialName("expires_at")
  public val expiresAt: Long,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `object`: Object? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `file`: OpenAIFile? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("upload")
    Upload("upload"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("pending")
    Pending("pending"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    @SerialName("expired")
    Expired("expired"),
    ;
  }
}
