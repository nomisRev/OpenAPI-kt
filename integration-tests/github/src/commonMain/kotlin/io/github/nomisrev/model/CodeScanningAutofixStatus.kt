package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class CodeScanningAutofixStatus {
    @SerialName("pending")
    Pending,
    @SerialName("error")
    Error,
    @SerialName("success")
    Success,
    @SerialName("outdated")
    Outdated;
}
