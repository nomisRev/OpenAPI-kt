package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface UserWrite {
  @SerialName("Default")
  @Serializable
  public data object Default : UserWrite

  @SerialName("VcsUnresolvedUser")
  @Serializable
  public class VcsUnresolvedUser() : UserWrite

  @SerialName("Me")
  @Serializable
  public class Me() : UserWrite
}
