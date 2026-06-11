package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual API key in a project.
 */
@Serializable
public data class ProjectApiKey(
  public val `object`: Object,
  @SerialName("redacted_value")
  public val redactedValue: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("last_used_at")
  public val lastUsedAt: Long,
  public val id: String,
  public val owner: Owner,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.project.api_key")
    OrganizationProjectApiKey("organization.project.api_key"),
    ;
  }

  @Serializable
  public data class Owner(
    public val type: Type? = null,
    public val user: ProjectUser? = null,
    @SerialName("service_account")
    public val serviceAccount: ProjectServiceAccount? = null,
  ) {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("user")
      User("user"),
      @SerialName("service_account")
      ServiceAccount("service_account"),
      ;
    }
  }
}
