package io.github.nomisrev.render.test.union.discriminated.mixed

import kotlin.Int
import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface DiscriminatedMixedUnion {
  @Serializable
  @JvmInline
  @SerialName("user")
  public value class User(
    public val `value`: MixedUser,
  ) : DiscriminatedMixedUnion

  @SerialName("guest")
  @Serializable
  public data class Guest(
    public val kind: Kind,
    public val id: Int,
  ) : DiscriminatedMixedUnion {
    @Serializable
    public enum class Kind {
      @SerialName("guest")
      Guest,
    }
  }
}
