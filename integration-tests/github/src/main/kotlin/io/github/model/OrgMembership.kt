package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Org Membership
 */
@Serializable
public data class OrgMembership(
  public val url: String,
  public val state: State,
  public val role: Role,
  @SerialName("direct_membership")
  public val directMembership: Boolean? = null,
  @SerialName("enterprise_teams_providing_indirect_membership")
  public val enterpriseTeamsProvidingIndirectMembership: List<String>? = null,
  @SerialName("organization_url")
  public val organizationUrl: String,
  public val organization: OrganizationSimple,
  public val user: NullableSimpleUser?,
  public val permissions: Permissions? = null,
) {
  @JvmInline
  @Serializable
  public value class Permissions(
    @SerialName("can_create_repository")
    public val canCreateRepository: Boolean,
  )

  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("admin")
    Admin("admin"),
    @SerialName("member")
    Member("member"),
    @SerialName("billing_manager")
    BillingManager("billing_manager"),
    ;
  }

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    @SerialName("pending")
    Pending("pending"),
    ;
  }
}
