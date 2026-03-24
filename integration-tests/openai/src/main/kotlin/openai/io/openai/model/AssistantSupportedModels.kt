package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AssistantSupportedModels(
  public val `value`: String,
) {
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
  @SerialName("o3-mini")
  O3Mini("o3-mini"),
  @SerialName("o3-mini-2025-01-31")
  O3Mini20250131("o3-mini-2025-01-31"),
  @SerialName("o1")
  O1("o1"),
  @SerialName("o1-2024-12-17")
  O120241217("o1-2024-12-17"),
  @SerialName("gpt-4o")
  Gpt4o("gpt-4o"),
  @SerialName("gpt-4o-2024-11-20")
  Gpt4o20241120("gpt-4o-2024-11-20"),
  @SerialName("gpt-4o-2024-08-06")
  Gpt4o20240806("gpt-4o-2024-08-06"),
  @SerialName("gpt-4o-2024-05-13")
  Gpt4o20240513("gpt-4o-2024-05-13"),
  @SerialName("gpt-4o-mini")
  Gpt4oMini("gpt-4o-mini"),
  @SerialName("gpt-4o-mini-2024-07-18")
  Gpt4oMini20240718("gpt-4o-mini-2024-07-18"),
  @SerialName("gpt-4.5-preview")
  Gpt45Preview("gpt-4.5-preview"),
  @SerialName("gpt-4.5-preview-2025-02-27")
  Gpt45Preview20250227("gpt-4.5-preview-2025-02-27"),
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
