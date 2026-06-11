package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual user in a project.
 */
@Serializable
public data class ProjectUser(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  public val email: String,
  public val role: Role,
  @SerialName("added_at")
  public val addedAt: Long,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.project.user")
    OrganizationProjectUser("organization.project.user"),
    ;
  }

  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("owner")
    Owner("owner"),
    @SerialName("member")
    Member("member"),
    ;
  }
}
