package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual project.
 */
@Serializable
public data class Project(
  public val id: String,
  public val `object`: Object,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("archived_at")
  public val archivedAt: Long? = null,
  public val status: Status,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.project")
    OrganizationProject("organization.project"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    @SerialName("archived")
    Archived("archived"),
    ;
  }
}
