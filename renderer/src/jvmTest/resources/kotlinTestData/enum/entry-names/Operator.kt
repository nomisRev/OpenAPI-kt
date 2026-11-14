package io.github.nomisrev.render.test.`enum`.entry.names

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Operator(
  public val `value`: String,
) {
  @SerialName("eq")
  Eq("eq"),
  @SerialName("<")
  LessThan("<"),
  @SerialName("<=")
  LessThanOrEqual("<="),
  @SerialName(">")
  GreaterThan(">"),
  @SerialName(">=")
  GreaterThanOrEqual(">="),
  ;
}
