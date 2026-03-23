package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GlobalSettingsRead(
  public val id: String? = null,
  public val systemSettings: SystemSettingsRead? = null,
  public val notificationSettings: NotificationSettingsRead? = null,
  public val restSettings: RestCorsSettingsRead? = null,
  public val appearanceSettings: AppearanceSettingsRead? = null,
  public val localeSettings: LocaleSettingsRead? = null,
  public val license: LicenseRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
