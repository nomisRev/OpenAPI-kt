package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAlertSeverity {
    @SerialName("critical")
    Critical,
    @SerialName("high")
    High,
    @SerialName("medium")
    Medium,
    @SerialName("low")
    Low,
    @SerialName("warning")
    Warning,
    @SerialName("note")
    Note,
    @SerialName("error")
    Error;
}
