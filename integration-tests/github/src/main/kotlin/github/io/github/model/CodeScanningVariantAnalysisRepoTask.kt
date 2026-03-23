package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningVariantAnalysisRepoTask(
  public val repository: SimpleRepository,
  @SerialName("analysis_status")
  public val analysisStatus: CodeScanningVariantAnalysisStatus,
  @SerialName("artifact_size_in_bytes")
  public val artifactSizeInBytes: Long? = null,
  @SerialName("result_count")
  public val resultCount: Long? = null,
  @SerialName("failure_message")
  public val failureMessage: String? = null,
  @SerialName("database_commit_sha")
  public val databaseCommitSha: String? = null,
  @SerialName("source_location_prefix")
  public val sourceLocationPrefix: String? = null,
  @SerialName("artifact_url")
  public val artifactUrl: String? = null,
)
