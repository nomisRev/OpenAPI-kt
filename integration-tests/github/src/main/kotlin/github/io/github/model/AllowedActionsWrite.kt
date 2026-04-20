package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AllowedActionsWrite(
  public val `value`: String,
) {
  @SerialName("all")
  All("all"),
  @SerialName("local_only")
  LocalOnly("local_only"),
  @SerialName("selected")
  Selected("selected"),
  ;
}
