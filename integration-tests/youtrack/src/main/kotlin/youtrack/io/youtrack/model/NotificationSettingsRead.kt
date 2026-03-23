package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class NotificationSettingsRead(
  public val id: String? = null,
  public val emailSettings: EmailSettings? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
