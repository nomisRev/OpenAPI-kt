package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class ConversationResource(
  public val id: String,
  @Required
  public val `object`: Object = Object.Conversation,
  public val metadata: JsonElement,
  @SerialName("created_at")
  public val createdAt: Long,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("conversation")
    Conversation("conversation"),
    ;
  }
}
