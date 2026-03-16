package io.github.nomisrev.render.test.discriminated.basic

import kotlin.Long
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
public sealed interface User {
  public val id: Long

  @JvmInline
  @SerialName("anonymous")
  @Serializable
  public value class Anonymous(
    override val id: Long,
  ) : User

  @SerialName("registered")
  @Serializable
  public data class Registered(
    override val id: Long,
    public val email: String,
  ) : User
}
