package io.github.model

import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Conditions for an organization ruleset.
 * The branch and tag rulesets conditions object should contain both `repository_name` and `ref_name` properties, or both `repository_id` and `ref_name` properties, or both `repository_property` and `ref_name` properties.
 * The push rulesets conditions object does not require the `ref_name` property.
 * For repository policy rulesets, the conditions object should only contain the `repository_name`, the `repository_id`, or the `repository_property`.
 */
@JvmInline
@Serializable
public value class OrgRulesetConditions(
  public val `value`: JsonElement,
)
