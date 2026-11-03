package io.github.nomisrev.render.test.client.union.nullable.member

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface InferredPet {
  @SerialName("cat")
  @Serializable
  public data class Cat(
    public val meow: String,
  ) : InferredPet

  @SerialName("dog")
  @Serializable
  public data class Dog(
    public val bark: String,
  ) : InferredPet
}
