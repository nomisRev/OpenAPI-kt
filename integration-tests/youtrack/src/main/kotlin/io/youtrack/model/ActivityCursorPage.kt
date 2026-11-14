package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActivityCursorPage(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val activities: List<ActivityItem>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val afterCursor: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val beforeCursor: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasAfter: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasBefore: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reverse: Boolean? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
