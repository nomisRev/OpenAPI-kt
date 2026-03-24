package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class LocalSkillParam(
  public val name: String,
  public val description: String,
  public val path: String,
)
