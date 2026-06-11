package io.youtrack.model

import kotlinx.serialization.Serializable

@Serializable
public data class GeneralUserProfileWrite(
  public val dateFieldFormat: DateFormatDescriptorWrite? = null,
  public val timezone: TimeZoneDescriptorWrite? = null,
  public val locale: LocaleDescriptorWrite? = null,
)
