package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Status Check Policy
 */
@Serializable
public data class StatusCheckPolicy(
  public val url: String,
  public val strict: Boolean,
  public val contexts: List<String>,
  public val checks: List<Checks>,
  @SerialName("contexts_url")
  public val contextsUrl: String,
) {
  @Serializable
  public data class Checks(
    public val context: String,
    @SerialName("app_id")
    public val appId: Long?,
  )
}
