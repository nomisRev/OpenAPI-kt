package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PullRequestEvent(
  public val action: String,
  public val number: Long,
  @SerialName("pull_request")
  public val pullRequest: PullRequestMinimal,
  public val assignee: SimpleUser? = null,
  public val assignees: List<SimpleUser>? = null,
  public val label: Label? = null,
  public val labels: List<Label>? = null,
)
