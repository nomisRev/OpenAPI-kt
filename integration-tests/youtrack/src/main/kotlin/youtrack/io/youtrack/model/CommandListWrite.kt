package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class CommandListWrite(
  public val comment: String? = null,
  public val visibility: CommandVisibilityWrite? = null,
  public val query: String? = null,
  public val caret: Int? = null,
  public val silent: Boolean? = null,
  public val runAs: String? = null,
  public val issues: List<IssueWrite>? = null,
)
