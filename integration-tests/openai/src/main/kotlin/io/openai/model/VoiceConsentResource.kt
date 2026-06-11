package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A consent recording used to authorize creation of a custom voice.
 */
@Serializable
public data class VoiceConsentResource(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  public val language: String,
  @SerialName("created_at")
  public val createdAt: Long,
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
