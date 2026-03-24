package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class RunGraderRequest(
  public val grader: JsonElement,
  public val item: JsonElement? = null,
  @SerialName("model_sample")
  public val modelSample: String,
)
