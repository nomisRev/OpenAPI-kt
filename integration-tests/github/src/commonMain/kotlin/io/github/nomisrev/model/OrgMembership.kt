package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class OrgMembership(
    val url: String,
    val state: State,
    val role: Role,
    @SerialName("direct_membership") val directMembership: Boolean? = null,
    @SerialName("enterprise_teams_providing_indirect_membership") val enterpriseTeamsProvidingIndirectMembership: List<String>? = null,
    @SerialName("organization_url") val organizationUrl: String,
    val organization: OrganizationSimple,
    val user: NullableSimpleUser?,
    val permissions: Permissions? = null,
) {
    @Serializable
    enum class State {
        @SerialName("active") Active, @SerialName("pending") Pending;
    }

    @Serializable
    enum class Role {
        @SerialName("admin") Admin, @SerialName("member") Member, @SerialName("billing_manager") BillingManager;
    }

    @Serializable
    @JvmInline
    value class Permissions(@SerialName("can_create_repository") val canCreateRepository: Boolean)
}
