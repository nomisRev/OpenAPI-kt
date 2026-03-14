package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ActionsHostedRunnerLimits(@SerialName("public_ips") val publicIps: PublicIps) {
    @Serializable
    data class PublicIps(val maximum: Long, @SerialName("current_usage") val currentUsage: Long)
}
