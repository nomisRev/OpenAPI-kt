package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Validation Error Simple
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ValidationErrorSimple(
  public val message: String,
  @SerialName("documentation_url")
  public val documentationUrl: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val errors: List<String>? = null,
)
