package io.github.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * A repository rule with ruleset details.
 */
@JvmInline
@Serializable
public value class RepositoryRuleDetailed(
  public val `value`: JsonElement,
)
