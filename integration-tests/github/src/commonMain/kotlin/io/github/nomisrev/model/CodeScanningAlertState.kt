package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAlertState {
    @SerialName("open") Open, @SerialName("dismissed") Dismissed, @SerialName("fixed") Fixed;
}
