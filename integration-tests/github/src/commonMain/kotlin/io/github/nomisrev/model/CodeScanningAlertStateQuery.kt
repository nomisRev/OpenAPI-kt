package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAlertStateQuery {
    @SerialName("open")
    Open,
    @SerialName("closed")
    Closed,
    @SerialName("dismissed")
    Dismissed,
    @SerialName("fixed")
    Fixed;
}
