package io.youtrack.model

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents WIP limits for particular column. If they are not satisfied, the column will be highlighted in UI.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WIPLimit(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val max: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val min: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val column: AgileColumn? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
