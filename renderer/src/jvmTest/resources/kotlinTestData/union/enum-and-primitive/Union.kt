package io.github.nomisrev.render.test.union.`enum`.and.primitive

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Union.Serializer::class)
public sealed interface Union {
  public val `value`: String

  @Serializable
  @JvmInline
  public value class CaseString(
    override val `value`: String,
  ) : Union

  @Serializable
  public enum class AscOrDesc(
    override val `value`: String,
  ) : Union {
    @SerialName("asc")
    Asc("asc"),
    @SerialName("desc")
    Desc("desc"),
    ;
  }

  public object Serializer : KSerializer<Union> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        AscOrDesc.Asc -> encoder.encodeString("asc")
        AscOrDesc.Desc -> encoder.encodeString("desc")
        is CaseString -> encoder.encodeString(value.value)
      }
    }

    override fun deserialize(decoder: Decoder): Union = when(val value = decoder.decodeString()) {
      "asc" -> AscOrDesc.Asc
      "desc" -> AscOrDesc.Desc
      else -> CaseString(value)
    }
  }
}
