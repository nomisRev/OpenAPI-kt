package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Protected Branch Required Status Check
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProtectedBranchRequiredStatusCheck(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
  @SerialName("enforcement_level")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enforcementLevel: String? = null,
  public val contexts: List<String>,
  public val checks: List<Checks>,
  @SerialName("contexts_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val contextsUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val strict: Boolean? = null,
) {
  @Serializable
  public data class Checks(
    public val context: String,
    @SerialName("app_id")
    public val appId: Long?,
  )
}
