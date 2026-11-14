package io.github.nomisrev.render.test.`enum`.entry.names

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Ordering(
  public val `value`: String,
) {
  @SerialName("last_activity")
  LastActivity("last_activity"),
  @SerialName("-last_activity")
  MinusLastActivity("-last_activity"),
  ;
}
