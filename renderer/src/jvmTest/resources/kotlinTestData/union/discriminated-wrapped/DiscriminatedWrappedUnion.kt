package io.github.nomisrev.render.test.union.discriminated.wrapped

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface DiscriminatedWrappedUnion {
  @Serializable
  @JvmInline
  @SerialName("employee")
  public value class Employee(
    public val `value`: WrappedEmployee,
  ) : DiscriminatedWrappedUnion

  @Serializable
  @JvmInline
  @SerialName("contractor")
  public value class Contractor(
    public val `value`: WrappedContractor,
  ) : DiscriminatedWrappedUnion
}
