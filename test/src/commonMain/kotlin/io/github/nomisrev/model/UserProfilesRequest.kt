package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfilesRequest(
    val general: GeneralUserProfileRequest? = null,
    val notifications: NotificationsUserProfileRequest? = null,
    val timetracking: TimeTrackingUserProfileRequest? = null,
)
