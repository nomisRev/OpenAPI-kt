package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class ModifyCertificateRequest(
  public val name: String,
)
