package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningAlertInstance(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ref: CodeScanningRef? = null,
  @SerialName("analysis_key")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val analysisKey: CodeScanningAnalysisAnalysisKey? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val environment: CodeScanningAlertEnvironment? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val category: CodeScanningAnalysisCategory? = null,
  public val state: CodeScanningAlertState? = null,
  @SerialName("commit_sha")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val commitSha: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: Message? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val location: CodeScanningAlertLocation? = null,
  @SerialName("html_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val htmlUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val classifications: List<CodeScanningAlertClassification?>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Message(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val text: String? = null,
  )
}
