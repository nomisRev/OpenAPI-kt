package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class InstallationToken(
    val token: String,
    @SerialName("expires_at") val expiresAt: String,
    val permissions: AppPermissions? = null,
    @SerialName("repository_selection") val repositorySelection: RepositorySelection? = null,
    val repositories: List<Repository>? = null,
    @SerialName("single_file") val singleFile: String? = null,
    @SerialName("has_multiple_single_files") val hasMultipleSingleFiles: Boolean? = null,
    @SerialName("single_file_paths") val singleFilePaths: List<String>? = null,
) {
    @Serializable
    enum class RepositorySelection {
        @SerialName("all") All, @SerialName("selected") Selected;
    }
}
