package io.github.nomisrev.render.test.union.discriminated.wrapped

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
public sealed interface DiscriminatedWrappedUnion {
  @JvmInline
  @SerialName("employee")
  @Serializable
  public value class Employee(
    public val name: String,
  ) : DiscriminatedWrappedUnion

  @JvmInline
  @SerialName("contractor")
  @Serializable
  public value class Contractor(
    public val name: String,
  ) : DiscriminatedWrappedUnion
}
