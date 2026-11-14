package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters for provisioning a new ChatKit session.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateChatSessionBody(
  public val workflow: WorkflowParam,
  public val user: String,
  @SerialName("expires_after")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val expiresAfter: ExpiresAfterParam? = null,
  @SerialName("rate_limits")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rateLimits: RateLimitsParam? = null,
  @SerialName("chatkit_configuration")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val chatkitConfiguration: ChatkitConfigurationParam? = null,
)
