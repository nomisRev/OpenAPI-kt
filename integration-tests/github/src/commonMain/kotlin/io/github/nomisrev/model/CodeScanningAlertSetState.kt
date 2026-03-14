package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAlertSetState {
    @SerialName("open") Open, @SerialName("dismissed") Dismissed;
}
