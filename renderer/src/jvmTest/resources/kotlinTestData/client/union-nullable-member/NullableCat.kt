package io.github.nomisrev.render.test.client.union.nullable.member

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class NullableCat(
  public val type: Type,
  public val meow: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("cat")
    Cat("cat"),
    ;
  }
}
