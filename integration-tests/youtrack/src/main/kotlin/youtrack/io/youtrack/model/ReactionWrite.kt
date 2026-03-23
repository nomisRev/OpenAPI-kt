package io.youtrack.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ReactionWrite(
  public val author: UserWrite? = null,
  public val reaction: String? = null,
)
