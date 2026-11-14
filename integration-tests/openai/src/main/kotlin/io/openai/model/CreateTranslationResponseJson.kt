package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class CreateTranslationResponseJson(
  public val text: String,
)
