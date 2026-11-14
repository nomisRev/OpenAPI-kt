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
 * Represents users that have voted for the issue or its duplicates.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueVoters(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasVote: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val original: List<UserRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val duplicate: List<DuplicateVote>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
