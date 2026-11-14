package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shows if the board has any configuration problems.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AgileStatus(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val valid: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasJobs: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val errors: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val warnings: List<String>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
