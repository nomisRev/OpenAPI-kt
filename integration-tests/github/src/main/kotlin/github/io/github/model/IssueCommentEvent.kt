package io.github.model

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class IssueCommentEvent(
  public val action: String,
  public val issue: Issue,
  public val comment: IssueComment,
)
