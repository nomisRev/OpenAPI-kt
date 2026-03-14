package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAlertInstanceState {
    @SerialName("open") Open, @SerialName("fixed") Fixed;
}
