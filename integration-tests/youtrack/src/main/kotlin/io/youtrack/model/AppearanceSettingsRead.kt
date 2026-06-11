package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AppearanceSettingsRead(
  public val id: String? = null,
  public val timeZone: TimeZoneDescriptorRead? = null,
  public val dateFieldFormat: DateFormatDescriptorRead? = null,
  public val logo: Logo? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
