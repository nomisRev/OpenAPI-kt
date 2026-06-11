package io.youtrack.model

import kotlinx.serialization.Serializable

@Serializable
public data class AppearanceSettingsWrite(
  public val timeZone: TimeZoneDescriptorWrite? = null,
  public val dateFieldFormat: DateFormatDescriptorWrite? = null,
)
