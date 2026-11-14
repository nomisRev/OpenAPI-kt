package io.openai.model

import kotlinx.serialization.Serializable

@Serializable
public data class ModifyMessageRequest(
  public val metadata: Metadata? = null,
)
