package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ContextManagementParam(
  public val type: String,
  @SerialName("compact_threshold")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val compactThreshold: Optional<Long> = Optional.Absent,
)
