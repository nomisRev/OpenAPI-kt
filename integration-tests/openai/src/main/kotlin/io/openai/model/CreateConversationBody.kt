package io.openai.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class CreateConversationBody(
  public val metadata: Metadata? = null,
  public val items: List<InputItem>? = null,
)
