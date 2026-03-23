package io.youtrack.model

import kotlin.OptIn
import kotlin.String
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

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("JenkinsServer")
  @Serializable
  public data class JenkinsServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("VcsHostingServer")
  @Serializable
  public data class VcsHostingServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GogsServer")
  @Serializable
  public data class GogsServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GiteaServer")
  @Serializable
  public data class GiteaServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("SpaceServer")
  @Serializable
  public data class SpaceServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GitHubServer")
  @Serializable
  public data class GitHubServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("GitLabServer")
  @Serializable
  public data class GitLabServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("BitBucketServer")
  @Serializable
  public data class BitBucketServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("BitbucketStandaloneServer")
  @Serializable
  public data class BitbucketStandaloneServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer

  @SerialName("TeamcityServer")
  @Serializable
  public data class TeamcityServer(
    override val id: String? = null,
    override val url: String? = null,
  ) : VcsServer
}
