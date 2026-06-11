package io.github.nomisrev.render.test.object_.shared.`enum`.no.read.write.split

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
  ;
}
