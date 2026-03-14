package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAlertDismissedReason {
    @SerialName("false positive")
    FalsePositive,
    @SerialName("won't fix")
    `Won'tFix`,
    @SerialName("used in tests")
    UsedInTests;
}
