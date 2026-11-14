package io.github.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Permission check result for a given devcontainer config.
 */
@Serializable
public data class CodespacesPermissionsCheckForDevcontainer(
  public val accepted: Boolean,
)
