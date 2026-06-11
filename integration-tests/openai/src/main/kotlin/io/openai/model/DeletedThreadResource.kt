package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Confirmation payload returned after deleting a thread.
 */
@Serializable
public data class DeletedThreadResource(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitThreadDeleted,
  public val deleted: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("chatkit.thread.deleted")
    ChatkitThreadDeleted("chatkit.thread.deleted"),
    ;
  }
}
