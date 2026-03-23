package io.youtrack.model

import kotlin.Boolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LocaleSettingsWrite(
  public val locale: LocaleDescriptorWrite? = null,
  @SerialName("isRTL")
  public val isRtl: Boolean? = null,
)
