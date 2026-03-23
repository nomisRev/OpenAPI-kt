package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Code Of Conduct
 */
@Serializable
public data class CodeOfConduct(
  public val key: String,
  public val name: String,
  public val url: String,
  public val body: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String?,
)
