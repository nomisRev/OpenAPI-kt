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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * A built-in voice name or a custom voice reference.
 *
 */
@Serializable(with = VoiceIdsOrCustomVoice.Serializer::class)
public sealed interface VoiceIdsOrCustomVoice {
  @Serializable
  @JvmInline
  public value class CaseVoiceIdsShared(
    public val `value`: VoiceIdsShared,
  ) : VoiceIdsOrCustomVoice

  /**
   * Custom voice reference.
   */
  @Serializable
  public data class Id(
    public val id: String,
  ) : VoiceIdsOrCustomVoice

  public object Serializer : KSerializer<VoiceIdsOrCustomVoice> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.openai.model.VoiceIdsOrCustomVoice", PolymorphicKind.SEALED) {
      element("CaseVoiceIdsShared", VoiceIdsShared.serializer().descriptor)
      element("Id", Id.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): VoiceIdsOrCustomVoice {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return when {
        value is JsonPrimitive && value.isString -> json.attemptDeserialize(
          value,
          CaseVoiceIdsShared::class to { CaseVoiceIdsShared(decodeFromJsonElement(VoiceIdsShared.serializer(), it)) },
        )
        value is JsonObject -> json.attemptDeserialize(
          value,
          Id::class to { decodeFromJsonElement(Id.serializer(), it) },
        )
        else -> throw UnionSerializationException(value, emptyMap())
      }
    }

    override fun serialize(encoder: Encoder, `value`: VoiceIdsOrCustomVoice) {
      when(value) {
        is CaseVoiceIdsShared -> encoder.encodeSerializableValue(VoiceIdsShared.serializer(), value.value)
        is Id -> encoder.encodeSerializableValue(Id.serializer(), value)
      }
    }
  }
}
