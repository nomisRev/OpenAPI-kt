package io.github.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The ID of the workflow run.
 */
@JvmInline
@Serializable
public value class WorkflowRunId(
  public val `value`: Long,
)
