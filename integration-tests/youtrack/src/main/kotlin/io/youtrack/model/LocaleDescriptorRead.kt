package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a language locale that is used in UI.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LocaleDescriptorRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val locale: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val language: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val community: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
