package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Role assignment linking a group to a role.
 */
@Serializable
public data class GroupRoleAssignment(
  public val `object`: Object,
  public val group: Group,
  public val role: Role,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("group.role")
    GroupRole("group.role"),
    ;
  }
}
