package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * An optional comment when closing or reopening an alert. Cannot be updated or deleted.
 */
@JvmInline
@Serializable
public value class SecretScanningAlertResolutionComment(
  public val `value`: String,
)
