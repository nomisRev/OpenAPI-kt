package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningAutofixResponse(
    val status: CodeScanningAutofixStatus,
    val description: CodeScanningAutofixDescription?,
    @SerialName("started_at") val startedAt: CodeScanningAutofixStartedAtResponse,
)
