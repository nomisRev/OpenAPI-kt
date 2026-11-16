package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sent when Realtime API Receives a incoming SIP call.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WebhookRealtimeCallIncoming(
  @SerialName("created_at")
  public val createdAt: Long,
  public val id: String,
  public val `data`: Data,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `object`: Object? = null,
  public val type: Type,
) {
  /**
   * Event data payload.
   *
   */
  @Serializable
  public data class Data(
    @SerialName("call_id")
    public val callId: String,
    @SerialName("sip_headers")
    public val sipHeaders: List<SipHeaders>,
  ) {
    /**
     * A header from the SIP Invite.
     *
     */
    @Serializable
    public data class SipHeaders(
      public val name: String,
      public val `value`: String,
    )
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("event")
    Event("event"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("realtime.call.incoming")
    RealtimeCallIncoming("realtime.call.incoming"),
    ;
  }
}
