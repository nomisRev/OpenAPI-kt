package io.github.nomisrev.render.test.union.discriminated.mixed

import kotlin.Int
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
public sealed interface DiscriminatedMixedUnion {
  @JvmInline
  @SerialName("user")
  @Serializable
  public value class User(
    public val name: String,
  ) : DiscriminatedMixedUnion

  @JvmInline
  @SerialName("guest")
  @Serializable
  public value class Guest(
    public val id: Int,
  ) : DiscriminatedMixedUnion
}
