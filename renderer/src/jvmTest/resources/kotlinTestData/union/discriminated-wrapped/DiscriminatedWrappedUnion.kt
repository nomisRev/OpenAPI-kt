package io.github.nomisrev.render.test.union.discriminated.wrapped

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface DiscriminatedWrappedUnion {
  @SerialName("employee")
  @Serializable
  public data class Employee(
    public val name: String,
  ) : DiscriminatedWrappedUnion

  @SerialName("contractor")
  @Serializable
  public data class Contractor(
    public val name: String,
  ) : DiscriminatedWrappedUnion
}
