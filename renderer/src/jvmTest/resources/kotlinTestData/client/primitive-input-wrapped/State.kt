package io.github.nomisrev.render.test.client.primitive.input.wrapped

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class State(
  public val `value`: String,
) {
  @SerialName("open")
  Open("open"),
  @SerialName("closed")
  Closed("closed"),
  ;
}
