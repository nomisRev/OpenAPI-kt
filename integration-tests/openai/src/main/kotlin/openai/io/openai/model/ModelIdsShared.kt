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

@Serializable(with = ModelIdsShared.Serializer::class)
public sealed interface ModelIdsShared {
  public val `value`: String

  @Serializable
  @JvmInline
  public value class CaseString(
    override val `value`: String,
  ) : ModelIdsShared

  @Serializable
  public enum class CaseEnum(
    override val `value`: String,
  ) : ModelIdsShared {
    @SerialName("gpt-5.4")
    Gpt54("gpt-5.4"),
    @SerialName("gpt-5.3-chat-latest")
    Gpt53ChatLatest("gpt-5.3-chat-latest"),
    @SerialName("gpt-5.2")
    Gpt52("gpt-5.2"),
    @SerialName("gpt-5.2-2025-12-11")
    Gpt5220251211("gpt-5.2-2025-12-11"),
    @SerialName("gpt-5.2-chat-latest")
    Gpt52ChatLatest("gpt-5.2-chat-latest"),
    @SerialName("gpt-5.2-pro")
    Gpt52Pro("gpt-5.2-pro"),
    @SerialName("gpt-5.2-pro-2025-12-11")
    Gpt52Pro20251211("gpt-5.2-pro-2025-12-11"),
    @SerialName("gpt-5.1")
    Gpt51("gpt-5.1"),
    @SerialName("gpt-5.1-2025-11-13")
    Gpt5120251113("gpt-5.1-2025-11-13"),
    @SerialName("gpt-5.1-codex")
    Gpt51Codex("gpt-5.1-codex"),
    @SerialName("gpt-5.1-mini")
    Gpt51Mini("gpt-5.1-mini"),
    @SerialName("gpt-5.1-chat-latest")
    Gpt51ChatLatest("gpt-5.1-chat-latest"),
    @SerialName("gpt-5")
    Gpt5("gpt-5"),
    @SerialName("gpt-5-mini")
    Gpt5Mini("gpt-5-mini"),
    @SerialName("gpt-5-nano")
    Gpt5Nano("gpt-5-nano"),
    @SerialName("gpt-5-2025-08-07")
    Gpt520250807("gpt-5-2025-08-07"),
    @SerialName("gpt-5-mini-2025-08-07")
    Gpt5Mini20250807("gpt-5-mini-2025-08-07"),
    @SerialName("gpt-5-nano-2025-08-07")
    Gpt5Nano20250807("gpt-5-nano-2025-08-07"),
    @SerialName("gpt-5-chat-latest")
    Gpt5ChatLatest("gpt-5-chat-latest"),
    @SerialName("gpt-4.1")
    Gpt41("gpt-4.1"),
    @SerialName("gpt-4.1-mini")
    Gpt41Mini("gpt-4.1-mini"),
    @SerialName("gpt-4.1-nano")
    Gpt41Nano("gpt-4.1-nano"),
    @SerialName("gpt-4.1-2025-04-14")
    Gpt4120250414("gpt-4.1-2025-04-14"),
    @SerialName("gpt-4.1-mini-2025-04-14")
    Gpt41Mini20250414("gpt-4.1-mini-2025-04-14"),
    @SerialName("gpt-4.1-nano-2025-04-14")
    Gpt41Nano20250414("gpt-4.1-nano-2025-04-14"),
    @SerialName("o4-mini")
    O4Mini("o4-mini"),
    @SerialName("o4-mini-2025-04-16")
    O4Mini20250416("o4-mini-2025-04-16"),
    @SerialName("o3")
    O3("o3"),
    @SerialName("o3-2025-04-16")
    O320250416("o3-2025-04-16"),
    @SerialName("o3-mini")
    O3Mini("o3-mini"),
    @SerialName("o3-mini-2025-01-31")
    O3Mini20250131("o3-mini-2025-01-31"),
    @SerialName("o1")
    O1("o1"),
    @SerialName("o1-2024-12-17")
    O120241217("o1-2024-12-17"),
    @SerialName("o1-preview")
    O1Preview("o1-preview"),
    @SerialName("o1-preview-2024-09-12")
    O1Preview20240912("o1-preview-2024-09-12"),
    @SerialName("o1-mini")
    O1Mini("o1-mini"),
    @SerialName("o1-mini-2024-09-12")
    O1Mini20240912("o1-mini-2024-09-12"),
    @SerialName("gpt-4o")
    Gpt4o("gpt-4o"),
    @SerialName("gpt-4o-2024-11-20")
    Gpt4o20241120("gpt-4o-2024-11-20"),
    @SerialName("gpt-4o-2024-08-06")
    Gpt4o20240806("gpt-4o-2024-08-06"),
    @SerialName("gpt-4o-2024-05-13")
    Gpt4o20240513("gpt-4o-2024-05-13"),
    @SerialName("gpt-4o-audio-preview")
    Gpt4oAudioPreview("gpt-4o-audio-preview"),
    @SerialName("gpt-4o-audio-preview-2024-10-01")
    Gpt4oAudioPreview20241001("gpt-4o-audio-preview-2024-10-01"),
    @SerialName("gpt-4o-audio-preview-2024-12-17")
    Gpt4oAudioPreview20241217("gpt-4o-audio-preview-2024-12-17"),
    @SerialName("gpt-4o-audio-preview-2025-06-03")
    Gpt4oAudioPreview20250603("gpt-4o-audio-preview-2025-06-03"),
    @SerialName("gpt-4o-mini-audio-preview")
    Gpt4oMiniAudioPreview("gpt-4o-mini-audio-preview"),
    @SerialName("gpt-4o-mini-audio-preview-2024-12-17")
    Gpt4oMiniAudioPreview20241217("gpt-4o-mini-audio-preview-2024-12-17"),
    @SerialName("gpt-4o-search-preview")
    Gpt4oSearchPreview("gpt-4o-search-preview"),
    @SerialName("gpt-4o-mini-search-preview")
    Gpt4oMiniSearchPreview("gpt-4o-mini-search-preview"),
    @SerialName("gpt-4o-search-preview-2025-03-11")
    Gpt4oSearchPreview20250311("gpt-4o-search-preview-2025-03-11"),
    @SerialName("gpt-4o-mini-search-preview-2025-03-11")
    Gpt4oMiniSearchPreview20250311("gpt-4o-mini-search-preview-2025-03-11"),
    @SerialName("chatgpt-4o-latest")
    Chatgpt4oLatest("chatgpt-4o-latest"),
    @SerialName("codex-mini-latest")
    CodexMiniLatest("codex-mini-latest"),
    @SerialName("gpt-4o-mini")
    Gpt4oMini("gpt-4o-mini"),
    @SerialName("gpt-4o-mini-2024-07-18")
    Gpt4oMini20240718("gpt-4o-mini-2024-07-18"),
    @SerialName("gpt-4-turbo")
    Gpt4Turbo("gpt-4-turbo"),
    @SerialName("gpt-4-turbo-2024-04-09")
    Gpt4Turbo20240409("gpt-4-turbo-2024-04-09"),
    @SerialName("gpt-4-0125-preview")
    Gpt40125Preview("gpt-4-0125-preview"),
    @SerialName("gpt-4-turbo-preview")
    Gpt4TurboPreview("gpt-4-turbo-preview"),
    @SerialName("gpt-4-1106-preview")
    Gpt41106Preview("gpt-4-1106-preview"),
    @SerialName("gpt-4-vision-preview")
    Gpt4VisionPreview("gpt-4-vision-preview"),
    @SerialName("gpt-4")
    Gpt4("gpt-4"),
    @SerialName("gpt-4-0314")
    Gpt40314("gpt-4-0314"),
    @SerialName("gpt-4-0613")
    Gpt40613("gpt-4-0613"),
    @SerialName("gpt-4-32k")
    Gpt432k("gpt-4-32k"),
    @SerialName("gpt-4-32k-0314")
    Gpt432k0314("gpt-4-32k-0314"),
    @SerialName("gpt-4-32k-0613")
    Gpt432k0613("gpt-4-32k-0613"),
    @SerialName("gpt-3.5-turbo")
    Gpt35Turbo("gpt-3.5-turbo"),
    @SerialName("gpt-3.5-turbo-16k")
    Gpt35Turbo16k("gpt-3.5-turbo-16k"),
    @SerialName("gpt-3.5-turbo-0301")
    Gpt35Turbo0301("gpt-3.5-turbo-0301"),
    @SerialName("gpt-3.5-turbo-0613")
    Gpt35Turbo0613("gpt-3.5-turbo-0613"),
    @SerialName("gpt-3.5-turbo-1106")
    Gpt35Turbo1106("gpt-3.5-turbo-1106"),
    @SerialName("gpt-3.5-turbo-0125")
    Gpt35Turbo0125("gpt-3.5-turbo-0125"),
    @SerialName("gpt-3.5-turbo-16k-0613")
    Gpt35Turbo16k0613("gpt-3.5-turbo-16k-0613"),
    ;
  }

  public object Serializer : KSerializer<ModelIdsShared> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, `value`: ModelIdsShared) {
      when(value) {
        CaseEnum.Gpt54 -> encoder.encodeString("gpt-5.4")
        CaseEnum.Gpt53ChatLatest -> encoder.encodeString("gpt-5.3-chat-latest")
        CaseEnum.Gpt52 -> encoder.encodeString("gpt-5.2")
        CaseEnum.Gpt5220251211 -> encoder.encodeString("gpt-5.2-2025-12-11")
        CaseEnum.Gpt52ChatLatest -> encoder.encodeString("gpt-5.2-chat-latest")
        CaseEnum.Gpt52Pro -> encoder.encodeString("gpt-5.2-pro")
        CaseEnum.Gpt52Pro20251211 -> encoder.encodeString("gpt-5.2-pro-2025-12-11")
        CaseEnum.Gpt51 -> encoder.encodeString("gpt-5.1")
        CaseEnum.Gpt5120251113 -> encoder.encodeString("gpt-5.1-2025-11-13")
        CaseEnum.Gpt51Codex -> encoder.encodeString("gpt-5.1-codex")
        CaseEnum.Gpt51Mini -> encoder.encodeString("gpt-5.1-mini")
        CaseEnum.Gpt51ChatLatest -> encoder.encodeString("gpt-5.1-chat-latest")
        CaseEnum.Gpt5 -> encoder.encodeString("gpt-5")
        CaseEnum.Gpt5Mini -> encoder.encodeString("gpt-5-mini")
        CaseEnum.Gpt5Nano -> encoder.encodeString("gpt-5-nano")
        CaseEnum.Gpt520250807 -> encoder.encodeString("gpt-5-2025-08-07")
        CaseEnum.Gpt5Mini20250807 -> encoder.encodeString("gpt-5-mini-2025-08-07")
        CaseEnum.Gpt5Nano20250807 -> encoder.encodeString("gpt-5-nano-2025-08-07")
        CaseEnum.Gpt5ChatLatest -> encoder.encodeString("gpt-5-chat-latest")
        CaseEnum.Gpt41 -> encoder.encodeString("gpt-4.1")
        CaseEnum.Gpt41Mini -> encoder.encodeString("gpt-4.1-mini")
        CaseEnum.Gpt41Nano -> encoder.encodeString("gpt-4.1-nano")
        CaseEnum.Gpt4120250414 -> encoder.encodeString("gpt-4.1-2025-04-14")
        CaseEnum.Gpt41Mini20250414 -> encoder.encodeString("gpt-4.1-mini-2025-04-14")
        CaseEnum.Gpt41Nano20250414 -> encoder.encodeString("gpt-4.1-nano-2025-04-14")
        CaseEnum.O4Mini -> encoder.encodeString("o4-mini")
        CaseEnum.O4Mini20250416 -> encoder.encodeString("o4-mini-2025-04-16")
        CaseEnum.O3 -> encoder.encodeString("o3")
        CaseEnum.O320250416 -> encoder.encodeString("o3-2025-04-16")
        CaseEnum.O3Mini -> encoder.encodeString("o3-mini")
        CaseEnum.O3Mini20250131 -> encoder.encodeString("o3-mini-2025-01-31")
        CaseEnum.O1 -> encoder.encodeString("o1")
        CaseEnum.O120241217 -> encoder.encodeString("o1-2024-12-17")
        CaseEnum.O1Preview -> encoder.encodeString("o1-preview")
        CaseEnum.O1Preview20240912 -> encoder.encodeString("o1-preview-2024-09-12")
        CaseEnum.O1Mini -> encoder.encodeString("o1-mini")
        CaseEnum.O1Mini20240912 -> encoder.encodeString("o1-mini-2024-09-12")
        CaseEnum.Gpt4o -> encoder.encodeString("gpt-4o")
        CaseEnum.Gpt4o20241120 -> encoder.encodeString("gpt-4o-2024-11-20")
        CaseEnum.Gpt4o20240806 -> encoder.encodeString("gpt-4o-2024-08-06")
        CaseEnum.Gpt4o20240513 -> encoder.encodeString("gpt-4o-2024-05-13")
        CaseEnum.Gpt4oAudioPreview -> encoder.encodeString("gpt-4o-audio-preview")
        CaseEnum.Gpt4oAudioPreview20241001 -> encoder.encodeString("gpt-4o-audio-preview-2024-10-01")
        CaseEnum.Gpt4oAudioPreview20241217 -> encoder.encodeString("gpt-4o-audio-preview-2024-12-17")
        CaseEnum.Gpt4oAudioPreview20250603 -> encoder.encodeString("gpt-4o-audio-preview-2025-06-03")
        CaseEnum.Gpt4oMiniAudioPreview -> encoder.encodeString("gpt-4o-mini-audio-preview")
        CaseEnum.Gpt4oMiniAudioPreview20241217 -> encoder.encodeString("gpt-4o-mini-audio-preview-2024-12-17")
        CaseEnum.Gpt4oSearchPreview -> encoder.encodeString("gpt-4o-search-preview")
        CaseEnum.Gpt4oMiniSearchPreview -> encoder.encodeString("gpt-4o-mini-search-preview")
        CaseEnum.Gpt4oSearchPreview20250311 -> encoder.encodeString("gpt-4o-search-preview-2025-03-11")
        CaseEnum.Gpt4oMiniSearchPreview20250311 -> encoder.encodeString("gpt-4o-mini-search-preview-2025-03-11")
        CaseEnum.Chatgpt4oLatest -> encoder.encodeString("chatgpt-4o-latest")
        CaseEnum.CodexMiniLatest -> encoder.encodeString("codex-mini-latest")
        CaseEnum.Gpt4oMini -> encoder.encodeString("gpt-4o-mini")
        CaseEnum.Gpt4oMini20240718 -> encoder.encodeString("gpt-4o-mini-2024-07-18")
        CaseEnum.Gpt4Turbo -> encoder.encodeString("gpt-4-turbo")
        CaseEnum.Gpt4Turbo20240409 -> encoder.encodeString("gpt-4-turbo-2024-04-09")
        CaseEnum.Gpt40125Preview -> encoder.encodeString("gpt-4-0125-preview")
        CaseEnum.Gpt4TurboPreview -> encoder.encodeString("gpt-4-turbo-preview")
        CaseEnum.Gpt41106Preview -> encoder.encodeString("gpt-4-1106-preview")
        CaseEnum.Gpt4VisionPreview -> encoder.encodeString("gpt-4-vision-preview")
        CaseEnum.Gpt4 -> encoder.encodeString("gpt-4")
        CaseEnum.Gpt40314 -> encoder.encodeString("gpt-4-0314")
        CaseEnum.Gpt40613 -> encoder.encodeString("gpt-4-0613")
        CaseEnum.Gpt432k -> encoder.encodeString("gpt-4-32k")
        CaseEnum.Gpt432k0314 -> encoder.encodeString("gpt-4-32k-0314")
        CaseEnum.Gpt432k0613 -> encoder.encodeString("gpt-4-32k-0613")
        CaseEnum.Gpt35Turbo -> encoder.encodeString("gpt-3.5-turbo")
        CaseEnum.Gpt35Turbo16k -> encoder.encodeString("gpt-3.5-turbo-16k")
        CaseEnum.Gpt35Turbo0301 -> encoder.encodeString("gpt-3.5-turbo-0301")
        CaseEnum.Gpt35Turbo0613 -> encoder.encodeString("gpt-3.5-turbo-0613")
        CaseEnum.Gpt35Turbo1106 -> encoder.encodeString("gpt-3.5-turbo-1106")
        CaseEnum.Gpt35Turbo0125 -> encoder.encodeString("gpt-3.5-turbo-0125")
        CaseEnum.Gpt35Turbo16k0613 -> encoder.encodeString("gpt-3.5-turbo-16k-0613")
        is CaseString -> encoder.encodeString(value.value)
      }
    }

    override fun deserialize(decoder: Decoder): ModelIdsShared = when(val value = decoder.decodeString()) {
      "gpt-5.4" -> CaseEnum.Gpt54
      "gpt-5.3-chat-latest" -> CaseEnum.Gpt53ChatLatest
      "gpt-5.2" -> CaseEnum.Gpt52
      "gpt-5.2-2025-12-11" -> CaseEnum.Gpt5220251211
      "gpt-5.2-chat-latest" -> CaseEnum.Gpt52ChatLatest
      "gpt-5.2-pro" -> CaseEnum.Gpt52Pro
      "gpt-5.2-pro-2025-12-11" -> CaseEnum.Gpt52Pro20251211
      "gpt-5.1" -> CaseEnum.Gpt51
      "gpt-5.1-2025-11-13" -> CaseEnum.Gpt5120251113
      "gpt-5.1-codex" -> CaseEnum.Gpt51Codex
      "gpt-5.1-mini" -> CaseEnum.Gpt51Mini
      "gpt-5.1-chat-latest" -> CaseEnum.Gpt51ChatLatest
      "gpt-5" -> CaseEnum.Gpt5
      "gpt-5-mini" -> CaseEnum.Gpt5Mini
      "gpt-5-nano" -> CaseEnum.Gpt5Nano
      "gpt-5-2025-08-07" -> CaseEnum.Gpt520250807
      "gpt-5-mini-2025-08-07" -> CaseEnum.Gpt5Mini20250807
      "gpt-5-nano-2025-08-07" -> CaseEnum.Gpt5Nano20250807
      "gpt-5-chat-latest" -> CaseEnum.Gpt5ChatLatest
      "gpt-4.1" -> CaseEnum.Gpt41
      "gpt-4.1-mini" -> CaseEnum.Gpt41Mini
      "gpt-4.1-nano" -> CaseEnum.Gpt41Nano
      "gpt-4.1-2025-04-14" -> CaseEnum.Gpt4120250414
      "gpt-4.1-mini-2025-04-14" -> CaseEnum.Gpt41Mini20250414
      "gpt-4.1-nano-2025-04-14" -> CaseEnum.Gpt41Nano20250414
      "o4-mini" -> CaseEnum.O4Mini
      "o4-mini-2025-04-16" -> CaseEnum.O4Mini20250416
      "o3" -> CaseEnum.O3
      "o3-2025-04-16" -> CaseEnum.O320250416
      "o3-mini" -> CaseEnum.O3Mini
      "o3-mini-2025-01-31" -> CaseEnum.O3Mini20250131
      "o1" -> CaseEnum.O1
      "o1-2024-12-17" -> CaseEnum.O120241217
      "o1-preview" -> CaseEnum.O1Preview
      "o1-preview-2024-09-12" -> CaseEnum.O1Preview20240912
      "o1-mini" -> CaseEnum.O1Mini
      "o1-mini-2024-09-12" -> CaseEnum.O1Mini20240912
      "gpt-4o" -> CaseEnum.Gpt4o
      "gpt-4o-2024-11-20" -> CaseEnum.Gpt4o20241120
      "gpt-4o-2024-08-06" -> CaseEnum.Gpt4o20240806
      "gpt-4o-2024-05-13" -> CaseEnum.Gpt4o20240513
      "gpt-4o-audio-preview" -> CaseEnum.Gpt4oAudioPreview
      "gpt-4o-audio-preview-2024-10-01" -> CaseEnum.Gpt4oAudioPreview20241001
      "gpt-4o-audio-preview-2024-12-17" -> CaseEnum.Gpt4oAudioPreview20241217
      "gpt-4o-audio-preview-2025-06-03" -> CaseEnum.Gpt4oAudioPreview20250603
      "gpt-4o-mini-audio-preview" -> CaseEnum.Gpt4oMiniAudioPreview
      "gpt-4o-mini-audio-preview-2024-12-17" -> CaseEnum.Gpt4oMiniAudioPreview20241217
      "gpt-4o-search-preview" -> CaseEnum.Gpt4oSearchPreview
      "gpt-4o-mini-search-preview" -> CaseEnum.Gpt4oMiniSearchPreview
      "gpt-4o-search-preview-2025-03-11" -> CaseEnum.Gpt4oSearchPreview20250311
      "gpt-4o-mini-search-preview-2025-03-11" -> CaseEnum.Gpt4oMiniSearchPreview20250311
      "chatgpt-4o-latest" -> CaseEnum.Chatgpt4oLatest
      "codex-mini-latest" -> CaseEnum.CodexMiniLatest
      "gpt-4o-mini" -> CaseEnum.Gpt4oMini
      "gpt-4o-mini-2024-07-18" -> CaseEnum.Gpt4oMini20240718
      "gpt-4-turbo" -> CaseEnum.Gpt4Turbo
      "gpt-4-turbo-2024-04-09" -> CaseEnum.Gpt4Turbo20240409
      "gpt-4-0125-preview" -> CaseEnum.Gpt40125Preview
      "gpt-4-turbo-preview" -> CaseEnum.Gpt4TurboPreview
      "gpt-4-1106-preview" -> CaseEnum.Gpt41106Preview
      "gpt-4-vision-preview" -> CaseEnum.Gpt4VisionPreview
      "gpt-4" -> CaseEnum.Gpt4
      "gpt-4-0314" -> CaseEnum.Gpt40314
      "gpt-4-0613" -> CaseEnum.Gpt40613
      "gpt-4-32k" -> CaseEnum.Gpt432k
      "gpt-4-32k-0314" -> CaseEnum.Gpt432k0314
      "gpt-4-32k-0613" -> CaseEnum.Gpt432k0613
      "gpt-3.5-turbo" -> CaseEnum.Gpt35Turbo
      "gpt-3.5-turbo-16k" -> CaseEnum.Gpt35Turbo16k
      "gpt-3.5-turbo-0301" -> CaseEnum.Gpt35Turbo0301
      "gpt-3.5-turbo-0613" -> CaseEnum.Gpt35Turbo0613
      "gpt-3.5-turbo-1106" -> CaseEnum.Gpt35Turbo1106
      "gpt-3.5-turbo-0125" -> CaseEnum.Gpt35Turbo0125
      "gpt-3.5-turbo-16k-0613" -> CaseEnum.Gpt35Turbo16k0613
      else -> CaseString(value)
    }
  }
}
