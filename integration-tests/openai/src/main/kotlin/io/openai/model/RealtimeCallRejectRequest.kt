package io.openai.model

import kotlin.Long
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters used to decline an incoming SIP call handled by the Realtime API.
 */
@JvmInline
@Serializable
public value class RealtimeCallRejectRequest(
  @SerialName("status_code")
  public val statusCode: Long? = null,
)
