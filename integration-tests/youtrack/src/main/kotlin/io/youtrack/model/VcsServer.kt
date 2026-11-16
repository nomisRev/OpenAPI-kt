package io.youtrack.model

import kotlin.OptIn
import kotlin.String
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
 * The basic entity that represents a VCS or a build server.
 */
@Serializable(with = VcsServer.Serializer::class)
public sealed interface VcsServer {
  public val id: String?

  public val url: String?

  @SerialName("VcsServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "VcsServer")
  }

  @SerialName("JenkinsServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = JenkinsServer.Serializer::class)
  public data class JenkinsServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<JenkinsServer>(JenkinsServer.generatedSerializer(), "${'$'}type", "JenkinsServer")
  }

  @SerialName("VcsHostingServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VcsHostingServer.Serializer::class)
  public data class VcsHostingServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<VcsHostingServer>(VcsHostingServer.generatedSerializer(), "${'$'}type", "VcsHostingServer")
  }

  @SerialName("GogsServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GogsServer.Serializer::class)
  public data class GogsServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<GogsServer>(GogsServer.generatedSerializer(), "${'$'}type", "GogsServer")
  }

  @SerialName("GiteaServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GiteaServer.Serializer::class)
  public data class GiteaServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<GiteaServer>(GiteaServer.generatedSerializer(), "${'$'}type", "GiteaServer")
  }

  @SerialName("SpaceServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SpaceServer.Serializer::class)
  public data class SpaceServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<SpaceServer>(SpaceServer.generatedSerializer(), "${'$'}type", "SpaceServer")
  }

  @SerialName("GitHubServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GitHubServer.Serializer::class)
  public data class GitHubServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<GitHubServer>(GitHubServer.generatedSerializer(), "${'$'}type", "GitHubServer")
  }

  @SerialName("GitLabServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GitLabServer.Serializer::class)
  public data class GitLabServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<GitLabServer>(GitLabServer.generatedSerializer(), "${'$'}type", "GitLabServer")
  }

  @SerialName("BitBucketServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BitBucketServer.Serializer::class)
  public data class BitBucketServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<BitBucketServer>(BitBucketServer.generatedSerializer(), "${'$'}type", "BitBucketServer")
  }

  @SerialName("BitbucketStandaloneServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BitbucketStandaloneServer.Serializer::class)
  public data class BitbucketStandaloneServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<BitbucketStandaloneServer>(BitbucketStandaloneServer.generatedSerializer(), "${'$'}type", "BitbucketStandaloneServer")
  }

  @SerialName("TeamcityServer")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TeamcityServer.Serializer::class)
  public data class TeamcityServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer {
    public object Serializer : TaggedCaseSerializer<TeamcityServer>(TeamcityServer.generatedSerializer(), "${'$'}type", "TeamcityServer")
  }

  public object Serializer : JsonContentPolymorphicSerializer<VcsServer>(VcsServer::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<VcsServer> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "VcsServer" -> Default.serializer()
      "JenkinsServer" -> JenkinsServer.serializer()
      "VcsHostingServer" -> VcsHostingServer.serializer()
      "GogsServer" -> GogsServer.serializer()
      "GiteaServer" -> GiteaServer.serializer()
      "SpaceServer" -> SpaceServer.serializer()
      "GitHubServer" -> GitHubServer.serializer()
      "GitLabServer" -> GitLabServer.serializer()
      "BitBucketServer" -> BitBucketServer.serializer()
      "BitbucketStandaloneServer" -> BitbucketStandaloneServer.serializer()
      "TeamcityServer" -> TeamcityServer.serializer()
      else -> throw SerializationException("'${'$'}type' of VcsServer selects none of VcsServer, JenkinsServer, VcsHostingServer, GogsServer, GiteaServer, SpaceServer, GitHubServer, GitLabServer, BitBucketServer, BitbucketStandaloneServer, TeamcityServer: " + tag)
    }
  }
}
