package io.openai.model

import kotlinx.serialization.Serializable

@Serializable
public data class UpdateConversationBody(
  public val metadata: Metadata?,
)
