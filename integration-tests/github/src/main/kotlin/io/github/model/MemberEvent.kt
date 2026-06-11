package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class MemberEvent(
  public val action: String,
  public val member: SimpleUser,
)
