package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The session in which the audit logged action was performed.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AuditLogActorSession(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: AuditLogActorUser? = null,
  @SerialName("ip_address")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ipAddress: String? = null,
)
