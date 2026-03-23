package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class IssuesEvent(
  public val action: String,
  public val issue: Issue,
  public val assignee: SimpleUser? = null,
  public val assignees: List<SimpleUser>? = null,
  public val label: Label? = null,
  public val labels: List<Label>? = null,
)
