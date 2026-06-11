package io.github.nomisrev.render.test.`enum`.nullable

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class NullableState(
  public val `value`: String,
) {
  @SerialName("on")
  On("on"),
  @SerialName("null")
  Null("null"),
  @SerialName("off")
  Off("off"),
  ;
}
