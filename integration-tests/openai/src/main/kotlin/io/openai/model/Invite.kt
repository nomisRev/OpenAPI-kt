package io.openai.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual `invite` to the organization.
 */
@Serializable
public data class Invite(
  public val `object`: Object,
  public val id: String,
  public val email: String,
  public val role: Role,
  public val status: Status,
  @SerialName("invited_at")
  public val invitedAt: Long,
  @SerialName("expires_at")
  public val expiresAt: Long,
  @SerialName("accepted_at")
  public val acceptedAt: Long? = null,
  public val projects: List<Projects>? = null,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.invite")
    OrganizationInvite("organization.invite"),
    ;
  }

  @Serializable
  public data class Projects(
    public val id: String? = null,
    public val role: Role? = null,
  ) {
    @Serializable
    public enum class Role(
      public val `value`: String,
    ) {
      @SerialName("member")
      Member("member"),
      @SerialName("owner")
      Owner("owner"),
      ;
    }
  }

  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("owner")
    Owner("owner"),
    @SerialName("reader")
    Reader("reader"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("accepted")
    Accepted("accepted"),
    @SerialName("expired")
    Expired("expired"),
    @SerialName("pending")
    Pending("pending"),
    ;
  }
}
