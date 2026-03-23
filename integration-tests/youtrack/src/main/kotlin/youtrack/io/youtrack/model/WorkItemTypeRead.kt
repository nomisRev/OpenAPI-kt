package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WorkItemTypeRead(
  public val id: String? = null,
  public val name: String? = null,
  public val autoAttached: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
