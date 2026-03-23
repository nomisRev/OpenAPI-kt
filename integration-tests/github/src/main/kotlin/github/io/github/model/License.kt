package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * License
 */
@Serializable
public data class License(
  public val key: String,
  public val name: String,
  @SerialName("spdx_id")
  public val spdxId: String?,
  public val url: String?,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val description: String,
  public val implementation: String,
  public val permissions: List<String>,
  public val conditions: List<String>,
  public val limitations: List<String>,
  public val body: String,
  public val featured: Boolean,
)
