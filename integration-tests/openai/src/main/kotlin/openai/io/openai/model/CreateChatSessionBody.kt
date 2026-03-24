package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters for provisioning a new ChatKit session.
 */
@Serializable
public data class CreateChatSessionBody(
  public val workflow: WorkflowParam,
  public val user: String,
  @SerialName("expires_after")
  public val expiresAfter: ExpiresAfterParam? = null,
  @SerialName("rate_limits")
  public val rateLimits: RateLimitsParam? = null,
  @SerialName("chatkit_configuration")
  public val chatkitConfiguration: ChatkitConfigurationParam? = null,
)
