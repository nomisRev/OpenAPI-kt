package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user who subscribed for notifications about an issue.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueWatcher(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val issue: IssueRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isStarred: Boolean? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
