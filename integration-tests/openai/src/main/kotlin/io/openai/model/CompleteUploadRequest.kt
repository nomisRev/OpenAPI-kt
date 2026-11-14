package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CompleteUploadRequest(
  @SerialName("part_ids")
  public val partIds: List<String>,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val md5: String? = null,
)
