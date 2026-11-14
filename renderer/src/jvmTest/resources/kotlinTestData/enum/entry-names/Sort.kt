package io.github.nomisrev.render.test.`enum`.entry.names

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Sort.Serializer::class)
public sealed interface Sort {
  public val `value`: String

  @Serializable
  @JvmInline
  public value class CaseString(
    override val `value`: String,
  ) : Sort

  @Serializable
  public enum class LastActivityOrLastActivity(
    override val `value`: String,
  ) : Sort {
    @SerialName("last_activity")
    LastActivity("last_activity"),
    @SerialName("-last_activity")
    MinusLastActivity("-last_activity"),
    ;
  }

  public object Serializer : KSerializer<Sort> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, `value`: Sort) {
      when(value) {
        LastActivityOrLastActivity.LastActivity -> encoder.encodeString("last_activity")
        LastActivityOrLastActivity.MinusLastActivity -> encoder.encodeString("-last_activity")
        is CaseString -> encoder.encodeString(value.value)
      }
    }

    override fun deserialize(decoder: Decoder): Sort = when(val value = decoder.decodeString()) {
      "last_activity" -> LastActivityOrLastActivity.LastActivity
      "-last_activity" -> LastActivityOrLastActivity.MinusLastActivity
      else -> CaseString(value)
    }
  }
}
