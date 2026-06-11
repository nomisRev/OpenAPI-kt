package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Dependency(
  @SerialName("package_url")
  public val packageUrl: String? = null,
  public val metadata: Metadata? = null,
  public val relationship: Relationship? = null,
  public val scope: Scope? = null,
  public val dependencies: List<String>? = null,
) {
  @Serializable
  public enum class Relationship(
    public val `value`: String,
  ) {
    @SerialName("direct")
    Direct("direct"),
    @SerialName("indirect")
    Indirect("indirect"),
    ;
  }

  @Serializable
  public enum class Scope(
    public val `value`: String,
  ) {
    @SerialName("runtime")
    Runtime("runtime"),
    @SerialName("development")
    Development("development"),
    ;
  }
}
