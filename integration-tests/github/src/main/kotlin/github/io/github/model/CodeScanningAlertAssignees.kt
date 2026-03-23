package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The list of users to assign to the code scanning alert. An empty array unassigns all previous assignees from the alert.
 */
@JvmInline
@Serializable
public value class CodeScanningAlertAssignees(
  public val items: List<String>,
)
