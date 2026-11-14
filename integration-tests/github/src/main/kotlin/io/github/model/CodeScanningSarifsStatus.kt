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
public data class CodeScanningSarifsStatus(
  @SerialName("processing_status")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val processingStatus: ProcessingStatus? = null,
  @SerialName("analyses_url")
  public val analysesUrl: String? = null,
  public val errors: List<String>? = null,
) {
  @Serializable
  public enum class ProcessingStatus(
    public val `value`: String,
  ) {
    @SerialName("pending")
    Pending("pending"),
    @SerialName("complete")
    Complete("complete"),
    @SerialName("failed")
    Failed("failed"),
    ;
  }
}
