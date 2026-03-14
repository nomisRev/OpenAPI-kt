package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class SecretScanningPushProtectionBypassReason {
    @SerialName("false_positive")
    FalsePositive,
    @SerialName("used_in_tests")
    UsedInTests,
    @SerialName("will_fix_later")
    WillFixLater;
}
