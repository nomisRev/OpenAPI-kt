package io.github.nomisrev.render.test.union.discriminated.`enum`.case

import kotlin.OptIn
import kotlin.jvm.JvmInline
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
  public enum class AscOrDesc : DiscriminatedEnumUnion {
    @SerialName("asc")
    Asc,
    @SerialName("desc")
    Desc,
  }

  @Serializable
  @JvmInline
  @SerialName("manual")
  public value class CaseEnumManual(
    public val `value`: EnumManual,
  ) : DiscriminatedEnumUnion
}
