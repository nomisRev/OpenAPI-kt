package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Use this option to force the model to call a specific tool on a remote MCP server.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ToolChoiceMCP(
  public val type: Type,
  @SerialName("server_label")
  public val serverLabel: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: Optional<String> = Optional.Absent,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("mcp")
    Mcp("mcp"),
    ;
  }
}
