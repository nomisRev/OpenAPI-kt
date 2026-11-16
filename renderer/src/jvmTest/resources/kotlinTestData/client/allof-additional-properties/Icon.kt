package io.github.nomisrev.render.test.client.allof.additional.properties

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Icon(
  public val `value`: String,
) {
  @SerialName("icon://appstore")
  IconAppstore("icon://appstore"),
  @SerialName("icon://bitcoin")
  IconBitcoin("icon://bitcoin"),
  ;
}
