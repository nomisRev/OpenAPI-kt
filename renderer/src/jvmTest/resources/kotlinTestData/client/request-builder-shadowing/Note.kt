package io.github.nomisrev.render.test.client.request.builder.shadowing

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Note(
  public val text: String,
)
