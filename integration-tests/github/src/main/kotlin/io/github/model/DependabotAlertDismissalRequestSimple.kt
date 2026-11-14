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
 * Information about an active dismissal request for this Dependabot alert.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class DependabotAlertDismissalRequestSimple(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val status: Status? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val requester: Requester? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: Instant? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val url: String? = null,
) {
  /**
   * The user who requested the dismissal.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Requester(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val login: String? = null,
  )

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("pending")
    Pending("pending"),
    @SerialName("approved")
    Approved("approved"),
    @SerialName("rejected")
    Rejected("rejected"),
    @SerialName("cancelled")
    Cancelled("cancelled"),
    ;
  }
}
