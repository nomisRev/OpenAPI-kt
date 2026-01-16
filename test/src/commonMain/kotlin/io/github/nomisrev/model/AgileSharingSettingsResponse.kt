package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AgileSharingSettingsResponse(
    val id: String? = null,
    val permittedGroups: List<UserGroupResponse>? = null,
    val permittedUsers: List<UserResponse>? = null,
    @SerialName($$"$type") val type: String? = null,
)
