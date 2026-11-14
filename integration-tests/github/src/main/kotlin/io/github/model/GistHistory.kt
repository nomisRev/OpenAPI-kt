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
 * Gist History
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class GistHistory(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val user: Optional<NullableSimpleUser> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val version: String? = null,
  @SerialName("committed_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val committedAt: Instant? = null,
  @SerialName("change_status")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val changeStatus: ChangeStatus? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
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
