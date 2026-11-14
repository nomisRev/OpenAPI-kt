package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class UpdateVoiceConsentRequest(
  public val name: String,
)
