package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable
public data class Response(
  public val metadata: Metadata? = null,
  @SerialName("top_logprobs")
  public val topLogprobs: Long? = null,
  public val temperature: Double? = null,
  @SerialName("top_p")
  public val topP: Double? = null,
  public val user: String? = null,
  @SerialName("safety_identifier")
  public val safetyIdentifier: String? = null,
  @SerialName("prompt_cache_key")
  public val promptCacheKey: String? = null,
  @SerialName("service_tier")
  public val serviceTier: ServiceTier? = null,
  @SerialName("prompt_cache_retention")
  public val promptCacheRetention: PromptCacheRetention? = null,
  @SerialName("previous_response_id")
  public val previousResponseId: String? = null,
  public val model: ModelIdsResponses? = null,
  public val reasoning: Reasoning? = null,
  public val background: Boolean? = null,
  @SerialName("max_output_tokens")
  public val maxOutputTokens: Long? = null,
  @SerialName("max_tool_calls")
  public val maxToolCalls: Long? = null,
  public val text: ResponseTextParam? = null,
  public val tools: ToolsArray? = null,
  @SerialName("tool_choice")
  public val toolChoice: ToolChoiceParam? = null,
  public val prompt: Prompt? = null,
  public val truncation: Truncation? = null,
  public val id: String? = null,
  public val `object`: Object? = null,
  public val status: Status? = null,
  @SerialName("created_at")
  public val createdAt: Double? = null,
  @SerialName("completed_at")
  public val completedAt: Double? = null,
  public val error: ResponseError? = null,
  @SerialName("incomplete_details")
  public val incompleteDetails: IncompleteDetails? = null,
  public val output: List<OutputItem>? = null,
  public val instructions: Instructions? = null,
  @SerialName("output_text")
  public val outputText: String? = null,
  public val usage: ResponseUsage? = null,
  @SerialName("parallel_tool_calls")
  public val parallelToolCalls: Boolean? = null,
  public val conversation: Conversation2? = null,
) {
  /**
   * Details about why the response is incomplete.
   *
   */
  @JvmInline
  @Serializable
  public value class IncompleteDetails(
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
        return json.attemptDeserialize(
          value,
          CaseInputItemList::class to { CaseInputItemList(decodeFromJsonElement(ListSerializer(InputItem.serializer()), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
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
