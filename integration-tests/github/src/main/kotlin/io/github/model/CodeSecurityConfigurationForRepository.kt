package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Code security configuration associated with a repository and attachment status
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeSecurityConfigurationForRepository(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: Status? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val configuration: CodeSecurityConfiguration? = null,
) {
  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("attached")
    Attached("attached"),
    @SerialName("attaching")
    Attaching("attaching"),
    @SerialName("detached")
    Detached("detached"),
    @SerialName("removed")
    Removed("removed"),
    @SerialName("enforced")
    Enforced("enforced"),
    @SerialName("failed")
    Failed("failed"),
    @SerialName("updating")
    Updating("updating"),
    @SerialName("removed_by_enterprise")
    RemovedByEnterprise("removed_by_enterprise"),
    ;
  }
}
