package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class CreateEvalRunRequest(
  public val name: String? = null,
  public val metadata: Metadata? = null,
  @SerialName("data_source")
  public val dataSource: JsonElement,
)
