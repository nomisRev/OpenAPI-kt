package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ActionsHostedRunner(
    val id: Long,
    val name: String,
    @SerialName("runner_group_id") val runnerGroupId: Long? = null,
    @SerialName("image_details") val imageDetails: NullableActionsHostedRunnerPoolImage?,
    @SerialName("machine_size_details") val machineSizeDetails: ActionsHostedRunnerMachineSpec,
    val status: Status,
    val platform: String,
    @SerialName("maximum_runners") val maximumRunners: Long? = null,
    @SerialName("public_ip_enabled") val publicIpEnabled: Boolean,
    @SerialName("public_ips") val publicIps: List<PublicIp>? = null,
    @SerialName("last_active_on") val lastActiveOn: LocalDateTime? = null,
    @SerialName("image_gen") val imageGen: Boolean? = null,
) {
    @Serializable
    enum class Status {
        Ready, Provisioning, Shutdown, Deleting, Stuck;
    }
}
