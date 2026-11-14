package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GlobalSettingsRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val systemSettings: SystemSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val notificationSettings: NotificationSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val restSettings: RestCorsSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val appearanceSettings: AppearanceSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val localeSettings: LocaleSettingsRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val license: LicenseRead? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
