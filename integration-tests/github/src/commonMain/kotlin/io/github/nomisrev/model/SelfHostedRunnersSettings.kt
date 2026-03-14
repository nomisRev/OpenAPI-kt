package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SelfHostedRunnersSettings(
    @SerialName("enabled_repositories") val enabledRepositories: EnabledRepositories,
    @SerialName("selected_repositories_url") val selectedRepositoriesUrl: String? = null,
) {
    @Serializable
    enum class EnabledRepositories {
        @SerialName("all") All, @SerialName("selected") Selected, @SerialName("none") None;
    }
}
