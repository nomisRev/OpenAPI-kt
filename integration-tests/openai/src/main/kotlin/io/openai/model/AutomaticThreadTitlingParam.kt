package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Controls whether ChatKit automatically generates thread titles.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AutomaticThreadTitlingParam(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
)
