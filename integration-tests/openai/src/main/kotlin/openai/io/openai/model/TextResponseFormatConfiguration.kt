package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * An object specifying the format that the model must output.
 *
 * Configuring `{ "type": "json_schema" }` enables Structured Outputs, 
 * which ensures the model will match your supplied JSON schema. Learn more in the 
 * [Structured Outputs guide](/docs/guides/structured-outputs).
 *
 * The default format is `{ "type": "text" }` with no additional options.
 *
 * **Not recommended for gpt-4o and newer models:**
 *
 * Setting to `{ "type": "json_object" }` enables the older JSON mode, which
 * ensures the message the model generates is valid JSON. Using `json_schema`
 * is preferred for models that support it.
 *
 */
@Serializable(with = TextResponseFormatConfiguration.Serializer::class)
public sealed interface TextResponseFormatConfiguration {
  @Serializable
  @JvmInline
  public value class CaseResponseFormatText(
    public val `value`: ResponseFormatText,
  ) : TextResponseFormatConfiguration

  @Serializable
  @JvmInline
  public value class CaseTextResponseFormatJsonSchema(
    public val `value`: TextResponseFormatJsonSchema,
  ) : TextResponseFormatConfiguration

  @Serializable
  @JvmInline
  public value class CaseResponseFormatJsonObject(
    public val `value`: ResponseFormatJsonObject,
  ) : TextResponseFormatConfiguration

  public object Serializer : KSerializer<TextResponseFormatConfiguration> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.TextResponseFormatConfiguration", PolymorphicKind.SEALED) {
      element("CaseResponseFormatText", ResponseFormatText.serializer().descriptor)
      element("CaseTextResponseFormatJsonSchema", TextResponseFormatJsonSchema.serializer().descriptor)
      element("CaseResponseFormatJsonObject", ResponseFormatJsonObject.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): TextResponseFormatConfiguration {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseResponseFormatText::class to { CaseResponseFormatText(decodeFromJsonElement(ResponseFormatText.serializer(), it)) },
        CaseTextResponseFormatJsonSchema::class to { CaseTextResponseFormatJsonSchema(decodeFromJsonElement(TextResponseFormatJsonSchema.serializer(), it)) },
        CaseResponseFormatJsonObject::class to { CaseResponseFormatJsonObject(decodeFromJsonElement(ResponseFormatJsonObject.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: TextResponseFormatConfiguration) {
      when(value) {
        is CaseResponseFormatText -> encoder.encodeSerializableValue(ResponseFormatText.serializer(), value.value)
        is CaseTextResponseFormatJsonSchema -> encoder.encodeSerializableValue(TextResponseFormatJsonSchema.serializer(), value.value)
        is CaseResponseFormatJsonObject -> encoder.encodeSerializableValue(ResponseFormatJsonObject.serializer(), value.value)
      }
    }
  }
}
