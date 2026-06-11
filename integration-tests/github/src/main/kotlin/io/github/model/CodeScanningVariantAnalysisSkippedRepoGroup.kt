package io.github.model

import kotlin.Long
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningVariantAnalysisSkippedRepoGroup(
  @SerialName("repository_count")
  public val repositoryCount: Long,
  public val repositories: List<CodeScanningVariantAnalysisRepository>,
)
