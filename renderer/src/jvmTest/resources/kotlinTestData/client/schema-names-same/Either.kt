package io.github.nomisrev.render.test.client.schema.names.same

import kotlin.Long
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

@Serializable(with = Either.Serializer::class)
public sealed interface Either {
  @Serializable
  public data class CaseElse(
    public val x: Long,
  ) : Either

  @Serializable
  @JvmInline
  public value class CaseMetadata2(
    public val `value`: Metadata2,
  ) : Either

  public object Serializer : KSerializer<Either> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.client.schema.names.same.Either", PolymorphicKind.SEALED) {
      element("CaseElse", CaseElse.serializer().descriptor)
      element("CaseMetadata2", Metadata2.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Either {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseElse::class to { decodeFromJsonElement(CaseElse.serializer(), it) },
        CaseMetadata2::class to { CaseMetadata2(decodeFromJsonElement(Metadata2.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Either) {
      when(value) {
        is CaseElse -> encoder.encodeSerializableValue(CaseElse.serializer(), value)
        is CaseMetadata2 -> encoder.encodeSerializableValue(Metadata2.serializer(), value.value)
      }
    }
  }
}
