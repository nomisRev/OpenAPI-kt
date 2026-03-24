package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CompleteUploadRequest(
  @SerialName("part_ids")
  public val partIds: List<String>,
  public val md5: String? = null,
)
