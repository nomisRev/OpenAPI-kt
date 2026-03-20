package io.github.nomisrev.render.test.union.discriminated.`value`.class_.case

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface DiscriminatedValueClassUnion {
  @JvmInline
  @SerialName("inline")
  @Serializable
  public value class Inline(
    public val kind: Kind,
  ) : DiscriminatedValueClassUnion {
    @Serializable
    public enum class Kind(
      public val `value`: String,
    ) {
      @SerialName("inline")
      Inline("inline"),
      ;
    }
  }

  @SerialName("remote")
  @Serializable
  public data class Remote(
    public val kind: Kind,
    public val id: String,
  ) : DiscriminatedValueClassUnion {
    @Serializable
    public enum class Kind(
      public val `value`: String,
    ) {
      @SerialName("remote")
      Remote("remote"),
      ;
    }
  }
}
