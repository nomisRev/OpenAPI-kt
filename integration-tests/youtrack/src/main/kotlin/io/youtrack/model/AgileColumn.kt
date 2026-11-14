package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents settings for a single board column
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AgileColumn(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val presentation: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isResolved: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ordinal: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val parent: ColumnSettings? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val wipLimit: WIPLimit? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fieldValues: List<DatabaseAttributeValueRead.AgileColumnFieldValue>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
