package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class SecretScanningAlertState {
    @SerialName("open") Open, @SerialName("resolved") Resolved;
}
