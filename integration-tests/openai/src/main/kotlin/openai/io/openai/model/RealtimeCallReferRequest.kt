package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters required to transfer a SIP call to a new destination using the
 * Realtime API.
 */
@JvmInline
@Serializable
public value class RealtimeCallReferRequest(
  @SerialName("target_uri")
  public val targetUri: String,
)
