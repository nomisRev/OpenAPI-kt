package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Updates the default version pointer for a skill.
 */
@JvmInline
@Serializable
public value class SetDefaultSkillVersionBody(
  @SerialName("default_version")
  public val defaultVersion: String,
)
