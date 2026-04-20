package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The ID of the push protection bypass placeholder. This value is returned on any push protected routes.
 */
@JvmInline
@Serializable
public value class SecretScanningPushProtectionBypassPlaceholderIdRead(
  public val `value`: String,
)
