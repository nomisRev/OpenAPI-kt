package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The approximate location of the user.
 *
 */
@Serializable
public data class WebSearchApproximateLocation(
  public val type: Type? = null,
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
