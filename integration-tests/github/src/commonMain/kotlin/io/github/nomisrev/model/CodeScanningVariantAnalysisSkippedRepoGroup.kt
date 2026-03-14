package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningVariantAnalysisSkippedRepoGroup(
    @SerialName("repository_count") val repositoryCount: Long,
    val repositories: List<CodeScanningVariantAnalysisRepository>,
)
