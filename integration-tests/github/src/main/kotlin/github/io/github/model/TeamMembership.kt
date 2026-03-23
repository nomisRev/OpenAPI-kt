package io.github.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Team Membership
 */
@Serializable
public data class TeamMembership(
  public val url: String,
  @Required
  public val role: Role = Role.Member,
  public val state: State,
) {
  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("member")
    Member("member"),
    @SerialName("maintainer")
    Maintainer("maintainer"),
    ;
  }

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    @SerialName("pending")
    Pending("pending"),
    ;
  }
}
