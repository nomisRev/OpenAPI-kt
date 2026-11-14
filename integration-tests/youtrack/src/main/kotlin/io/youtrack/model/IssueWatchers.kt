package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents users that are subscribed to notifications about the issue.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueWatchers(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasStar: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issueWatchers: List<IssueWatcher>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val duplicateWatchers: List<IssueWatcher>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
