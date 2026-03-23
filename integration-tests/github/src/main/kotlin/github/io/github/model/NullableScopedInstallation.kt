package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class NullableScopedInstallation(
  public val permissions: AppPermissions,
  @SerialName("repository_selection")
  public val repositorySelection: RepositorySelection,
  @SerialName("single_file_name")
  public val singleFileName: String?,
  @SerialName("has_multiple_single_files")
  public val hasMultipleSingleFiles: Boolean? = null,
  @SerialName("single_file_paths")
  public val singleFilePaths: List<String>? = null,
  @SerialName("repositories_url")
  public val repositoriesUrl: String,
  public val account: SimpleUser,
) {
  @Serializable
  public enum class RepositorySelection(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("selected")
    Selected("selected"),
    ;
  }
}
