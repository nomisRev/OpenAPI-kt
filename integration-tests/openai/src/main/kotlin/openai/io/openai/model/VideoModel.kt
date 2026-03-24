package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = VideoModel.Serializer::class)
public sealed interface VideoModel {
  public val `value`: String

  @Serializable
  @JvmInline
  public value class CaseString(
    override val `value`: String,
  ) : VideoModel

  @Serializable
  public enum class Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208(
    override val `value`: String,
  ) : VideoModel {
    @SerialName("sora-2")
    Sora2("sora-2"),
    @SerialName("sora-2-pro")
    Sora2Pro("sora-2-pro"),
    @SerialName("sora-2-2025-10-06")
    Sora220251006("sora-2-2025-10-06"),
    @SerialName("sora-2-pro-2025-10-06")
    Sora2Pro20251006("sora-2-pro-2025-10-06"),
    @SerialName("sora-2-2025-12-08")
    Sora220251208("sora-2-2025-12-08"),
    ;
  }

  public object Serializer : KSerializer<VideoModel> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, `value`: VideoModel) {
      when(value) {
        Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora2 -> encoder.encodeString("sora-2")
        Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora2Pro -> encoder.encodeString("sora-2-pro")
        Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora220251006 -> encoder.encodeString("sora-2-2025-10-06")
        Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora2Pro20251006 -> encoder.encodeString("sora-2-pro-2025-10-06")
        Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora220251208 -> encoder.encodeString("sora-2-2025-12-08")
        is CaseString -> encoder.encodeString(value.value)
      }
    }

    override fun deserialize(decoder: Decoder): VideoModel = when(val value = decoder.decodeString()) {
      "sora-2" -> Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora2
      "sora-2-pro" -> Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora2Pro
      "sora-2-2025-10-06" -> Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora220251006
      "sora-2-pro-2025-10-06" -> Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora2Pro20251006
      "sora-2-2025-12-08" -> Sora2OrSora2ProOrSora220251006OrSora2Pro20251006OrSora220251208.Sora220251208
      else -> CaseString(value)
    }
  }
}
