package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Github-hosted hosted runner.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ActionsHostedRunner(
  public val id: Long,
  public val name: String,
  @SerialName("runner_group_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runnerGroupId: Long? = null,
  @SerialName("image_details")
  public val imageDetails: NullableActionsHostedRunnerPoolImage?,
  @SerialName("machine_size_details")
  public val machineSizeDetails: ActionsHostedRunnerMachineSpec,
  public val status: Status,
  public val platform: String,
  @SerialName("maximum_runners")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maximumRunners: Long? = null,
  @SerialName("public_ip_enabled")
  public val publicIpEnabled: Boolean,
  @SerialName("public_ips")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val publicIps: List<PublicIp>? = null,
  @SerialName("last_active_on")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lastActiveOn: Optional<Instant> = Optional.Absent,
  @SerialName("image_gen")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
