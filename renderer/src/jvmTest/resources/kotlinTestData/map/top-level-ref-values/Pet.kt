package io.github.nomisrev.render.test.map.top.level.ref.values

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Pet(
  public val name: String,
  public val age: Int? = null,
)
