package io.github.nomisrev.render.test.`enum`.singlevalue

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Version {
  @SerialName("v1")
  V1,
}
