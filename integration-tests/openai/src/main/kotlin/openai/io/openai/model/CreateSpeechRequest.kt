package io.openai.model

import kotlin.Double
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
public data class CreateSpeechRequest(
  public val model: Model,
  public val input: String,
  public val instructions: String? = null,
  public val voice: VoiceIdsOrCustomVoice,
  @SerialName("response_format")
  public val responseFormat: ResponseFormat? = null,
  public val speed: Double? = null,
  @SerialName("stream_format")
  public val streamFormat: StreamFormat? = null,
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
    public enum class Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215(
      override val `value`: String,
    ) : Model {
      @SerialName("tts-1")
      Tts1("tts-1"),
      @SerialName("tts-1-hd")
      Tts1Hd("tts-1-hd"),
      @SerialName("gpt-4o-mini-tts")
      Gpt4oMiniTts("gpt-4o-mini-tts"),
      @SerialName("gpt-4o-mini-tts-2025-12-15")
      Gpt4oMiniTts20251215("gpt-4o-mini-tts-2025-12-15"),
      ;
    }

    public object Serializer : KSerializer<Model> {
      override val descriptor: SerialDescriptor = String.serializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Model) {
        when(value) {
          Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Tts1 -> encoder.encodeString("tts-1")
          Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Tts1Hd -> encoder.encodeString("tts-1-hd")
          Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Gpt4oMiniTts -> encoder.encodeString("gpt-4o-mini-tts")
          Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Gpt4oMiniTts20251215 -> encoder.encodeString("gpt-4o-mini-tts-2025-12-15")
          is CaseString -> encoder.encodeString(value.value)
        }
      }

      override fun deserialize(decoder: Decoder): Model = when(val value = decoder.decodeString()) {
        "tts-1" -> Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Tts1
        "tts-1-hd" -> Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Tts1Hd
        "gpt-4o-mini-tts" -> Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Gpt4oMiniTts
        "gpt-4o-mini-tts-2025-12-15" -> Tts1OrTts1HdOrGpt4oMiniTtsOrGpt4oMiniTts20251215.Gpt4oMiniTts20251215
        else -> CaseString(value)
      }
    }
  }

  @Serializable
  public enum class ResponseFormat(
    public val `value`: String,
  ) {
    @SerialName("mp3")
    Mp3("mp3"),
    @SerialName("opus")
    Opus("opus"),
    @SerialName("aac")
    Aac("aac"),
    @SerialName("flac")
    Flac("flac"),
    @SerialName("wav")
    Wav("wav"),
    @SerialName("pcm")
    Pcm("pcm"),
    ;
  }

  @Serializable
  public enum class StreamFormat(
    public val `value`: String,
  ) {
    @SerialName("sse")
    Sse("sse"),
    @SerialName("audio")
    Audio("audio"),
    ;
  }
}
