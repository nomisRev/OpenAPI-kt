package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TeamOrganization(
    val login: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    @SerialName("repos_url") val reposUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("hooks_url") val hooksUrl: String,
    @SerialName("issues_url") val issuesUrl: String,
    @SerialName("members_url") val membersUrl: String,
    @SerialName("public_members_url") val publicMembersUrl: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val description: String?,
    val name: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    @SerialName("twitter_username") val twitterUsername: String? = null,
    @SerialName("is_verified") val isVerified: Boolean? = null,
    @SerialName("has_organization_projects") val hasOrganizationProjects: Boolean,
    @SerialName("has_repository_projects") val hasRepositoryProjects: Boolean,
    @SerialName("public_repos") val publicRepos: Long,
    @SerialName("public_gists") val publicGists: Long,
    val followers: Long,
    val following: Long,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    val type: String,
    @SerialName("total_private_repos") val totalPrivateRepos: Long? = null,
    @SerialName("owned_private_repos") val ownedPrivateRepos: Long? = null,
    @SerialName("private_gists") val privateGists: Long? = null,
    @SerialName("disk_usage") val diskUsage: Long? = null,
    val collaborators: Long? = null,
    @SerialName("billing_email") val billingEmail: String? = null,
    val plan: Plan? = null,
    @SerialName("default_repository_permission") val defaultRepositoryPermission: String? = null,
    @SerialName("members_can_create_repositories") val membersCanCreateRepositories: Boolean? = null,
    @SerialName("two_factor_requirement_enabled") val twoFactorRequirementEnabled: Boolean? = null,
    @SerialName("members_allowed_repository_creation_type") val membersAllowedRepositoryCreationType: String? = null,
    @SerialName("members_can_create_public_repositories") val membersCanCreatePublicRepositories: Boolean? = null,
    @SerialName("members_can_create_private_repositories") val membersCanCreatePrivateRepositories: Boolean? = null,
    @SerialName("members_can_create_internal_repositories") val membersCanCreateInternalRepositories: Boolean? = null,
    @SerialName("members_can_create_pages") val membersCanCreatePages: Boolean? = null,
    @SerialName("members_can_create_public_pages") val membersCanCreatePublicPages: Boolean? = null,
    @SerialName("members_can_create_private_pages") val membersCanCreatePrivatePages: Boolean? = null,
    @SerialName("members_can_fork_private_repositories") val membersCanForkPrivateRepositories: Boolean? = null,
    @SerialName("web_commit_signoff_required") val webCommitSignoffRequired: Boolean? = null,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("archived_at") val archivedAt: LocalDateTime?,
) {
    @Serializable
    data class Plan(
        val name: String,
        val space: Long,
        @SerialName("private_repos") val privateRepos: Long,
        @SerialName("filled_seats") val filledSeats: Long? = null,
        val seats: Long? = null,
    )
}
