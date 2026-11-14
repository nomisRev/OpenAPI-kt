package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GeneralUserProfileWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dateFieldFormat: DateFormatDescriptorWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timezone: TimeZoneDescriptorWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val locale: LocaleDescriptorWrite? = null,
)
