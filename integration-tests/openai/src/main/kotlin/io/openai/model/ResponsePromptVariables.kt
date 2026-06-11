package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Optional map of values to substitute in for variables in your
 * prompt. The substitution values can either be strings, or other
 * Response input types like images or files.
 *
 */
@JvmInline
@Serializable
public value class ResponsePromptVariables(
  public val values: List<AdditionalProperties>? = null,
) {
  @Serializable(with = AdditionalProperties.Serializer::class)
  public sealed interface AdditionalProperties {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : AdditionalProperties

    @Serializable
    @JvmInline
    public value class CaseInputTextContent(
      public val `value`: InputTextContent,
    ) : AdditionalProperties

    @Serializable
    @JvmInline
    public value class CaseInputImageContent(
      public val `value`: InputImageContent,
    ) : AdditionalProperties

    @Serializable
    @JvmInline
    public value class CaseInputFileContent(
      public val `value`: InputFileContent,
    ) : AdditionalProperties

    public object Serializer : KSerializer<AdditionalProperties> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.ResponsePromptVariables.AdditionalProperties", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseInputTextContent", InputTextContent.serializer().descriptor)
        element("CaseInputImageContent", InputImageContent.serializer().descriptor)
        element("CaseInputFileContent", InputFileContent.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): AdditionalProperties {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseInputTextContent::class to { CaseInputTextContent(decodeFromJsonElement(InputTextContent.serializer(), it)) },
          CaseInputImageContent::class to { CaseInputImageContent(decodeFromJsonElement(InputImageContent.serializer(), it)) },
          CaseInputFileContent::class to { CaseInputFileContent(decodeFromJsonElement(InputFileContent.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: AdditionalProperties) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseInputTextContent -> encoder.encodeSerializableValue(InputTextContent.serializer(), value.value)
          is CaseInputImageContent -> encoder.encodeSerializableValue(InputImageContent.serializer(), value.value)
          is CaseInputFileContent -> encoder.encodeSerializableValue(InputFileContent.serializer(), value.value)
        }
      }
    }
  }
}
