package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters used to decline an incoming SIP call handled by the Realtime API.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RealtimeCallRejectRequest(
  @SerialName("status_code")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val statusCode: Long? = null,
)
