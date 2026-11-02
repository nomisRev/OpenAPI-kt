package io.github.nomisrev.render.test.union.discriminated.mixed

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface DiscriminatedMixedUnion {
  @SerialName("user")
  @Serializable
  public data class User(
    public val name: String,
  ) : DiscriminatedMixedUnion

  @SerialName("guest")
  @Serializable
  public data class Guest(
    public val id: Int,
  ) : DiscriminatedMixedUnion
}
