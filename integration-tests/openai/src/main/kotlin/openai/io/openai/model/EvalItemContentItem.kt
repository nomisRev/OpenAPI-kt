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
 * A single content item: input text, output text, input image, or input audio.
 *
 */
@Serializable(with = EvalItemContentItem.Serializer::class)
public sealed interface EvalItemContentItem {
  @Serializable
  @JvmInline
  public value class CaseEvalItemContentText(
    public val `value`: EvalItemContentText,
  ) : EvalItemContentItem

  @Serializable
  @JvmInline
  public value class CaseInputTextContent(
    public val `value`: InputTextContent,
  ) : EvalItemContentItem

  @Serializable
  @JvmInline
  public value class CaseEvalItemContentOutputText(
    public val `value`: EvalItemContentOutputText,
  ) : EvalItemContentItem

  @Serializable
  @JvmInline
  public value class CaseEvalItemInputImage(
    public val `value`: EvalItemInputImage,
  ) : EvalItemContentItem

  @Serializable
  @JvmInline
  public value class CaseInputAudio(
    public val `value`: InputAudio,
  ) : EvalItemContentItem

  public object Serializer : KSerializer<EvalItemContentItem> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.EvalItemContentItem", PolymorphicKind.SEALED) {
      element("CaseEvalItemContentText", EvalItemContentText.serializer().descriptor)
      element("CaseInputTextContent", InputTextContent.serializer().descriptor)
      element("CaseEvalItemContentOutputText", EvalItemContentOutputText.serializer().descriptor)
      element("CaseEvalItemInputImage", EvalItemInputImage.serializer().descriptor)
      element("CaseInputAudio", InputAudio.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): EvalItemContentItem {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseEvalItemContentText::class to { CaseEvalItemContentText(decodeFromJsonElement(EvalItemContentText.serializer(), it)) },
        CaseInputTextContent::class to { CaseInputTextContent(decodeFromJsonElement(InputTextContent.serializer(), it)) },
        CaseEvalItemContentOutputText::class to { CaseEvalItemContentOutputText(decodeFromJsonElement(EvalItemContentOutputText.serializer(), it)) },
        CaseEvalItemInputImage::class to { CaseEvalItemInputImage(decodeFromJsonElement(EvalItemInputImage.serializer(), it)) },
        CaseInputAudio::class to { CaseInputAudio(decodeFromJsonElement(InputAudio.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: EvalItemContentItem) {
      when(value) {
        is CaseEvalItemContentText -> encoder.encodeSerializableValue(EvalItemContentText.serializer(), value.value)
        is CaseInputTextContent -> encoder.encodeSerializableValue(InputTextContent.serializer(), value.value)
        is CaseEvalItemContentOutputText -> encoder.encodeSerializableValue(EvalItemContentOutputText.serializer(), value.value)
        is CaseEvalItemInputImage -> encoder.encodeSerializableValue(EvalItemInputImage.serializer(), value.value)
        is CaseInputAudio -> encoder.encodeSerializableValue(InputAudio.serializer(), value.value)
      }
    }
  }
}
