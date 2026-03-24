package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VadConfig(
  public val type: Type,
  @SerialName("prefix_padding_ms")
  public val prefixPaddingMs: Long? = null,
  @SerialName("silence_duration_ms")
  public val silenceDurationMs: Long? = null,
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
