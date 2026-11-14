package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * The basic entity that represents a VCS or a build server.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface VcsServer {
  public val id: String?

  public val url: String?

  @SerialName("VcsServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("JenkinsServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class JenkinsServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("VcsHostingServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VcsHostingServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GogsServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GogsServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GiteaServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GiteaServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("SpaceServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SpaceServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GitHubServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GitHubServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GitLabServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GitLabServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("BitBucketServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BitBucketServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("BitbucketStandaloneServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BitbucketStandaloneServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer

  @SerialName("TeamcityServer")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TeamcityServer(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val url: String? = null,
  ) : VcsServer
}
