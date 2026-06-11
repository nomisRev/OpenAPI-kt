package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Organization roles
 */
@Serializable
public data class OrganizationRole(
  public val id: Long,
  public val name: String,
  public val description: String? = null,
  @SerialName("base_role")
  public val baseRole: BaseRole? = null,
  public val source: Source? = null,
  public val permissions: List<String>,
  public val organization: NullableSimpleUser?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
) {
  @Serializable
  public enum class BaseRole(
    public val `value`: String,
  ) {
    @SerialName("read")
    Read("read"),
    @SerialName("triage")
    Triage("triage"),
    @SerialName("write")
    Write("write"),
    @SerialName("maintain")
    Maintain("maintain"),
    @SerialName("admin")
    Admin("admin"),
    ;
  }

  @Serializable
  public enum class Source {
    Organization,
    Enterprise,
    Predefined,
  }
}
