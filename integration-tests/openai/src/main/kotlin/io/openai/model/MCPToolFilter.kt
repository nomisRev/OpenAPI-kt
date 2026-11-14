package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A filter object to specify which tools are allowed.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class MCPToolFilter(
  @SerialName("tool_names")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolNames: List<String>? = null,
  @SerialName("read_only")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val readOnly: Boolean? = null,
)
