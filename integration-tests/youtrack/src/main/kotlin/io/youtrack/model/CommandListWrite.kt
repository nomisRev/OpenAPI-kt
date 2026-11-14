package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CommandListWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val comment: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: CommandVisibilityWrite? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val query: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val caret: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val silent: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runAs: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issues: List<IssueWrite>? = null,
)
