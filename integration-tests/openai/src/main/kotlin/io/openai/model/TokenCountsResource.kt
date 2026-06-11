package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TokenCountsResource(
  @Required
  public val `object`: Object = Object.ResponseInputTokens,
  @SerialName("input_tokens")
  public val inputTokens: Long,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("response.input_tokens")
    ResponseInputTokens("response.input_tokens"),
    ;
  }
}
