package io.github.nomisrev.render.test.`enum`.singlevalue

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Version(
  public val `value`: String,
) {
  @SerialName("v1")
  V1("v1"),
  ;
}
