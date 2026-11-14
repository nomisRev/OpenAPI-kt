package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssuesEvent(
  public val action: String,
  public val issue: Issue,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignee: SimpleUser? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val assignees: List<SimpleUser>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val label: Label? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val labels: List<Label>? = null,
)
