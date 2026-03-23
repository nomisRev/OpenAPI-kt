package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SecurityAdvisoryEcosystems(
  public val `value`: String,
) {
  @SerialName("rubygems")
  Rubygems("rubygems"),
  @SerialName("npm")
  Npm("npm"),
  @SerialName("pip")
  Pip("pip"),
  @SerialName("maven")
  Maven("maven"),
  @SerialName("nuget")
  Nuget("nuget"),
  @SerialName("composer")
  Composer("composer"),
  @SerialName("go")
  Go("go"),
  @SerialName("rust")
  Rust("rust"),
  @SerialName("erlang")
  Erlang("erlang"),
  @SerialName("actions")
  Actions("actions"),
  @SerialName("pub")
  Pub("pub"),
  @SerialName("other")
  Other("other"),
  @SerialName("swift")
  Swift("swift"),
  ;
}
