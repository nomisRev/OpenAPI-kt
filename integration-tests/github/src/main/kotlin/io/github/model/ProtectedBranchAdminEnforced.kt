package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Protected Branch Admin Enforced
 */
@Serializable
public data class ProtectedBranchAdminEnforced(
  public val url: String,
  public val enabled: Boolean,
)
