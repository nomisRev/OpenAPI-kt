package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.EncodeDefault
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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TokenCountsBody(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val input: Optional<Input> = Optional.Absent,
  @SerialName("previous_response_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val previousResponseId: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tools: Optional<List<Tool>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val text: Optional<ResponseTextParam> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reasoning: Optional<Reasoning> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val truncation: TruncationEnum? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val instructions: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val conversation: Optional<ConversationParam> = Optional.Absent,
  @SerialName("tool_choice")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val toolChoice: Optional<ToolChoiceParam> = Optional.Absent,
  @SerialName("parallel_tool_calls")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val parallelToolCalls: Optional<Boolean> = Optional.Absent,
) {
  /**
   * Text, image, or file inputs to the model, used to generate a response
   */
  @Serializable(with = Input.Serializer::class)
  public sealed interface Input {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : Input

    @Serializable
    @JvmInline
    public value class CaseInputItemList(
      public val `value`: List<InputItem>,
    ) : Input

    public object Serializer : KSerializer<Input> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.TokenCountsBody.Input", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseInputItemList", ListSerializer(InputItem.serializer()).descriptor)
      }

      override fun deserialize(decoder: Decoder): Input {
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

      override fun serialize(encoder: Encoder, `value`: Input) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseInputItemList -> encoder.encodeSerializableValue(ListSerializer(InputItem.serializer()), value.value)
        }
      }
    }
  }
}
