package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ReviewCustomGatesStateRequired(
  @SerialName("environment_name")
  public val environmentName: String,
  public val state: State,
  public val comment: String? = null,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("approved")
    Approved("approved"),
    @SerialName("rejected")
    Rejected("rejected"),
    ;
  }
}
