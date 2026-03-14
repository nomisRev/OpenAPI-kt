package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAlertClassification {
    @SerialName("source")
    Source,
    @SerialName("generated")
    Generated,
    @SerialName("test")
    Test,
    @SerialName("library")
    Library;
}
