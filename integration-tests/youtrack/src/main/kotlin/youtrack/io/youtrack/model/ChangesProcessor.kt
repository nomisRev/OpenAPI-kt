package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * The basic entity that represents a VCS or a build server integration configured for a project.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface ChangesProcessor {
  public val id: String?

  public val project: IssueFolderRead.Project?

  public val relatedProjects: List<IssueFolderRead.Project>?

  public val enabled: Boolean?

  public val visibleForGroups: List<UserGroupRead>?

  public val addComments: Boolean?

  public val lookupIssuesInBranchName: Boolean?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    public val server: VcsServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
  ) : ChangesProcessor

  @SerialName("VcsHostingChangesProcessor")
  @Serializable
  public data class VcsHostingChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.VcsHostingServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GitHubChangesProcessor")
  @Serializable
  public data class GitHubChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.GitHubServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GogsChangesProcessor")
  @Serializable
  public data class GogsChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.GogsServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GiteaChangesProcessor")
  @Serializable
  public data class GiteaChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.GiteaServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("SpaceChangesProcessor")
  @Serializable
  public data class SpaceChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.SpaceServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GitLabChangesProcessor")
  @Serializable
  public data class GitLabChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.GitLabServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("BitBucketChangesProcessor")
  @Serializable
  public data class BitBucketChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.BitBucketServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("BitbucketStandaloneChangesProcessor")
  @Serializable
  public data class BitbucketStandaloneChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.BitbucketStandaloneServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
    public val path: String? = null,
    public val branchSpecification: String? = null,
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("TeamcityChangesProcessor")
  @Serializable
  public data class TeamcityChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.TeamcityServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
  ) : ChangesProcessor

  @SerialName("JenkinsChangesProcessor")
  @Serializable
  public data class JenkinsChangesProcessor(
    override val id: String? = null,
    public val server: VcsServer.JenkinsServer? = null,
    override val project: IssueFolderRead.Project? = null,
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    override val enabled: Boolean? = null,
    override val visibleForGroups: List<UserGroupRead>? = null,
    override val addComments: Boolean? = null,
    override val lookupIssuesInBranchName: Boolean? = null,
  ) : ChangesProcessor
}
