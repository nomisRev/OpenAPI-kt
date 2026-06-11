package io.github.nomisrev.render.test.union.discriminated.basic

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface DiscriminatedBasicUnion {
  @JvmInline
  @SerialName("employee")
  @Serializable
  public value class Employee(
    public val name: String,
  ) : DiscriminatedBasicUnion

  @JvmInline
  @SerialName("manager")
  @Serializable
  public value class Manager(
    public val level: Int,
  ) : DiscriminatedBasicUnion
}
