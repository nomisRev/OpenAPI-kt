package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class EnabledRepositories(
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
