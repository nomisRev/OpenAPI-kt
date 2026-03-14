package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CopilotOrganizationSeatBreakdown(
    val total: Long? = null,
    @SerialName("added_this_cycle") val addedThisCycle: Long? = null,
    @SerialName("pending_cancellation") val pendingCancellation: Long? = null,
    @SerialName("pending_invitation") val pendingInvitation: Long? = null,
    @SerialName("active_this_cycle") val activeThisCycle: Long? = null,
    @SerialName("inactive_this_cycle") val inactiveThisCycle: Long? = null,
)
