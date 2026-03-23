package io.github.model

import kotlin.Boolean
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * Permission check result for a given devcontainer config.
 */
@JvmInline
@Serializable
public value class CodespacesPermissionsCheckForDevcontainer(
  public val accepted: Boolean,
)
