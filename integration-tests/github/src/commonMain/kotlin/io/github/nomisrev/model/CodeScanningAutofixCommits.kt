package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningAutofixCommits(
    @SerialName("target_ref") val targetRef: String? = null,
    val message: String? = null,
)
