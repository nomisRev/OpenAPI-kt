package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningVariantAnalysisStatus {
    @SerialName("pending")
    Pending,
    @SerialName("in_progress")
    InProgress,
    @SerialName("succeeded")
    Succeeded,
    @SerialName("failed")
    Failed,
    @SerialName("canceled")
    Canceled,
    @SerialName("timed_out")
    TimedOut;
}
