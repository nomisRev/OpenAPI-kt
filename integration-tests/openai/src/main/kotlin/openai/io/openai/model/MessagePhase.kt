package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class MessagePhase(
  public val `value`: String,
) {
  @SerialName("commentary")
  Commentary("commentary"),
  @SerialName("final_answer")
  FinalAnswer("final_answer"),
  ;
}
