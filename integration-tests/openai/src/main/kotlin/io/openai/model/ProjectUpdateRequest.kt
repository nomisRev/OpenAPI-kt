package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectUpdateRequest(
  public val name: String,
)
