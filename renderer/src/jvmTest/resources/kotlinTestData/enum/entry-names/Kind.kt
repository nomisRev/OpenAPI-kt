package io.github.nomisrev.render.test.`enum`.entry.names

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Kind(
  public val `value`: String,
) {
  @SerialName("")
  Empty(""),
  @SerialName("spiders")
  Spiders("spiders"),
  ;
}
