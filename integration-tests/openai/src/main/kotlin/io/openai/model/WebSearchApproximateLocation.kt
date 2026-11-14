package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The approximate location of the user.
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class WebSearchApproximateLocation(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
