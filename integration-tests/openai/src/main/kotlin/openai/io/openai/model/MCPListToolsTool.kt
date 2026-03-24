package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A tool available on an MCP server.
 *
 */
@Serializable
public data class MCPListToolsTool(
  public val name: String,
  public val description: String? = null,
  @SerialName("input_schema")
  public val inputSchema: JsonElement,
  public val annotations: JsonElement? = null,
)
