package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * List all Copilot Content Exclusion rules for an organization.
 */
@JvmInline
@Serializable
public value class CopilotOrganizationContentExclusionDetails(
  public val values: Map<String, List<String>>,
)
