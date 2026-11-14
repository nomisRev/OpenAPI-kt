package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ReviewCustomGatesStateRequired(
  @SerialName("environment_name")
  public val environmentName: String,
  public val state: State,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
