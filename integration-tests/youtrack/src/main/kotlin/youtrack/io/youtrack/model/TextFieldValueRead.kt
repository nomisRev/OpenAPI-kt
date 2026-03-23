package io.youtrack.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a value of the text field. Returns both source and rendered text.
 */
@Serializable
public data class TextFieldValueRead(
  public val id: String? = null,
  public val text: String? = null,
  public val markdownText: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
