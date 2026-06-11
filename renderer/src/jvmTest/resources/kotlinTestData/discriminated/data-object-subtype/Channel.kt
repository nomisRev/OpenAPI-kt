package io.github.nomisrev.render.test.discriminated.`data`.object_.subtype

import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("kind")
@Serializable
public sealed interface Channel {
  @SerialName("sms")
  @Serializable
  public data object Sms : Channel

  @SerialName("email")
  @Serializable
  public data object Email : Channel
}
