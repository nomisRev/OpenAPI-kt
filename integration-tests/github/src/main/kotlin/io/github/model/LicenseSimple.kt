package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * License Simple
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LicenseSimple(
  public val key: String,
  public val name: String,
  public val url: String?,
  @SerialName("spdx_id")
  public val spdxId: String?,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: String? = null,
)
