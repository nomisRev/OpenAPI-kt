package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeleteMessageResponse(
  public val id: String,
  public val deleted: Boolean,
  public val `object`: Object,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("thread.message.deleted")
    ThreadMessageDeleted("thread.message.deleted"),
    ;
  }
}
