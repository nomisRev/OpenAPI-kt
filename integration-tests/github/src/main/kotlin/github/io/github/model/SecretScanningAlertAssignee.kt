package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The username of the user to assign to the alert. Set to `null` to unassign the alert.
 */
@JvmInline
@Serializable
public value class SecretScanningAlertAssignee(
  public val `value`: String,
)
