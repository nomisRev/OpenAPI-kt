package io.github.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Limit interactions to a specific type of user for a specified duration
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class InteractionLimit(
  public val limit: InteractionGroup,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val expiry: InteractionExpiry? = null,
)
