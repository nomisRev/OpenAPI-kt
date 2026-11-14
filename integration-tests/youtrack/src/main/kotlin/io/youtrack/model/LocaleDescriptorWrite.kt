package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Represents a language locale that is used in UI.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LocaleDescriptorWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
)
