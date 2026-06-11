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

@Serializable
public data class AudioTranscription(
  public val model: Model? = null,
  public val language: String? = null,
  public val prompt: String? = null,
) {
  @Serializable(with = Model.Serializer::class)
  public sealed interface Model {
    public val `value`: String

    @Serializable
    @JvmInline
    public value class CaseString(
      override val `value`: String,
    ) : Model

    @Serializable
    public enum class CaseEnum(
      override val `value`: String,
    ) : Model {
      @SerialName("whisper-1")
      Whisper1("whisper-1"),
      @SerialName("gpt-4o-mini-transcribe")
      Gpt4oMiniTranscribe("gpt-4o-mini-transcribe"),
      @SerialName("gpt-4o-mini-transcribe-2025-12-15")
      Gpt4oMiniTranscribe20251215("gpt-4o-mini-transcribe-2025-12-15"),
      @SerialName("gpt-4o-transcribe")
      Gpt4oTranscribe("gpt-4o-transcribe"),
      @SerialName("gpt-4o-transcribe-diarize")
      Gpt4oTranscribeDiarize("gpt-4o-transcribe-diarize"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          CaseEnum.Whisper1 -> encoder.encodeString("whisper-1")
          CaseEnum.Gpt4oMiniTranscribe -> encoder.encodeString("gpt-4o-mini-transcribe")
          CaseEnum.Gpt4oMiniTranscribe20251215 -> encoder.encodeString("gpt-4o-mini-transcribe-2025-12-15")
          CaseEnum.Gpt4oTranscribe -> encoder.encodeString("gpt-4o-transcribe")
          CaseEnum.Gpt4oTranscribeDiarize -> encoder.encodeString("gpt-4o-transcribe-diarize")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "whisper-1" -> CaseEnum.Whisper1
        "gpt-4o-mini-transcribe" -> CaseEnum.Gpt4oMiniTranscribe
        "gpt-4o-mini-transcribe-2025-12-15" -> CaseEnum.Gpt4oMiniTranscribe20251215
        "gpt-4o-transcribe" -> CaseEnum.Gpt4oTranscribe
        "gpt-4o-transcribe-diarize" -> CaseEnum.Gpt4oTranscribeDiarize
        else -> CaseString(value)
      }
    }
  }
}
