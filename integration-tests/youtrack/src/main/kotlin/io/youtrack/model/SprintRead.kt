package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SprintRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val agile: AgileRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val goal: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val start: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val finish: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val archived: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isDefault: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issues: List<IssueRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val unresolvedIssuesCount: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val previousSprint: SprintRead? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
