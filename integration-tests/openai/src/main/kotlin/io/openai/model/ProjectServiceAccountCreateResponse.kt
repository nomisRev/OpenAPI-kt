package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectServiceAccountCreateResponse(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  public val role: Role,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("api_key")
  public val apiKey: ProjectServiceAccountApiKey,
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
    @SerialName("member")
    Member("member"),
    ;
  }
}
