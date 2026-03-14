package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ReviewCustomGatesStateRequired(
    @SerialName("environment_name") val environmentName: String,
    val state: State,
    val comment: String? = null,
) {
    @Serializable
    enum class State {
        @SerialName("approved") Approved, @SerialName("rejected") Rejected;
    }
}
