package io.youtrack.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents query suggestion.
 */
@Serializable
public data class Suggestion(
  public val id: String? = null,
  public val completionStart: Int? = null,
  public val completionEnd: Int? = null,
  public val matchingStart: Int? = null,
  public val matchingEnd: Int? = null,
  public val caret: Int? = null,
  public val description: String? = null,
  public val option: String? = null,
  public val prefix: String? = null,
  public val suffix: String? = null,
  public val group: String? = null,
  public val icon: String? = null,
  public val auxiliaryIcon: String? = null,
  public val className: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
