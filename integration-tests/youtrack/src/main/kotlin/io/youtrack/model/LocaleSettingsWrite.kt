package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LocaleSettingsWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val locale: LocaleDescriptorWrite? = null,
  @SerialName("isRTL")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isRtl: Boolean? = null,
)
