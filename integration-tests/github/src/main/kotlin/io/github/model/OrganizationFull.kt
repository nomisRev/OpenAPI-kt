package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Organization Full
 */
@Serializable
public data class OrganizationFull(
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
  public val name: String? = null,
  public val company: String? = null,
  public val blog: String? = null,
  public val location: String? = null,
  public val email: String? = null,
  @SerialName("twitter_username")
  public val twitterUsername: String? = null,
  @SerialName("is_verified")
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
  public val type: String,
  @SerialName("total_private_repos")
  public val totalPrivateRepos: Long? = null,
  @SerialName("owned_private_repos")
  public val ownedPrivateRepos: Long? = null,
  @SerialName("private_gists")
  public val privateGists: Long? = null,
  @SerialName("disk_usage")
  public val diskUsage: Long? = null,
  public val collaborators: Long? = null,
  @SerialName("billing_email")
  public val billingEmail: String? = null,
  public val plan: Plan? = null,
  @SerialName("default_repository_permission")
  public val defaultRepositoryPermission: String? = null,
  @SerialName("default_repository_branch")
  public val defaultRepositoryBranch: String? = null,
  @SerialName("members_can_create_repositories")
  public val membersCanCreateRepositories: Boolean? = null,
  @SerialName("two_factor_requirement_enabled")
  public val twoFactorRequirementEnabled: Boolean? = null,
  @SerialName("members_allowed_repository_creation_type")
  public val membersAllowedRepositoryCreationType: String? = null,
  @SerialName("members_can_create_public_repositories")
  public val membersCanCreatePublicRepositories: Boolean? = null,
  @SerialName("members_can_create_private_repositories")
  public val membersCanCreatePrivateRepositories: Boolean? = null,
  @SerialName("members_can_create_internal_repositories")
  public val membersCanCreateInternalRepositories: Boolean? = null,
  @SerialName("members_can_create_pages")
  public val membersCanCreatePages: Boolean? = null,
  @SerialName("members_can_create_public_pages")
  public val membersCanCreatePublicPages: Boolean? = null,
  @SerialName("members_can_create_private_pages")
  public val membersCanCreatePrivatePages: Boolean? = null,
  @SerialName("members_can_delete_repositories")
  public val membersCanDeleteRepositories: Boolean? = null,
  @SerialName("members_can_change_repo_visibility")
  public val membersCanChangeRepoVisibility: Boolean? = null,
  @SerialName("members_can_invite_outside_collaborators")
  public val membersCanInviteOutsideCollaborators: Boolean? = null,
  @SerialName("members_can_delete_issues")
  public val membersCanDeleteIssues: Boolean? = null,
  @SerialName("display_commenter_full_name_setting_enabled")
  public val displayCommenterFullNameSettingEnabled: Boolean? = null,
  @SerialName("readers_can_create_discussions")
  public val readersCanCreateDiscussions: Boolean? = null,
  @SerialName("members_can_create_teams")
  public val membersCanCreateTeams: Boolean? = null,
  @SerialName("members_can_view_dependency_insights")
  public val membersCanViewDependencyInsights: Boolean? = null,
  @SerialName("members_can_fork_private_repositories")
  public val membersCanForkPrivateRepositories: Boolean? = null,
  @SerialName("web_commit_signoff_required")
  public val webCommitSignoffRequired: Boolean? = null,
  @SerialName("advanced_security_enabled_for_new_repositories")
  public val advancedSecurityEnabledForNewRepositories: Boolean? = null,
  @SerialName("dependabot_alerts_enabled_for_new_repositories")
  public val dependabotAlertsEnabledForNewRepositories: Boolean? = null,
  @SerialName("dependabot_security_updates_enabled_for_new_repositories")
  public val dependabotSecurityUpdatesEnabledForNewRepositories: Boolean? = null,
  @SerialName("dependency_graph_enabled_for_new_repositories")
  public val dependencyGraphEnabledForNewRepositories: Boolean? = null,
  @SerialName("secret_scanning_enabled_for_new_repositories")
  public val secretScanningEnabledForNewRepositories: Boolean? = null,
  @SerialName("secret_scanning_push_protection_enabled_for_new_repositories")
  public val secretScanningPushProtectionEnabledForNewRepositories: Boolean? = null,
  @SerialName("secret_scanning_push_protection_custom_link_enabled")
  public val secretScanningPushProtectionCustomLinkEnabled: Boolean? = null,
  @SerialName("secret_scanning_push_protection_custom_link")
  public val secretScanningPushProtectionCustomLink: String? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("archived_at")
  public val archivedAt: Instant?,
  @SerialName("deploy_keys_enabled_for_repositories")
  public val deployKeysEnabledForRepositories: Boolean? = null,
) {
  @Serializable
  public data class Plan(
    public val name: String,
    public val space: Long,
    @SerialName("private_repos")
    public val privateRepos: Long,
    @SerialName("filled_seats")
    public val filledSeats: Long? = null,
    public val seats: Long? = null,
  )
}
