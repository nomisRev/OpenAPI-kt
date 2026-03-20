package io.github.nomisrev.render.test.`enum`.special.chars

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SpecialValues(
  public val `value`: String,
) {
  @SerialName("*")
  Star("*"),
  @SerialName("/")
  Slash("/"),
  `+1`("+1"),
  `-1`("-1"),
  ;
}
