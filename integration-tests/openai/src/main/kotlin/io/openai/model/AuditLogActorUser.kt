package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The user who performed the audit logged action.
 */
@Serializable
public data class AuditLogActorUser(
  public val id: String? = null,
  public val email: String? = null,
)
