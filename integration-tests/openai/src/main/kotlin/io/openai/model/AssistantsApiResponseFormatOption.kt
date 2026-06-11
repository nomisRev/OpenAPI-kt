package io.openai.model

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
 * Specifies the format that the model must output. Compatible with [GPT-4o](/docs/models#gpt-4o), [GPT-4 Turbo](/docs/models#gpt-4-turbo-and-gpt-4), and all GPT-3.5 Turbo models since `gpt-3.5-turbo-1106`.
 *
 * Setting to `{ "type": "json_schema", "json_schema": {...} }` enables Structured Outputs which ensures the model will match your supplied JSON schema. Learn more in the [Structured Outputs guide](/docs/guides/structured-outputs).
 *
 * Setting to `{ "type": "json_object" }` enables JSON mode, which ensures the message the model generates is valid JSON.
 *
 * **Important:** when using JSON mode, you **must** also instruct the model to produce JSON yourself via a system or user message. Without this, the model may generate an unending stream of whitespace until the generation reaches the token limit, resulting in a long-running and seemingly "stuck" request. Also note that the message content may be partially cut off if `finish_reason="length"`, which indicates the generation exceeded `max_tokens` or the conversation exceeded the max context length.
 *
 */
@Serializable(with = AssistantsApiResponseFormatOption.Serializer::class)
public sealed interface AssistantsApiResponseFormatOption {
  @Serializable
  public enum class Auto(
    public val `value`: String,
  ) : AssistantsApiResponseFormatOption {
    @SerialName("auto")
    Auto("auto"),
    ;
  }

  @Serializable
  @JvmInline
  public value class CaseResponseFormatText(
    public val `value`: ResponseFormatText,
  ) : AssistantsApiResponseFormatOption

  @Serializable
  @JvmInline
  public value class CaseResponseFormatJsonObject(
    public val `value`: ResponseFormatJsonObject,
  ) : AssistantsApiResponseFormatOption

  @Serializable
  @JvmInline
  public value class CaseResponseFormatJsonSchema(
    public val `value`: ResponseFormatJsonSchema,
  ) : AssistantsApiResponseFormatOption

  public object Serializer : KSerializer<AssistantsApiResponseFormatOption> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.AssistantsApiResponseFormatOption", PolymorphicKind.SEALED) {
      element("Auto", Auto.serializer().descriptor)
      element("CaseResponseFormatText", ResponseFormatText.serializer().descriptor)
      element("CaseResponseFormatJsonObject", ResponseFormatJsonObject.serializer().descriptor)
      element("CaseResponseFormatJsonSchema", ResponseFormatJsonSchema.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): AssistantsApiResponseFormatOption {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        Auto::class to { decodeFromJsonElement(Auto.serializer(), it) },
        CaseResponseFormatText::class to { CaseResponseFormatText(decodeFromJsonElement(ResponseFormatText.serializer(), it)) },
        CaseResponseFormatJsonObject::class to { CaseResponseFormatJsonObject(decodeFromJsonElement(ResponseFormatJsonObject.serializer(), it)) },
        CaseResponseFormatJsonSchema::class to { CaseResponseFormatJsonSchema(decodeFromJsonElement(ResponseFormatJsonSchema.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: AssistantsApiResponseFormatOption) {
      when(value) {
        is Auto -> encoder.encodeSerializableValue(Auto.serializer(), value)
        is CaseResponseFormatText -> encoder.encodeSerializableValue(ResponseFormatText.serializer(), value.value)
        is CaseResponseFormatJsonObject -> encoder.encodeSerializableValue(ResponseFormatJsonObject.serializer(), value.value)
        is CaseResponseFormatJsonSchema -> encoder.encodeSerializableValue(ResponseFormatJsonSchema.serializer(), value.value)
      }
    }
  }
}
