package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Configuration for turn detection, ether Server VAD or Semantic VAD. This can be set to `null` to turn off, in which case the client must manually trigger model response.
 *
 * Server VAD means that the model will detect the start and end of speech based on audio volume and respond at the end of user speech.
 *
 * Semantic VAD is more advanced and uses a turn detection model (in conjunction with VAD) to semantically estimate whether the user has finished speaking, then dynamically sets a timeout based on this probability. For example, if user audio trails off with "uhhm", the model will score a low probability of turn end and wait longer for the user to continue speaking. This can be useful for more natural conversations, but may have a higher latency.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface RealtimeTurnDetection {
  /**
   * Server-side voice activity detection (VAD) which flips on when user speech is detected and off after a period of silence.
   */
  @SerialName("ServerVAD")
  @Serializable
  public data class ServerVAD(
    @Required
    public val type: String = "server_vad",
    public val threshold: Double? = null,
    @SerialName("prefix_padding_ms")
    public val prefixPaddingMs: Long? = null,
    @SerialName("silence_duration_ms")
    public val silenceDurationMs: Long? = null,
    @SerialName("create_response")
    public val createResponse: Boolean? = null,
    @SerialName("interrupt_response")
    public val interruptResponse: Boolean? = null,
    @SerialName("idle_timeout_ms")
    public val idleTimeoutMs: Long? = null,
  ) : RealtimeTurnDetection

  /**
   * Server-side semantic turn detection which uses a model to determine when the user has finished speaking.
   */
  @SerialName("SemanticVAD")
  @Serializable
  public data class SemanticVAD(
    public val type: String,
    public val eagerness: Eagerness? = null,
    @SerialName("create_response")
    public val createResponse: Boolean? = null,
    @SerialName("interrupt_response")
    public val interruptResponse: Boolean? = null,
  ) : RealtimeTurnDetection {
    @Serializable
    public enum class Eagerness(
      public val `value`: String,
    ) {
      @SerialName("low")
      Low("low"),
      @SerialName("medium")
      Medium("medium"),
      @SerialName("high")
      High("high"),
      @SerialName("auto")
      Auto("auto"),
      ;
    }
  }
}
