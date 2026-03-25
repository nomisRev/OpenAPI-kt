package io.github.nomisrev.render.test.union.discriminated.`enum`.case

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface DiscriminatedEnumUnion {
  @Serializable
  @SerialName("AscOrDesc")
  public enum class AscOrDesc(
    public val `value`: String,
  ) : DiscriminatedEnumUnion {
    @SerialName("asc")
    Asc("asc"),
    @SerialName("desc")
    Desc("desc"),
    ;
  }

  @Serializable
  @SerialName("manual")
  public data object Manual : DiscriminatedEnumUnion
}
