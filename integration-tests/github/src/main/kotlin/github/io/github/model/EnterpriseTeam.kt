package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Group of enterprise owners and/or members
 */
@Serializable
public data class EnterpriseTeam(
  public val id: Long,
  public val name: String,
  public val description: String? = null,
  public val slug: String,
  public val url: String,
  @SerialName("sync_to_organizations")
  public val syncToOrganizations: String? = null,
  @SerialName("organization_selection_type")
  public val organizationSelectionType: String? = null,
  @SerialName("group_id")
  public val groupId: String?,
  @SerialName("group_name")
  public val groupName: String? = null,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("members_url")
  public val membersUrl: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
)
