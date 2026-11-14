package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AppearanceSettingsRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timeZone: TimeZoneDescriptorRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dateFieldFormat: DateFormatDescriptorRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val logo: Logo? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
