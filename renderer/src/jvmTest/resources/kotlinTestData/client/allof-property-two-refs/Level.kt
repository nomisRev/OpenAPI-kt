package io.github.nomisrev.render.test.client.allof.`property`.two.refs

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Level(
  public val `value`: String,
) {
  @SerialName("low")
  Low("low"),
  @SerialName("high")
  High("high"),
  ;
}
