package io.github.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Authentication token for a GitHub App installed on a user or org.
 */
@Serializable
public data class InstallationToken(
  public val token: String,
  @SerialName("expires_at")
  public val expiresAt: String,
  public val permissions: AppPermissions? = null,
  @SerialName("repository_selection")
  public val repositorySelection: RepositorySelection? = null,
  public val repositories: List<Repository>? = null,
  @SerialName("single_file")
  public val singleFile: String? = null,
  @SerialName("has_multiple_single_files")
  public val hasMultipleSingleFiles: Boolean? = null,
  @SerialName("single_file_paths")
  public val singleFilePaths: List<String>? = null,
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
