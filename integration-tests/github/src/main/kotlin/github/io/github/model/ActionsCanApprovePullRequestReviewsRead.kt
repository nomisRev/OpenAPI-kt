package io.github.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Whether GitHub Actions can approve pull requests. Enabling this can be a security risk.
 */
@JvmInline
@Serializable
public value class ActionsCanApprovePullRequestReviewsRead(
  public val `value`: Boolean,
)
