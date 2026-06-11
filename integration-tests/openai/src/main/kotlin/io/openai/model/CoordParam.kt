package io.openai.model

import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * An x/y coordinate pair, e.g. `{ x: 100, y: 200 }`.
 */
@Serializable
public data class CoordParam(
  public val x: Long,
  public val y: Long,
)
