package io.github.nomisrev.render.test.client.single.ref.alias

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class AnyOfColor(
  public val `value`: String,
) {
  @SerialName("red")
  Red("red"),
  @SerialName("green")
  Green("green"),
  ;
}
