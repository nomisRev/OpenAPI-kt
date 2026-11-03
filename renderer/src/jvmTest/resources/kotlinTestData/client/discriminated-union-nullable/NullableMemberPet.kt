package io.github.nomisrev.render.test.client.discriminated.union.nullable

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("petType")
@Serializable
public sealed interface NullableMemberPet {
  @SerialName("cat")
  @Serializable
  public data class Cat(
    public val meow: String,
  ) : NullableMemberPet

  @SerialName("dog")
  @Serializable
  public data class Dog(
    public val bark: String,
  ) : NullableMemberPet
}
