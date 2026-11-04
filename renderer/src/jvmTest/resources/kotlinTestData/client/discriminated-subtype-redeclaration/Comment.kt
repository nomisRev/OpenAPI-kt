package io.github.nomisrev.render.test.client.discriminated.subtype.redeclaration

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class Comment(
  public val text: String,
)
