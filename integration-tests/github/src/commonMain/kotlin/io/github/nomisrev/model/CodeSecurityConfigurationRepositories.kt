package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeSecurityConfigurationRepositories(val status: Status? = null, val repository: SimpleRepository? = null) {
    @Serializable
    enum class Status {
        @SerialName("attached")
        Attached,
        @SerialName("attaching")
        Attaching,
        @SerialName("detached")
        Detached,
        @SerialName("removed")
        Removed,
        @SerialName("enforced")
        Enforced,
        @SerialName("failed")
        Failed,
        @SerialName("updating")
        Updating,
        @SerialName("removed_by_enterprise")
        RemovedByEnterprise;
    }
}
