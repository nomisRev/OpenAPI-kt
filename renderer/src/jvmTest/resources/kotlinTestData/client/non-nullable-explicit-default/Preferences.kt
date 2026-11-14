package io.github.nomisrev.render.test.client.non.nullable.explicit.default

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Preferences(
  public val name: String,
  public val kind: Kind,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val appId: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val optionalKind: OptionalKind? = null,
  public val nullableName: String? = null,
) {
  @Serializable
  public enum class Kind {
    S,
    O,
  }

  @Serializable
  public enum class OptionalKind {
    S,
    O,
  }
}
