package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SelfHostedRunnersSettings(
  @SerialName("enabled_repositories")
  public val enabledRepositories: EnabledRepositories,
  @SerialName("selected_repositories_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
