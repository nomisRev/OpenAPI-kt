package io.github.nomisrev.render.test.`enum`.special.chars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SpecialValues {
  @SerialName("*")
  Star,
  @SerialName("/")
  Slash,
  `+1`,
  `-1`,
}
