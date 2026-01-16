package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface ChangesProcessorResponse {
    val id: String?
    val server: VcsServerResponse?
    val project: Project?
    val relatedProjects: List<Project>?
    val enabled: Boolean?
    val visibleForGroups: List<UserGroupResponse>?
    val addComments: Boolean?
    val lookupIssuesInBranchName: Boolean?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val server: VcsServerResponse? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
    ) : ChangesProcessorResponse

    @SerialName("VcsHostingChangesProcessor")
    @Serializable
    data class VcsHostingChangesProcessor(
        override val id: String? = null,
        override val server: VcsServerResponse? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("GitHubChangesProcessor")
    @Serializable
    data class GitHubChangesProcessor(
        override val id: String? = null,
        override val server: GitHubServer? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("GogsChangesProcessor")
    @Serializable
    data class GogsChangesProcessor(
        override val id: String? = null,
        override val server: GogsServer? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("GiteaChangesProcessor")
    @Serializable
    data class GiteaChangesProcessor(
        override val id: String? = null,
        override val server: GiteaServer? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("SpaceChangesProcessor")
    @Serializable
    data class SpaceChangesProcessor(
        override val id: String? = null,
        override val server: SpaceServer? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("GitLabChangesProcessor")
    @Serializable
    data class GitLabChangesProcessor(
        override val id: String? = null,
        override val server: GitLabServer? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("BitBucketChangesProcessor")
    @Serializable
    data class BitBucketChangesProcessor(
        override val id: String? = null,
        override val server: BitBucketServer? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("BitbucketStandaloneChangesProcessor")
    @Serializable
    data class BitbucketStandaloneChangesProcessor(
        override val id: String? = null,
        override val server: BitbucketStandaloneServer? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
        val path: String? = null,
        val branchSpecification: String? = null,
        val committers: UserGroupResponse? = null,
    ) : ChangesProcessorResponse

    @SerialName("TeamcityChangesProcessor")
    @Serializable
    data class TeamcityChangesProcessor(
        override val id: String? = null,
        override val server: VcsServerResponse? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
    ) : ChangesProcessorResponse

    @SerialName("JenkinsChangesProcessor")
    @Serializable
    data class JenkinsChangesProcessor(
        override val id: String? = null,
        override val server: VcsServerResponse? = null,
        override val project: Project? = null,
        override val relatedProjects: List<Project>? = null,
        override val enabled: Boolean? = null,
        override val visibleForGroups: List<UserGroupResponse>? = null,
        override val addComments: Boolean? = null,
        override val lookupIssuesInBranchName: Boolean? = null,
    ) : ChangesProcessorResponse
}
