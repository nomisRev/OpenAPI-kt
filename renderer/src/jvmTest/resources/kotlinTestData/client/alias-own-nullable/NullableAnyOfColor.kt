package io.github.nomisrev.render.test.client.alias.own.nullable

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class NullableAnyOfColor(
  public val `value`: String,
) {
  @SerialName("red")
  Red("red"),
  @SerialName("green")
  Green("green"),
  ;
}
