package io.github.nomisrev.render.test.object_.neutral.leaf.no.split

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Mode(
  public val `value`: String,
) {
  @SerialName("enabled")
  Enabled("enabled"),
  @SerialName("disabled")
  Disabled("disabled"),
  ;
}
