package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ChatCompletionMessageToolCallChunk(
  public val index: Long,
  public val id: String? = null,
  public val type: Type? = null,
  public val function: Function? = null,
) {
  @Serializable
  public data class Function(
    public val name: String? = null,
    public val arguments: String? = null,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("function")
    Function("function"),
    ;
  }
}
