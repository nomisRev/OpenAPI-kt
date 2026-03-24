package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class TranscriptionInclude(
  public val `value`: String,
) {
  @SerialName("logprobs")
  Logprobs("logprobs"),
  ;
}
