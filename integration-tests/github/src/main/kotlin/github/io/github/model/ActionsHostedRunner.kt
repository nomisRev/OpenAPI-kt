package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Github-hosted hosted runner.
 */
@Serializable
public data class ActionsHostedRunner(
  public val id: Long,
  public val name: String,
  @SerialName("runner_group_id")
  public val runnerGroupId: Long? = null,
  @SerialName("image_details")
  public val imageDetails: NullableActionsHostedRunnerPoolImage?,
  @SerialName("machine_size_details")
  public val machineSizeDetails: ActionsHostedRunnerMachineSpec,
  public val status: Status,
  public val platform: String,
  @SerialName("maximum_runners")
  public val maximumRunners: Long? = null,
  @SerialName("public_ip_enabled")
  public val publicIpEnabled: Boolean,
  @SerialName("public_ips")
  public val publicIps: List<PublicIp>? = null,
  @SerialName("last_active_on")
  public val lastActiveOn: Instant? = null,
  @SerialName("image_gen")
  public val imageGen: Boolean? = null,
) {
  @Serializable
  public enum class Status {
    Ready,
    Provisioning,
    Shutdown,
    Deleting,
    Stuck,
  }
}
