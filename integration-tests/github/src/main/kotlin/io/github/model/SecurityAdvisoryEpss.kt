package io.github.model

import kotlin.Double
import kotlinx.serialization.Serializable

/**
 * The EPSS scores as calculated by the [Exploit Prediction Scoring System](https://www.first.org/epss).
 */
@Serializable
public data class SecurityAdvisoryEpss(
  public val percentage: Double? = null,
  public val percentile: Double? = null,
)
