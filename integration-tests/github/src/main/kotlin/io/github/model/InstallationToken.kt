package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Authentication token for a GitHub App installed on a user or org.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class InstallationToken(
  public val token: String,
  @SerialName("expires_at")
  public val expiresAt: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val permissions: AppPermissions? = null,
  @SerialName("repository_selection")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositorySelection: RepositorySelection? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val repositories: List<Repository>? = null,
  @SerialName("single_file")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val singleFile: String? = null,
  @SerialName("has_multiple_single_files")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val hasMultipleSingleFiles: Boolean? = null,
  @SerialName("single_file_paths")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
