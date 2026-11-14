package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ChatCompletionMessageToolCallChunk(
  public val index: Long,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: Type? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val function: Function? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Function(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
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
