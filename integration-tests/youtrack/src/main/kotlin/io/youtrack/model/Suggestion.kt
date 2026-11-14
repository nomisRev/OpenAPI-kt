package io.youtrack.model

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents query suggestion.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Suggestion(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val completionStart: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val completionEnd: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val matchingStart: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val matchingEnd: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val caret: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val option: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prefix: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val suffix: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val group: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val icon: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val auxiliaryIcon: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val className: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
