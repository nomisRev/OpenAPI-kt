package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectServiceAccountApiKey(
  public val `object`: Object,
  public val `value`: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val id: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.project.service_account.api_key")
    OrganizationProjectServiceAccountApiKey("organization.project.service_account.api_key"),
    ;
  }
}
