package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The version of the entity. This is used to confirm you're updating the current version of the entity and mitigate unintentionally overriding someone else's update.
 */
@JvmInline
@Serializable
public value class SecretScanningRowVersion(
  public val `value`: String,
)
