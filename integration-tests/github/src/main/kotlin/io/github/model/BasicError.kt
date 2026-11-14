package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Basic Error
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BasicError(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: String? = null,
  @SerialName("documentation_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val documentationUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: String? = null,
)
