package io.openai.model

import kotlinx.serialization.Serializable

@Serializable
public data class ModifyRunRequest(
  public val metadata: Metadata? = null,
)
