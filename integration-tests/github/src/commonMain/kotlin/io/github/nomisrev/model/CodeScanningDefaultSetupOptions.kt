package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningDefaultSetupOptions(
    @SerialName("runner_type") val runnerType: RunnerType? = null,
    @SerialName("runner_label") val runnerLabel: String? = null,
) {
    @Serializable
    enum class RunnerType {
        @SerialName("standard") Standard, @SerialName("labeled") Labeled, @SerialName("not_set") NotSet;
    }
}
