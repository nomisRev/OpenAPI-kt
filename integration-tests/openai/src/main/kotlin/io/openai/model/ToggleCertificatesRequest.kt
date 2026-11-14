package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ToggleCertificatesRequest(
  @SerialName("certificate_ids")
  public val certificateIds: List<String>,
)
