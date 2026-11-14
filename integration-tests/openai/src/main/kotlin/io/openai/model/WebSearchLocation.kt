package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Approximate location parameters for the search.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WebSearchLocation(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val country: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val region: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val city: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timezone: String? = null,
)
