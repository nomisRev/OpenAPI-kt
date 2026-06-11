package io.openai.model

import kotlin.ByteArray
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateFileRequest(
  public val `file`: ByteArray,
  public val purpose: Purpose,
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
    @SerialName("user_data")
    UserData("user_data"),
    @SerialName("evals")
    Evals("evals"),
    ;
  }
}
