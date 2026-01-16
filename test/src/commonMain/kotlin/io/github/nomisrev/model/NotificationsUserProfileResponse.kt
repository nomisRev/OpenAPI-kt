package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NotificationsUserProfileResponse(
    val id: String? = null,
    val notifyOnOwnChanges: Boolean? = null,
    val emailNotificationsEnabled: Boolean? = null,
    val mentionNotificationsEnabled: Boolean? = null,
    val duplicateClusterNotificationsEnabled: Boolean? = null,
    val mailboxIntegrationNotificationsEnabled: Boolean? = null,
    val usePlainTextEmails: Boolean? = null,
    val autoWatchOnComment: Boolean? = null,
    val autoWatchOnCreate: Boolean? = null,
    val autoWatchOnVote: Boolean? = null,
    val autoWatchOnUpdate: Boolean? = null,
    @SerialName($$"$type") val type: String? = null,
)
