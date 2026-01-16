package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserProfilesResponse(
    val id: String? = null,
    val general: GeneralUserProfileResponse? = null,
    val notifications: NotificationsUserProfileResponse? = null,
    val timetracking: TimeTrackingUserProfileResponse? = null,
    @SerialName($$"$type") val type: String? = null,
)
