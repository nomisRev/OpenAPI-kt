package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * The user who performed the audit logged action.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AuditLogActorUser(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: String? = null,
)
