package io.openai.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class UploadCertificateRequest(
  public val name: String? = null,
  public val content: String,
)
