package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class InviteRequest(
  public val email: String,
  public val role: Role,
  public val projects: List<Projects>? = null,
) {
  @Serializable
  public data class Projects(
    public val id: String,
    public val role: Role,
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
    @SerialName("reader")
    Reader("reader"),
    @SerialName("owner")
    Owner("owner"),
    ;
  }
}
