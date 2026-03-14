package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationProgrammaticAccessGrant(
    val id: Long,
    val owner: SimpleUser,
    @SerialName("repository_selection") val repositorySelection: RepositorySelection,
    @SerialName("repositories_url") val repositoriesUrl: String,
    val permissions: Permissions,
    @SerialName("access_granted_at") val accessGrantedAt: String,
    @SerialName("token_id") val tokenId: Long,
    @SerialName("token_name") val tokenName: String,
    @SerialName("token_expired") val tokenExpired: Boolean,
    @SerialName("token_expires_at") val tokenExpiresAt: String?,
    @SerialName("token_last_used_at") val tokenLastUsedAt: String?,
) {
    @Serializable
    enum class RepositorySelection {
        @SerialName("none") None, @SerialName("all") All, @SerialName("subset") Subset;
    }

    @Serializable
    data class Permissions(
        val organization: List<String>? = null,
        val repository: List<String>? = null,
        val other: List<String>? = null,
    )
}
