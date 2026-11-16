package io.github.nomisrev.render.test.client.discriminated.base.context

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ItemWrite(
  public val name: String,
)
