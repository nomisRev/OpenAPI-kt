package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningVariantAnalysisRepoTask(
  public val repository: SimpleRepository,
  @SerialName("analysis_status")
  public val analysisStatus: CodeScanningVariantAnalysisStatus,
  @SerialName("artifact_size_in_bytes")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val artifactSizeInBytes: Long? = null,
  @SerialName("result_count")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val resultCount: Long? = null,
  @SerialName("failure_message")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val failureMessage: String? = null,
  @SerialName("database_commit_sha")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val databaseCommitSha: String? = null,
  @SerialName("source_location_prefix")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sourceLocationPrefix: String? = null,
  @SerialName("artifact_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val artifactUrl: String? = null,
)
