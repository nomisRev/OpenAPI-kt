package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ImmutableReleasesOrganizationSettings(
    @SerialName("enforced_repositories") val enforcedRepositories: EnforcedRepositories,
    @SerialName("selected_repositories_url") val selectedRepositoriesUrl: String? = null,
) {
    @Serializable
    enum class EnforcedRepositories {
        @SerialName("all") All, @SerialName("none") None, @SerialName("selected") Selected;
    }
}
