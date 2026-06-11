package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LocaleSettingsRead(
  public val id: String? = null,
  public val locale: LocaleDescriptorRead? = null,
  @SerialName("isRTL")
  public val isRtl: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
