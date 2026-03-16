package io.github.nomisrev.render.test.`enum`.nullable

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class NullableState {
  @SerialName("on")
  On,
  @SerialName("null")
  Null,
  @SerialName("off")
  Off,
}
