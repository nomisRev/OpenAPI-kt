package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gist Commit
 */
@Serializable
public data class GistCommit(
  public val url: String,
  public val version: String,
  public val user: NullableSimpleUser?,
  @SerialName("change_status")
  public val changeStatus: ChangeStatus,
  @SerialName("committed_at")
  public val committedAt: Instant,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ChangeStatus(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val total: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val additions: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val deletions: Long? = null,
  )
}
