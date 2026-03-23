package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface UserGroupWrite {
  @SerialName("Default")
  @Serializable
  public data object Default : UserGroupWrite

  @SerialName("AllUsersGroup")
  @Serializable
  public class AllUsersGroup() : UserGroupWrite

  @SerialName("RegisteredUsersGroup")
  @Serializable
  public class RegisteredUsersGroup() : UserGroupWrite

  @SerialName("NestedGroup")
  @Serializable
  public class NestedGroup() : UserGroupWrite

  @SerialName("ProjectTeam")
  @Serializable
  public class ProjectTeam() : UserGroupWrite
}
