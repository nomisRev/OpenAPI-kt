package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Color-coded labels help you categorize and filter your issues (just like labels in Gmail).
 */
@Serializable
public data class Label(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  public val name: String,
  public val description: String?,
  public val color: String,
  public val default: Boolean,
)
