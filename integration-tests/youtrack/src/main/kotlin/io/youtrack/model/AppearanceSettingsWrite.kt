package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AppearanceSettingsWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeZone: TimeZoneDescriptorWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dateFieldFormat: DateFormatDescriptorWrite? = null,
)
