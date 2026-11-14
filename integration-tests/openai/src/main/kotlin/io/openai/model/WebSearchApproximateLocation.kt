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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val country: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val region: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val city: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val timezone: Optional<String> = Optional.Absent,
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
