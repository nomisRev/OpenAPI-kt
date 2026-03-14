package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullableScopedInstallation(
    val permissions: AppPermissions,
    @SerialName("repository_selection") val repositorySelection: RepositorySelection,
    @SerialName("single_file_name") val singleFileName: String?,
    @SerialName("has_multiple_single_files") val hasMultipleSingleFiles: Boolean? = null,
    @SerialName("single_file_paths") val singleFilePaths: List<String>? = null,
    @SerialName("repositories_url") val repositoriesUrl: String,
    val account: SimpleUser,
) {
    @Serializable
    enum class RepositorySelection {
        @SerialName("all") All, @SerialName("selected") Selected;
    }
}
