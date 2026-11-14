package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NotificationsUserProfileRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val notifyOnOwnChanges: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val emailNotificationsEnabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mentionNotificationsEnabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val duplicateClusterNotificationsEnabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val mailboxIntegrationNotificationsEnabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usePlainTextEmails: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val autoWatchOnComment: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val autoWatchOnCreate: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val autoWatchOnVote: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val autoWatchOnUpdate: Boolean? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
