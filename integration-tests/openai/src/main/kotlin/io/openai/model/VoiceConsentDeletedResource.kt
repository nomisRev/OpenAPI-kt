package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VoiceConsentDeletedResource(
  public val id: String,
  public val `object`: Object,
  public val deleted: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("audio.voice_consent")
    AudioVoiceConsent("audio.voice_consent"),
    ;
  }
}
