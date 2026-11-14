package io.github.nomisrev.render.test.client.optional.nullable.variants

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface Pet {
  public val nickname: Optional<String>

  @SerialName("cat")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Cat(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val nickname: Optional<String> = Optional.Absent,
  ) : Pet

  @SerialName("dog")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Dog(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val nickname: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tricks: Optional<List<String>> = Optional.Absent,
  ) : Pet
}
