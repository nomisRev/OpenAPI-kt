package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface VcsServerResponse {
    val id: String?
    val url: String?

    @SerialName("Default")
    @Serializable
    data class Default(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("JenkinsServer")
    @Serializable
    data class JenkinsServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("VcsHostingServer")
    @Serializable
    data class VcsHostingServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("GogsServer")
    @Serializable
    data class GogsServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("GiteaServer")
    @Serializable
    data class GiteaServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("SpaceServer")
    @Serializable
    data class SpaceServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("GitHubServer")
    @Serializable
    data class GitHubServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("GitLabServer")
    @Serializable
    data class GitLabServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("BitBucketServer")
    @Serializable
    data class BitBucketServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse

    @SerialName("BitbucketStandaloneServer")
    @Serializable
    data class BitbucketStandaloneServer(
        override val id: String? = null,
        override val url: String? = null,
    ) : VcsServerResponse

    @SerialName("TeamcityServer")
    @Serializable
    data class TeamcityServer(override val id: String? = null, override val url: String? = null) : VcsServerResponse
}
