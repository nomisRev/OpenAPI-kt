package io.github.nomisrev.render.test.client.annotated.reference

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Secret(
  public val token: String,
)
