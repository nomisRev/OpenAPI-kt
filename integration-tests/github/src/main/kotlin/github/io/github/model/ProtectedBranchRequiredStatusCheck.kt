package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Protected Branch Required Status Check
 */
@Serializable
public data class ProtectedBranchRequiredStatusCheck(
  public val url: String? = null,
  @SerialName("enforcement_level")
  public val enforcementLevel: String? = null,
  public val contexts: List<String>,
  public val checks: List<Checks>,
  @SerialName("contexts_url")
  public val contextsUrl: String? = null,
  public val strict: Boolean? = null,
) {
  @Serializable
  public data class Checks(
    public val context: String,
    @SerialName("app_id")
    public val appId: Long?,
  )
}
