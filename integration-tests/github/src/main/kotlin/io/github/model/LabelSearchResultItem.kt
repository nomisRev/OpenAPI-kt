package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Label Search Result Item
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LabelSearchResultItem(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val name: String,
  public val color: String,
  public val default: Boolean,
  public val description: String?,
  public val score: Double,
  @SerialName("text_matches")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val textMatches: SearchResultTextMatches? = null,
)
