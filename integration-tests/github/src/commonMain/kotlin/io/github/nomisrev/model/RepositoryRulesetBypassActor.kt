package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepositoryRulesetBypassActor(
    @SerialName("actor_id") val actorId: Long? = null,
    @SerialName("actor_type") val actorType: ActorType,
    @SerialName("bypass_mode") val bypassMode: BypassMode? = null,
) {
    @Serializable
    enum class ActorType {
        Integration, OrganizationAdmin, RepositoryRole, Team, DeployKey;
    }

    @Serializable
    enum class BypassMode {
        @SerialName("always") Always, @SerialName("pull_request") PullRequest, @SerialName("exempt") Exempt;
    }
}
