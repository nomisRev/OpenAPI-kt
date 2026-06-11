package io.github.nomisrev.render.test.discriminated.`value`.class_.subtype

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
public sealed interface Account {
  @SerialName("local")
  @Serializable
  public data object Local : Account

  @JvmInline
  @SerialName("remote")
  @Serializable
  public value class Remote(
    public val externalId: String,
  ) : Account
}
