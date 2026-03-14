package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningAutofixCommitsResponse(
    @SerialName("target_ref") val targetRef: String? = null,
    val sha: String? = null,
)
