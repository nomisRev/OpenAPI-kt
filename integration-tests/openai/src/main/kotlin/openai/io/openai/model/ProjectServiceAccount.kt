package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual service account in a project.
 */
@Serializable
public data class ProjectServiceAccount(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  public val role: Role,
  @SerialName("created_at")
  public val createdAt: Long,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.project.service_account")
    OrganizationProjectServiceAccount("organization.project.service_account"),
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
