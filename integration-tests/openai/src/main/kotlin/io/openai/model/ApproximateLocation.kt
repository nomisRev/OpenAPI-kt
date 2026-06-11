package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ApproximateLocation(
  @Required
  public val type: Type = Type.Approximate,
  public val country: String? = null,
  public val region: String? = null,
  public val city: String? = null,
  public val timezone: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("approximate")
    Approximate("approximate"),
    ;
  }
}
