package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepositoryAdvisoryCredit(val user: SimpleUser, val type: SecurityAdvisoryCreditTypes, val state: State) {
    @Serializable
    enum class State {
        @SerialName("accepted") Accepted, @SerialName("declined") Declined, @SerialName("pending") Pending;
    }
}
