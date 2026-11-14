package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Check immutable releases settings for an organization.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ImmutableReleasesOrganizationSettings(
  @SerialName("enforced_repositories")
  public val enforcedRepositories: EnforcedRepositories,
  @SerialName("selected_repositories_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val selectedRepositoriesUrl: String? = null,
) {
  @Serializable
  public enum class EnforcedRepositories(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("none")
    None("none"),
    @SerialName("selected")
    Selected("selected"),
    ;
  }
}
