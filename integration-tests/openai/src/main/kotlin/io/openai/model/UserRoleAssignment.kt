package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Role assignment linking a user to a role.
 */
@Serializable
public data class UserRoleAssignment(
  public val `object`: Object,
  public val user: User,
  public val role: Role,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("user.role")
    UserRole("user.role"),
    ;
  }
}
