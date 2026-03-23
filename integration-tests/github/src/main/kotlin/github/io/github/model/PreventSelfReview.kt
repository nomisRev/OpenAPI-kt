package io.github.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Whether or not a user who created the job is prevented from approving their own job.
 */
@JvmInline
@Serializable
public value class PreventSelfReview(
  public val `value`: Boolean,
)
