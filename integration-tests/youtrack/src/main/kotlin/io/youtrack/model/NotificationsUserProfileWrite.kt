package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NotificationsUserProfileWrite(
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
)
