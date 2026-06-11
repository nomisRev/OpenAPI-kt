package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A credit given to a user for a repository security advisory.
 */
@Serializable
public data class RepositoryAdvisoryCredit(
  public val user: SimpleUser,
  public val type: SecurityAdvisoryCreditTypes,
  public val state: State,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("accepted")
    Accepted("accepted"),
    @SerialName("declined")
    Declined("declined"),
    @SerialName("pending")
    Pending("pending"),
    ;
  }
}
