package io.github.nomisrev.render.test.object_.neutral.leaf.no.split

import kotlinx.serialization.Serializable

@Serializable
public data class RequestConfig(
  public val mode: Mode,
  public val enabled: Enabled,
)
