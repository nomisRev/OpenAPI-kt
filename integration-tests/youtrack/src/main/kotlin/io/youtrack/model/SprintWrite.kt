package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SprintWrite(
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
  public val issues: List<IssueWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val previousSprint: SprintWrite? = null,
)
