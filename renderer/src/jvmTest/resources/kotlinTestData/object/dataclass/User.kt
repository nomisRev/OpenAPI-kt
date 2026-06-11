package io.github.nomisrev.render.test.object_.dataclass

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class User(
  public val id: Long,
  public val name: String,
  public val active: Boolean? = null,
)
