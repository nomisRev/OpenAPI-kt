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
 * Inputs to the model - can contain template strings. Supports text, output text, input images, and input audio, either as a single item or an array of items.
 *
 */
@Serializable(with = EvalItemContent.Serializer::class)
public sealed interface EvalItemContent {
  @Serializable
  @JvmInline
  public value class CaseEvalItemContentItem(
    public val `value`: EvalItemContentItem,
  ) : EvalItemContent

  @Serializable
  @JvmInline
  public value class CaseEvalItemContentArray(
    public val `value`: EvalItemContentArray,
  ) : EvalItemContent

  public object Serializer : KSerializer<EvalItemContent> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.EvalItemContent", PolymorphicKind.SEALED) {
      element("CaseEvalItemContentItem", EvalItemContentItem.serializer().descriptor)
      element("CaseEvalItemContentArray", EvalItemContentArray.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): EvalItemContent {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseEvalItemContentItem::class to { CaseEvalItemContentItem(decodeFromJsonElement(EvalItemContentItem.serializer(), it)) },
        CaseEvalItemContentArray::class to { CaseEvalItemContentArray(decodeFromJsonElement(EvalItemContentArray.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: EvalItemContent) {
      when(value) {
        is CaseEvalItemContentItem -> encoder.encodeSerializableValue(EvalItemContentItem.serializer(), value.value)
        is CaseEvalItemContentArray -> encoder.encodeSerializableValue(EvalItemContentArray.serializer(), value.value)
      }
    }
  }
}
