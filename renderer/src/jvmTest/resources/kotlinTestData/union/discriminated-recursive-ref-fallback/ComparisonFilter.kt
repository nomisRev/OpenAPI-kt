package io.github.nomisrev.render.test.union.discriminated.recursive.ref.fallback

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ComparisonFilter(
  public val type: Type,
  public val key: String,
  public val `value`: String,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("eq")
    Eq("eq"),
    @SerialName("ne")
    Ne("ne"),
    ;
  }
}
