package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes an OpenAI model offering that can be used with the API.
 */
@Serializable
public data class Model(
  public val id: String,
  public val created: Long,
  public val `object`: Object,
  @SerialName("owned_by")
  public val ownedBy: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("model")
    Model("model"),
    ;
  }
}
