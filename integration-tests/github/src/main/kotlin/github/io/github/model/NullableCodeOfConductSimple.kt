package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Code of Conduct Simple
 */
@Serializable
public data class NullableCodeOfConductSimple(
  public val url: String,
  public val key: String,
  public val name: String,
  @SerialName("html_url")
  public val htmlUrl: String?,
)
