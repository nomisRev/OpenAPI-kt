package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * License Simple
 */
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
  public val htmlUrl: String? = null,
)
