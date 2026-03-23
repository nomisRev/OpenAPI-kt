package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SelfHostedRunnersSettings(
  @SerialName("enabled_repositories")
  public val enabledRepositories: EnabledRepositories,
  @SerialName("selected_repositories_url")
  public val selectedRepositoriesUrl: String? = null,
) {
  @Serializable
  public enum class EnabledRepositories(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("selected")
    Selected("selected"),
    @SerialName("none")
    None("none"),
    ;
  }
}
