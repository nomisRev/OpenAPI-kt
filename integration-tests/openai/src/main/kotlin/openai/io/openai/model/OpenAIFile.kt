package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `File` object represents a document that has been uploaded to OpenAI.
 */
@Serializable
public data class OpenAIFile(
  public val id: String,
  public val bytes: Long,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("expires_at")
  public val expiresAt: Long? = null,
  public val filename: String,
  public val `object`: Object,
  public val purpose: Purpose,
  public val status: Status,
  @SerialName("status_details")
  public val statusDetails: String? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("file")
    File("file"),
    ;
  }

  @Serializable
  public enum class Purpose(
    public val `value`: String,
  ) {
    @SerialName("assistants")
    Assistants("assistants"),
    @SerialName("assistants_output")
    AssistantsOutput("assistants_output"),
    @SerialName("batch")
    Batch("batch"),
    @SerialName("batch_output")
    BatchOutput("batch_output"),
    @SerialName("fine-tune")
    FineTune("fine-tune"),
    @SerialName("fine-tune-results")
    FineTuneResults("fine-tune-results"),
    @SerialName("vision")
    Vision("vision"),
    @SerialName("user_data")
    UserData("user_data"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("uploaded")
    Uploaded("uploaded"),
    @SerialName("processed")
    Processed("processed"),
    @SerialName("error")
    Error("error"),
    ;
  }
}
