package io.github.model

import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The breakdown of Copilot Business seats for the organization.
 */
@Serializable
public data class CopilotOrganizationSeatBreakdown(
  public val total: Long? = null,
  @SerialName("added_this_cycle")
  public val addedThisCycle: Long? = null,
  @SerialName("pending_cancellation")
  public val pendingCancellation: Long? = null,
  @SerialName("pending_invitation")
  public val pendingInvitation: Long? = null,
  @SerialName("active_this_cycle")
  public val activeThisCycle: Long? = null,
  @SerialName("inactive_this_cycle")
  public val inactiveThisCycle: Long? = null,
)
