package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Information about an active dismissal request for this Dependabot alert.
 */
@Serializable
public data class DependabotAlertDismissalRequestSimple(
  public val id: Long? = null,
  public val status: Status? = null,
  public val requester: Requester? = null,
  @SerialName("created_at")
  public val createdAt: Instant? = null,
  public val url: String? = null,
) {
  /**
   * The user who requested the dismissal.
   */
  @Serializable
  public data class Requester(
    public val id: Long? = null,
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
