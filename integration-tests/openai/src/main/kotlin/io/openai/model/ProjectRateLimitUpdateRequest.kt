package io.openai.model

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectRateLimitUpdateRequest(
  @SerialName("max_requests_per_1_minute")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxRequestsPer1Minute: Long? = null,
  @SerialName("max_tokens_per_1_minute")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxTokensPer1Minute: Long? = null,
  @SerialName("max_images_per_1_minute")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxImagesPer1Minute: Long? = null,
  @SerialName("max_audio_megabytes_per_1_minute")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxAudioMegabytesPer1Minute: Long? = null,
  @SerialName("max_requests_per_1_day")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxRequestsPer1Day: Long? = null,
  @SerialName("batch_1_day_max_input_tokens")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val batch1DayMaxInputTokens: Long? = null,
)
