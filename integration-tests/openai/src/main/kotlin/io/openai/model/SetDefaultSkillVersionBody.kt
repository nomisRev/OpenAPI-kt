package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Updates the default version pointer for a skill.
 */
@Serializable
public data class SetDefaultSkillVersionBody(
  @SerialName("default_version")
  public val defaultVersion: String,
)
