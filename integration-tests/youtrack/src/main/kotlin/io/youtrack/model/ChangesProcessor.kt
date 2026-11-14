package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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

  @SerialName("ChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
  ) : ChangesProcessor

  @SerialName("VcsHostingChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VcsHostingChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.VcsHostingServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GitHubChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GitHubChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.GitHubServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GogsChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GogsChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.GogsServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GiteaChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GiteaChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.GiteaServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("SpaceChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SpaceChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.SpaceServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("GitLabChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GitLabChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.GitLabServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("BitBucketChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BitBucketChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.BitBucketServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("BitbucketStandaloneChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BitbucketStandaloneChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.BitbucketStandaloneServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val branchSpecification: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val committers: UserGroupRead? = null,
  ) : ChangesProcessor

  @SerialName("TeamcityChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TeamcityChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.TeamcityServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
  ) : ChangesProcessor

  @SerialName("JenkinsChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class JenkinsChangesProcessor(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val server: VcsServer.JenkinsServer? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val relatedProjects: List<IssueFolderRead.Project>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val enabled: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val visibleForGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val addComments: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val lookupIssuesInBranchName: Boolean? = null,
  ) : ChangesProcessor
}
