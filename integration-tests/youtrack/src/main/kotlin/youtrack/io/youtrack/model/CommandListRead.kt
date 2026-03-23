package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CommandListRead(
  public val id: String? = null,
  public val comment: String? = null,
  public val visibility: CommandVisibilityRead? = null,
  public val query: String? = null,
  public val caret: Int? = null,
  public val silent: Boolean? = null,
  public val runAs: String? = null,
  public val commands: List<ParsedCommand>? = null,
  public val issues: List<IssueRead>? = null,
  public val suggestions: List<Suggestion>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
