package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class DiscussionEvent(
  public val action: String,
  public val discussion: Discussion,
)
