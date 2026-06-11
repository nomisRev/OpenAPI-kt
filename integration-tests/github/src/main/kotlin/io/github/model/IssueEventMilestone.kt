package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Issue Event Milestone
 */
@JvmInline
@Serializable
public value class IssueEventMilestone(
  public val title: String,
)
