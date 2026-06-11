package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A filter object to specify which tools are allowed.
 *
 */
@Serializable
public data class MCPToolFilter(
  @SerialName("tool_names")
  public val toolNames: List<String>? = null,
  @SerialName("read_only")
  public val readOnly: Boolean? = null,
)
