package io.github.nomisrev.render.test.client.parameter.styles

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Status(
  public val `value`: String,
) {
  @SerialName("open")
  Open("open"),
  @SerialName("in-progress")
  InProgress("in-progress"),
  ;
}
