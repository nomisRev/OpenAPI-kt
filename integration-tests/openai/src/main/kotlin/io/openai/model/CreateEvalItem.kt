package io.openai.model

import kotlin.OptIn
import kotlin.String
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
 * A chat message that makes up the prompt or context. May include variable references to the `item` namespace, ie {{item.name}}.
 */
@Serializable(with = CreateEvalItem.Serializer::class)
public sealed interface CreateEvalItem {
  @Serializable
  public data class CaseElse(
    public val role: String,
    public val content: String,
  ) : CreateEvalItem

  @Serializable
  @JvmInline
  public value class CaseEvalItem(
    public val `value`: EvalItem,
  ) : CreateEvalItem

  public object Serializer : KSerializer<CreateEvalItem> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.CreateEvalItem", PolymorphicKind.SEALED) {
      element("CaseElse", CaseElse.serializer().descriptor)
      element("CaseEvalItem", EvalItem.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): CreateEvalItem {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseElse::class to { decodeFromJsonElement(CaseElse.serializer(), it) },
        CaseEvalItem::class to { CaseEvalItem(decodeFromJsonElement(EvalItem.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: CreateEvalItem) {
      when(value) {
        is CaseElse -> encoder.encodeSerializableValue(CaseElse.serializer(), value)
        is CaseEvalItem -> encoder.encodeSerializableValue(EvalItem.serializer(), value.value)
      }
    }
  }
}
