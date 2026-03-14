package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName

@Serializable
data class TeamMembership(val url: String, @Required val role: Role, val state: State) {
    @Serializable
    enum class Role {
        @SerialName("member") Member, @SerialName("maintainer") Maintainer;
    }

    @Serializable
    enum class State {
        @SerialName("active") Active, @SerialName("pending") Pending;
    }
}
