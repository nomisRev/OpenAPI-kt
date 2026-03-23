package io.youtrack.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class WorkItemTypeWrite(
  public val name: String? = null,
  public val autoAttached: Boolean? = null,
)
