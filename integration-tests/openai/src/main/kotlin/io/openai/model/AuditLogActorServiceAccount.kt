package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

/**
 * The service account that performed the audit logged action.
 */
@JvmInline
@Serializable
public value class AuditLogActorServiceAccount(
  public val id: String? = null,
)
