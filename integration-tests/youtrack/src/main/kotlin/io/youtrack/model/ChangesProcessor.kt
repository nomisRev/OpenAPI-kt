package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * The basic entity that represents a VCS or a build server integration configured for a project.
 */
@Serializable(with = ChangesProcessor.Serializer::class)
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
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "ChangesProcessor")
  }

  @SerialName("VcsHostingChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VcsHostingChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<VcsHostingChangesProcessor>(VcsHostingChangesProcessor.generatedSerializer(), "${'$'}type", "VcsHostingChangesProcessor")
  }

  @SerialName("GitHubChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GitHubChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<GitHubChangesProcessor>(GitHubChangesProcessor.generatedSerializer(), "${'$'}type", "GitHubChangesProcessor")
  }

  @SerialName("GogsChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GogsChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<GogsChangesProcessor>(GogsChangesProcessor.generatedSerializer(), "${'$'}type", "GogsChangesProcessor")
  }

  @SerialName("GiteaChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GiteaChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<GiteaChangesProcessor>(GiteaChangesProcessor.generatedSerializer(), "${'$'}type", "GiteaChangesProcessor")
  }

  @SerialName("SpaceChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SpaceChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<SpaceChangesProcessor>(SpaceChangesProcessor.generatedSerializer(), "${'$'}type", "SpaceChangesProcessor")
  }

  @SerialName("GitLabChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GitLabChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<GitLabChangesProcessor>(GitLabChangesProcessor.generatedSerializer(), "${'$'}type", "GitLabChangesProcessor")
  }

  @SerialName("BitBucketChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BitBucketChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<BitBucketChangesProcessor>(BitBucketChangesProcessor.generatedSerializer(), "${'$'}type", "BitBucketChangesProcessor")
  }

  @SerialName("BitbucketStandaloneChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BitbucketStandaloneChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<BitbucketStandaloneChangesProcessor>(BitbucketStandaloneChangesProcessor.generatedSerializer(), "${'$'}type", "BitbucketStandaloneChangesProcessor")
  }

  @SerialName("TeamcityChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TeamcityChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<TeamcityChangesProcessor>(TeamcityChangesProcessor.generatedSerializer(), "${'$'}type", "TeamcityChangesProcessor")
  }

  @SerialName("JenkinsChangesProcessor")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = JenkinsChangesProcessor.Serializer::class)
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
  ) : ChangesProcessor {
    public object Serializer : TaggedCaseSerializer<JenkinsChangesProcessor>(JenkinsChangesProcessor.generatedSerializer(), "${'$'}type", "JenkinsChangesProcessor")
  }

  public object Serializer : JsonContentPolymorphicSerializer<ChangesProcessor>(ChangesProcessor::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ChangesProcessor> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "ChangesProcessor" -> Default.serializer()
      "VcsHostingChangesProcessor" -> VcsHostingChangesProcessor.serializer()
      "GitHubChangesProcessor" -> GitHubChangesProcessor.serializer()
      "GogsChangesProcessor" -> GogsChangesProcessor.serializer()
      "GiteaChangesProcessor" -> GiteaChangesProcessor.serializer()
      "SpaceChangesProcessor" -> SpaceChangesProcessor.serializer()
      "GitLabChangesProcessor" -> GitLabChangesProcessor.serializer()
      "BitBucketChangesProcessor" -> BitBucketChangesProcessor.serializer()
      "BitbucketStandaloneChangesProcessor" -> BitbucketStandaloneChangesProcessor.serializer()
      "TeamcityChangesProcessor" -> TeamcityChangesProcessor.serializer()
      "JenkinsChangesProcessor" -> JenkinsChangesProcessor.serializer()
      else -> throw SerializationException("'${'$'}type' of ChangesProcessor selects none of ChangesProcessor, VcsHostingChangesProcessor, GitHubChangesProcessor, GogsChangesProcessor, GiteaChangesProcessor, SpaceChangesProcessor, GitLabChangesProcessor, BitBucketChangesProcessor, BitbucketStandaloneChangesProcessor, TeamcityChangesProcessor, JenkinsChangesProcessor: " + tag)
    }
  }
}
