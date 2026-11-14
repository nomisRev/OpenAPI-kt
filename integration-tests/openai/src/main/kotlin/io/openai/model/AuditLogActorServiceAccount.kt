package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * The service account that performed the audit logged action.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AuditLogActorServiceAccount(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
)
