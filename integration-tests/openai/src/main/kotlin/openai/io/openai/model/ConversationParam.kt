package io.openai.model

import kotlin.OptIn
import kotlin.String
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
 * The conversation that this response belongs to. Items from this conversation are prepended to `input_items` for this response request.
 * Input items and output items from this response are automatically added to this conversation after this response completes.
 *
 */
@Serializable(with = ConversationParam.Serializer::class)
public sealed interface ConversationParam {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : ConversationParam

  @Serializable
  @JvmInline
  public value class CaseConversationParam2(
    public val `value`: ConversationParam2,
  ) : ConversationParam

  public object Serializer : KSerializer<ConversationParam> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.ConversationParam", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("CaseConversationParam2", ConversationParam2.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): ConversationParam {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseConversationParam2::class to { CaseConversationParam2(decodeFromJsonElement(ConversationParam2.serializer(), it)) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: ConversationParam) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is CaseConversationParam2 -> encoder.encodeSerializableValue(ConversationParam2.serializer(), value.value)
      }
    }
  }
}
