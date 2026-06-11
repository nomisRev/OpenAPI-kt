package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The session in which the audit logged action was performed.
 */
@Serializable
public data class AuditLogActorSession(
  public val user: AuditLogActorUser? = null,
  @SerialName("ip_address")
  public val ipAddress: String? = null,
)
