package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class ToolSearchExecutionType(
  public val `value`: String,
) {
  @SerialName("server")
  Server("server"),
  @SerialName("client")
  Client("client"),
  ;
}
