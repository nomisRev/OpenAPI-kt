package io.openai.model

import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * When the number of tokens in a conversation exceeds the model's input token limit, the conversation be truncated, meaning messages (starting from the oldest) will not be included in the model's context. A 32k context model with 4,096 max output tokens can only include 28,224 tokens in the context before truncation occurs.
 *
 * Clients can configure truncation behavior to truncate with a lower max token limit, which is an effective way to control token usage and cost.
 *
 * Truncation will reduce the number of cached tokens on the next turn (busting the cache), since messages are dropped from the beginning of the context. However, clients can also configure truncation to retain messages up to a fraction of the maximum context size, which will reduce the need for future truncations and thus improve the cache rate.
 *
 * Truncation can be disabled entirely, which means the server will never truncate but would instead return an error if the conversation exceeds the model's input token limit.
 *
 */
@Serializable(with = RealtimeTruncation.Serializer::class)
public sealed interface RealtimeTruncation {
  @Serializable
  public enum class AutoOrDisabled(
    public val `value`: String,
  ) : RealtimeTruncation {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("disabled")
    Disabled("disabled"),
    ;
  }

  /**
   * Retain a fraction of the conversation tokens when the conversation exceeds the input token limit. This allows you to amortize truncations across multiple turns, which can help improve cached token usage.
   */
  @Serializable
  public data class RetentionRatio(
    public val type: Type,
    @SerialName("retention_ratio")
    public val retentionRatio: Double,
    @SerialName("token_limits")
    public val tokenLimits: TokenLimits? = null,
  ) : RealtimeTruncation {
    /**
     * Optional custom token limits for this truncation strategy. If not provided, the model's default token limits will be used.
     */
    @JvmInline
    @Serializable
    public value class TokenLimits(
      @SerialName("post_instructions")
      public val postInstructions: Long? = null,
    )

    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("retention_ratio")
      RetentionRatio("retention_ratio"),
      ;
    }
  }

  public object Serializer : KSerializer<RealtimeTruncation> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.RealtimeTruncation", PolymorphicKind.SEALED) {
      element("AutoOrDisabled", AutoOrDisabled.serializer().descriptor)
      element("RetentionRatio", RetentionRatio.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): RealtimeTruncation {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        RetentionRatio::class to { decodeFromJsonElement(RetentionRatio.serializer(), it) },
        AutoOrDisabled::class to { decodeFromJsonElement(AutoOrDisabled.serializer(), it) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: RealtimeTruncation) {
      when(value) {
        is AutoOrDisabled -> encoder.encodeSerializableValue(AutoOrDisabled.serializer(), value)
        is RetentionRatio -> encoder.encodeSerializableValue(RetentionRatio.serializer(), value)
      }
    }
  }
}
