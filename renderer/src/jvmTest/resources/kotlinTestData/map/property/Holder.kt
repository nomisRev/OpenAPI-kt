package io.github.nomisrev.render.test.map.`property`

import kotlin.Int
import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.Serializable

@Serializable
public data class Holder(
  public val `inline`: Map<String, Int>,
  public val labels: Labels,
)
