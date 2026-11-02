package io.github.nomisrev.render.test.union.discriminated.basic

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface DiscriminatedBasicUnion {
  @SerialName("employee")
  @Serializable
  public data class Employee(
    public val name: String,
  ) : DiscriminatedBasicUnion

  @SerialName("manager")
  @Serializable
  public data class Manager(
    public val level: Int,
  ) : DiscriminatedBasicUnion
}
