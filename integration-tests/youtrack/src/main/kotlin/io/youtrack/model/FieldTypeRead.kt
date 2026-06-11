package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class FieldTypeRead(
  public val id: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
