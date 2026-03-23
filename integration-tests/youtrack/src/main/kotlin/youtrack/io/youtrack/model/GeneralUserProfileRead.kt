package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GeneralUserProfileRead(
  public val id: String? = null,
  public val dateFieldFormat: DateFormatDescriptorRead? = null,
  public val timezone: TimeZoneDescriptorRead? = null,
  public val locale: LocaleDescriptorRead? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
