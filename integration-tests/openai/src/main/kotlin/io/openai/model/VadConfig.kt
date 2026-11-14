package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class VadConfig(
  public val type: Type,
  @SerialName("prefix_padding_ms")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prefixPaddingMs: Long? = null,
  @SerialName("silence_duration_ms")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val silenceDurationMs: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val threshold: Double? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("server_vad")
    ServerVad("server_vad"),
    ;
  }
}
