package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateEmbeddingResponse(
  public val `data`: List<Embedding>,
  public val model: String,
  public val `object`: Object,
  public val usage: Usage,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("list")
    List("list"),
    ;
  }

  /**
   * The usage information for the request.
   */
  @Serializable
  public data class Usage(
    @SerialName("prompt_tokens")
    public val promptTokens: Long,
    @SerialName("total_tokens")
    public val totalTokens: Long,
  )
}
