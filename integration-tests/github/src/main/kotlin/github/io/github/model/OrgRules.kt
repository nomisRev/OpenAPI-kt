package io.github.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A repository rule.
 */
@JvmInline
@Serializable
public value class OrgRules(
  public val `value`: JsonElement,
)
