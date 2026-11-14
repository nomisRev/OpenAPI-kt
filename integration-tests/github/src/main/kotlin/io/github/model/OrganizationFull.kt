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
 * Organization Full
 */
@OptIn(ExperimentalSerializationApi::class)
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val twitterUsername: Optional<String> = Optional.Absent,
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
  public val type: String,
  @SerialName("total_private_repos")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val totalPrivateRepos: Long? = null,
  @SerialName("owned_private_repos")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ownedPrivateRepos: Long? = null,
  @SerialName("private_gists")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val privateGists: Optional<Long> = Optional.Absent,
  @SerialName("disk_usage")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val diskUsage: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val collaborators: Optional<Long> = Optional.Absent,
  @SerialName("billing_email")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val billingEmail: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val plan: Plan? = null,
  @SerialName("default_repository_permission")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val defaultRepositoryPermission: Optional<String> = Optional.Absent,
  @SerialName("default_repository_branch")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val defaultRepositoryBranch: Optional<String> = Optional.Absent,
  @SerialName("members_can_create_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreateRepositories: Optional<Boolean> = Optional.Absent,
  @SerialName("two_factor_requirement_enabled")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val twoFactorRequirementEnabled: Optional<Boolean> = Optional.Absent,
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
  @SerialName("members_can_delete_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanDeleteRepositories: Boolean? = null,
  @SerialName("members_can_change_repo_visibility")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanChangeRepoVisibility: Boolean? = null,
  @SerialName("members_can_invite_outside_collaborators")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanInviteOutsideCollaborators: Boolean? = null,
  @SerialName("members_can_delete_issues")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanDeleteIssues: Boolean? = null,
  @SerialName("display_commenter_full_name_setting_enabled")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val displayCommenterFullNameSettingEnabled: Boolean? = null,
  @SerialName("readers_can_create_discussions")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val readersCanCreateDiscussions: Boolean? = null,
  @SerialName("members_can_create_teams")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanCreateTeams: Boolean? = null,
  @SerialName("members_can_view_dependency_insights")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanViewDependencyInsights: Boolean? = null,
  @SerialName("members_can_fork_private_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val membersCanForkPrivateRepositories: Optional<Boolean> = Optional.Absent,
  @SerialName("web_commit_signoff_required")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val webCommitSignoffRequired: Boolean? = null,
  @SerialName("advanced_security_enabled_for_new_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val advancedSecurityEnabledForNewRepositories: Boolean? = null,
  @SerialName("dependabot_alerts_enabled_for_new_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabotAlertsEnabledForNewRepositories: Boolean? = null,
  @SerialName("dependabot_security_updates_enabled_for_new_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependabotSecurityUpdatesEnabledForNewRepositories: Boolean? = null,
  @SerialName("dependency_graph_enabled_for_new_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dependencyGraphEnabledForNewRepositories: Boolean? = null,
  @SerialName("secret_scanning_enabled_for_new_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningEnabledForNewRepositories: Boolean? = null,
  @SerialName("secret_scanning_push_protection_enabled_for_new_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningPushProtectionEnabledForNewRepositories: Boolean? = null,
  @SerialName("secret_scanning_push_protection_custom_link_enabled")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningPushProtectionCustomLinkEnabled: Boolean? = null,
  @SerialName("secret_scanning_push_protection_custom_link")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secretScanningPushProtectionCustomLink: Optional<String> = Optional.Absent,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("archived_at")
  public val archivedAt: Instant?,
  @SerialName("deploy_keys_enabled_for_repositories")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val deployKeysEnabledForRepositories: Boolean? = null,
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
