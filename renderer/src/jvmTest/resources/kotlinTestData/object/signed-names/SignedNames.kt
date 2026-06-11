package io.github.nomisrev.render.test.object_.signed.names

import kotlin.Long
import kotlinx.serialization.Serializable

@Serializable
public data class SignedNames(
  public val `+1`: Long,
  public val `-1`: Long,
  public val `+count`: Long,
  public val `-offset`: Long,
)
