package io.github.model

import kotlin.Double
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * The EPSS scores as calculated by the [Exploit Prediction Scoring System](https://www.first.org/epss).
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SecurityAdvisoryEpss(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val percentage: Double? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val percentile: Double? = null,
)
