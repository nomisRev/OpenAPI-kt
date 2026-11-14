package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectServiceAccountCreateRequest(
  public val name: String,
)
