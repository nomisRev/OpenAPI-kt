package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RepositoryRuleEnforcementWrite(
  public val `value`: String,
) {
  @SerialName("disabled")
  Disabled("disabled"),
  @SerialName("active")
  Active("active"),
  @SerialName("evaluate")
  Evaluate("evaluate"),
  ;
}
