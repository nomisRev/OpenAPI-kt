package io.github.nomisrev.render.test.client.discriminator.without.base

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("petType")
@Serializable
public sealed interface Pet {
  public val name: String

  @SerialName("feline")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Feline(
    override val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val huntingSkill: String? = null,
  ) : Pet

  @SerialName("bird")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Bird(
    override val name: String,
    public val span: Double,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val canFly: Boolean? = null,
  ) : Pet

  @SerialName("Dog")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Dog(
    override val name: String,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val packSize: Long? = null,
  ) : Pet
}
