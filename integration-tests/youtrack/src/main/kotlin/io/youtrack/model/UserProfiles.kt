package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a group of settings of a user profile in YouTrack.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class UserProfiles(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val general: GeneralUserProfileRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val notifications: NotificationsUserProfileRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timetracking: TimeTrackingUserProfileRead? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
