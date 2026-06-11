package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Check immutable releases settings for an organization.
 */
@Serializable
public data class ImmutableReleasesOrganizationSettings(
  @SerialName("enforced_repositories")
  public val enforcedRepositories: EnforcedRepositories,
  @SerialName("selected_repositories_url")
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
