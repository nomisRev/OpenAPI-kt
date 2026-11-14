package io.github.nomisrev.render.test.client.media.type.without.schema

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Problem(
  public val title: String,
)
