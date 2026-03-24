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
 * A built-in voice name or a custom voice reference.
 *
 */
@Serializable(with = VoiceIdsOrCustomVoice.Serializer::class)
public sealed interface VoiceIdsOrCustomVoice {
  @Serializable
  @JvmInline
  public value class CaseString(
    public val `value`: String,
  ) : VoiceIdsOrCustomVoice

  /**
   * Custom voice reference.
   */
  @JvmInline
  @Serializable
  public value class Id(
    public val id: String,
  ) : VoiceIdsOrCustomVoice

  public object Serializer : KSerializer<VoiceIdsOrCustomVoice> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.VoiceIdsOrCustomVoice", PolymorphicKind.SEALED) {
      element("CaseString", String.serializer().descriptor)
      element("Id", Id.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): VoiceIdsOrCustomVoice {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        Id::class to { decodeFromJsonElement(Id.serializer(), it) },
        CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: VoiceIdsOrCustomVoice) {
      when(value) {
        is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        is Id -> encoder.encodeSerializableValue(Id.serializer(), value)
      }
    }
  }
}
