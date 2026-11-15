package io.github.nomisrev.render.test.client.annotated.reference

import kotlin.Int
import kotlinx.serialization.Serializable

@Serializable
public data class Status(
  public val code: Int,
)
