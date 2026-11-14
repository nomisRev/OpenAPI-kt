package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The breakdown of Copilot Business seats for the organization.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CopilotOrganizationSeatBreakdown(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val total: Long? = null,
  @SerialName("added_this_cycle")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val addedThisCycle: Long? = null,
  @SerialName("pending_cancellation")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pendingCancellation: Long? = null,
  @SerialName("pending_invitation")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pendingInvitation: Long? = null,
  @SerialName("active_this_cycle")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val activeThisCycle: Long? = null,
  @SerialName("inactive_this_cycle")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inactiveThisCycle: Long? = null,
)
