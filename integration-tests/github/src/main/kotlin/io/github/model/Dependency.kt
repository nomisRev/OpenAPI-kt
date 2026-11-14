package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Dependency(
  @SerialName("package_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val packageUrl: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val metadata: Metadata? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val relationship: Relationship? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val scope: Scope? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
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
