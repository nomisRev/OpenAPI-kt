package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Team Organization
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TeamOrganization(
  public val login: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  @SerialName("repos_url")
  public val reposUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("hooks_url")
  public val hooksUrl: String,
  @SerialName("issues_url")
  public val issuesUrl: String,
  @SerialName("members_url")
  public val membersUrl: String,
  @SerialName("public_members_url")
  public val publicMembersUrl: String,
  @SerialName("avatar_url")
  public val avatarUrl: String,
  public val description: String?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val company: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val blog: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val location: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: String? = null,
  @SerialName("twitter_username")
  public val twitterUsername: String? = null,
  @SerialName("is_verified")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isVerified: Boolean? = null,
  @SerialName("has_organization_projects")
  public val hasOrganizationProjects: Boolean,
  @SerialName("has_repository_projects")
  public val hasRepositoryProjects: Boolean,
  @SerialName("public_repos")
  public val publicRepos: Long,
  @SerialName("public_gists")
  public val publicGists: Long,
  public val followers: Long,
  public val following: Long,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  public val type: String,
  @SerialName("total_private_repos")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val totalPrivateRepos: Long? = null,
  @SerialName("owned_private_repos")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ownedPrivateRepos: Long? = null,
  @SerialName("private_gists")
  public val privateGists: Long? = null,
  @SerialName("disk_usage")
  public val diskUsage: Long? = null,
  public val collaborators: Long? = null,
  @SerialName("billing_email")
  public val billingEmail: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val plan: Plan? = null,
  @SerialName("default_repository_permission")
  public val defaultRepositoryPermission: String? = null,
  @SerialName("members_can_create_repositories")
  public val membersCanCreateRepositories: Boolean? = null,
  @SerialName("two_factor_requirement_enabled")
  public val twoFactorRequirementEnabled: Boolean? = null,
  @SerialName("members_allowed_repository_creation_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersAllowedRepositoryCreationType: String? = null,
  @SerialName("members_can_create_public_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreatePublicRepositories: Boolean? = null,
  @SerialName("members_can_create_private_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreatePrivateRepositories: Boolean? = null,
  @SerialName("members_can_create_internal_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreateInternalRepositories: Boolean? = null,
  @SerialName("members_can_create_pages")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreatePages: Boolean? = null,
  @SerialName("members_can_create_public_pages")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreatePublicPages: Boolean? = null,
  @SerialName("members_can_create_private_pages")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreatePrivatePages: Boolean? = null,
  @SerialName("members_can_fork_private_repositories")
  public val membersCanForkPrivateRepositories: Boolean? = null,
  @SerialName("web_commit_signoff_required")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val webCommitSignoffRequired: Boolean? = null,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("archived_at")
  public val archivedAt: Instant?,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Plan(
    public val name: String,
    public val space: Long,
    @SerialName("private_repos")
    public val privateRepos: Long,
    @SerialName("filled_seats")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val filledSeats: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val seats: Long? = null,
  )
}
