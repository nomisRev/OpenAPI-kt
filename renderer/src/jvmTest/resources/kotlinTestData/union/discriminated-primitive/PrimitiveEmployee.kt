package io.github.nomisrev.render.test.union.discriminated.primitive

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class PrimitiveEmployee(
  public val kind: String,
  public val age: Int,
)
