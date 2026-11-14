package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A tool available on an MCP server.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class MCPListToolsTool(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: Optional<String> = Optional.Absent,
  @SerialName("input_schema")
  public val inputSchema: JsonElement,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val annotations: Optional<JsonElement> = Optional.Absent,
)
