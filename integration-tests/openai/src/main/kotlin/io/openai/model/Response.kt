package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Response(
  public val metadata: Metadata?,
  @SerialName("top_logprobs")
  public val topLogprobs: Long? = null,
  @Required
  public val temperature: Double? = null,
  @SerialName("top_p")
  @Required
  public val topP: Double? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: String? = null,
  @SerialName("safety_identifier")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val safetyIdentifier: String? = null,
  @SerialName("prompt_cache_key")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val promptCacheKey: String? = null,
  @SerialName("service_tier")
  public val serviceTier: ServiceTier? = null,
  @SerialName("prompt_cache_retention")
  public val promptCacheRetention: PromptCacheRetention? = null,
  @SerialName("previous_response_id")
  public val previousResponseId: String? = null,
  public val model: ModelIdsResponses,
  public val reasoning: Reasoning? = null,
  public val background: Boolean? = null,
  @SerialName("max_output_tokens")
  public val maxOutputTokens: Long? = null,
  @SerialName("max_tool_calls")
  public val maxToolCalls: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val text: ResponseTextParam? = null,
  public val tools: ToolsArray,
  @SerialName("tool_choice")
  public val toolChoice: ToolChoiceParam,
  public val prompt: Prompt? = null,
  public val truncation: Truncation? = null,
  public val id: String,
  public val `object`: Object,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: Status? = null,
  @SerialName("created_at")
  public val createdAt: Double,
  @SerialName("completed_at")
  public val completedAt: Double? = null,
  public val error: ResponseError?,
  @SerialName("incomplete_details")
  public val incompleteDetails: IncompleteDetails?,
  public val output: List<OutputItem>,
  public val instructions: Instructions?,
  @SerialName("output_text")
  public val outputText: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val usage: ResponseUsage? = null,
  @SerialName("parallel_tool_calls")
  @Required
  public val parallelToolCalls: Boolean = true,
  public val conversation: Conversation2? = null,
) {
  /**
   * Details about why the response is incomplete.
   *
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IncompleteDetails(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reason: Reason? = null,
  ) {
    @Serializable
    public enum class Reason(
      public val `value`: String,
    ) {
      @SerialName("max_output_tokens")
      MaxOutputTokens("max_output_tokens"),
      @SerialName("content_filter")
      ContentFilter("content_filter"),
      ;
    }
  }

  /**
   * A system (or developer) message inserted into the model's context.
   *
   * When using along with `previous_response_id`, the instructions from a previous
   * response will not be carried over to the next response. This makes it simple
   * to swap out system (or developer) messages in new responses.
   *
   */
  @Serializable(with = Instructions.Serializer::class)
  public sealed interface Instructions {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Instructions

    @Serializable
    @JvmInline
    public value class CaseInputItemList(
      public val `value`: List<InputItem>,
    ) : Instructions

    public object Serializer : KSerializer<Instructions> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.Response.Instructions", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseInputItemList", ListSerializer(InputItem.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Instructions {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonArray -> json.attemptDeserialize(
            value,
            CaseInputItemList::class to { CaseInputItemList(decodeFromJsonElement(ListSerializer(InputItem.serializer()), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: Instructions) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseInputItemList -> encoder.encodeSerializableValue(ListSerializer(InputItem.serializer()), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("response")
    Response("response"),
    ;
  }

  @Serializable
  public enum class PromptCacheRetention(
    public val `value`: String,
  ) {
    @SerialName("in-memory")
    InMemory("in-memory"),
    `24h`("24h"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("completed")
    Completed("completed"),
    @SerialName("failed")
    Failed("failed"),
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    @SerialName("queued")
    Queued("queued"),
    @SerialName("incomplete")
    Incomplete("incomplete"),
    ;
  }

  @Serializable
  public enum class Truncation(
    public val `value`: String,
  ) {
    @SerialName("auto")
    Auto("auto"),
    @SerialName("disabled")
    Disabled("disabled"),
    ;
  }
}
