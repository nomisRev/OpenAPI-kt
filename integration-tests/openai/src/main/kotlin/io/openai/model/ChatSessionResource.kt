package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a ChatKit session and its resolved configuration.
 */
@Serializable
public data class ChatSessionResource(
  public val id: String,
  @Required
  public val `object`: Object = Object.ChatkitSession,
  @SerialName("expires_at")
  public val expiresAt: Long,
  @SerialName("client_secret")
  public val clientSecret: String,
  public val workflow: ChatkitWorkflow,
  public val user: String,
  @SerialName("rate_limits")
  public val rateLimits: ChatSessionRateLimits,
  @SerialName("max_requests_per_1_minute")
  public val maxRequestsPer1Minute: Long,
  public val status: ChatSessionStatus,
  @SerialName("chatkit_configuration")
  public val chatkitConfiguration: ChatSessionChatkitConfiguration,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("chatkit.session")
    ChatkitSession("chatkit.session"),
    ;
  }
}
