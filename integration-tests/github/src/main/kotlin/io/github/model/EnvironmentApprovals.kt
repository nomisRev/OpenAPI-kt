package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An entry in the reviews log for environment deployments
 */
@Serializable
public data class EnvironmentApprovals(
  public val environments: List<Environments>,
  public val state: State,
  public val user: SimpleUser,
  public val comment: String,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Environments(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @SerialName("node_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nodeId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    @SerialName("created_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updatedAt: Instant? = null,
  )

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("approved")
    Approved("approved"),
    @SerialName("rejected")
    Rejected("rejected"),
    @SerialName("pending")
    Pending("pending"),
    ;
  }
}
