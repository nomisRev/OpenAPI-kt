package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Code Of Conduct
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeOfConduct(
  public val key: String,
  public val name: String,
  public val url: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val body: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String?,
)
