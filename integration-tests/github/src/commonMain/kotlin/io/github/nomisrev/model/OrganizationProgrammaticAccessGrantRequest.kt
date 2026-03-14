package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationProgrammaticAccessGrantRequest(
    val id: Long,
    val reason: String?,
    val owner: SimpleUser,
    @SerialName("repository_selection") val repositorySelection: RepositorySelection,
    @SerialName("repositories_url") val repositoriesUrl: String,
    val permissions: Permissions,
    @SerialName("created_at") val createdAt: String,
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
