package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An SSH key granting access to a single repository.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DeployKey(
  public val id: Long,
  public val key: String,
  public val url: String,
  public val title: String,
  public val verified: Boolean,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("read_only")
  public val readOnly: Boolean,
  @SerialName("added_by")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val addedBy: Optional<String> = Optional.Absent,
  @SerialName("last_used")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val lastUsed: Optional<Instant> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
)
