package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AudioResponseFormat(
  public val `value`: String,
) {
  @SerialName("json")
  Json("json"),
  @SerialName("text")
  Text("text"),
  @SerialName("srt")
  Srt("srt"),
  @SerialName("verbose_json")
  VerboseJson("verbose_json"),
  @SerialName("vtt")
  Vtt("vtt"),
  @SerialName("diarized_json")
  DiarizedJson("diarized_json"),
  ;
}
