package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a group of settings of a user profile in YouTrack.
 */
@Serializable
public data class UserProfiles(
  public val id: String? = null,
  public val general: GeneralUserProfileRead? = null,
  public val notifications: NotificationsUserProfileRead? = null,
  public val timetracking: TimeTrackingUserProfileRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
