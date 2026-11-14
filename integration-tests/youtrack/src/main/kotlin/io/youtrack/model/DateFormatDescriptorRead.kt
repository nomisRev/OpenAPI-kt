package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents date format.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DateFormatDescriptorRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val presentation: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pattern: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val datePattern: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
