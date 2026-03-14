package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class SecretScanningAlertResolution {
    @SerialName("false_positive")
    FalsePositive,
    @SerialName("wont_fix")
    WontFix,
    @SerialName("revoked")
    Revoked,
    @SerialName("used_in_tests")
    UsedInTests;
}
