package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ActivityCursorPage(
  public val id: String? = null,
  public val activities: List<ActivityItem>? = null,
  public val afterCursor: String? = null,
  public val beforeCursor: String? = null,
  public val hasAfter: Boolean? = null,
  public val hasBefore: Boolean? = null,
  public val reverse: Boolean? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
