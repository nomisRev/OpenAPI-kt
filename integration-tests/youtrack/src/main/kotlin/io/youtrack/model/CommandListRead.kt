package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CommandListRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val comment: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: CommandVisibilityRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val query: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val caret: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val silent: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runAs: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val commands: List<ParsedCommand>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issues: List<IssueRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val suggestions: List<Suggestion>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
