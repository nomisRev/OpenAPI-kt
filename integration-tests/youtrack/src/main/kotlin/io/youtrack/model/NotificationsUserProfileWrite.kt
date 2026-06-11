package io.youtrack.model

import kotlin.Boolean
import kotlinx.serialization.Serializable

@Serializable
public data class NotificationsUserProfileWrite(
  public val notifyOnOwnChanges: Boolean? = null,
  public val emailNotificationsEnabled: Boolean? = null,
  public val mentionNotificationsEnabled: Boolean? = null,
  public val duplicateClusterNotificationsEnabled: Boolean? = null,
  public val mailboxIntegrationNotificationsEnabled: Boolean? = null,
  public val usePlainTextEmails: Boolean? = null,
  public val autoWatchOnComment: Boolean? = null,
  public val autoWatchOnCreate: Boolean? = null,
  public val autoWatchOnVote: Boolean? = null,
  public val autoWatchOnUpdate: Boolean? = null,
)
