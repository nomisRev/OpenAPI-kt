package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents settings for a single board column
 */
@Serializable
public data class AgileColumn(
  public val id: String? = null,
  public val presentation: String? = null,
  public val isResolved: Boolean? = null,
  public val ordinal: Int? = null,
  public val parent: ColumnSettings? = null,
  public val wipLimit: WIPLimit? = null,
  public val fieldValues: List<DatabaseAttributeValueRead.AgileColumnFieldValue>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
