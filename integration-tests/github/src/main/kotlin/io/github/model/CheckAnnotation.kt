package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Check Annotation
 */
@Serializable
public data class CheckAnnotation(
  public val path: String,
  @SerialName("start_line")
  public val startLine: Long,
  @SerialName("end_line")
  public val endLine: Long,
  @SerialName("start_column")
  public val startColumn: Long?,
  @SerialName("end_column")
  public val endColumn: Long?,
  @SerialName("annotation_level")
  public val annotationLevel: String?,
  public val title: String?,
  public val message: String?,
  @SerialName("raw_details")
  public val rawDetails: String?,
  @SerialName("blob_href")
  public val blobHref: String,
)
