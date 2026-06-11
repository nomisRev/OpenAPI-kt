package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface UserGroupRead {
  public val id: String?

  public val name: String?

  public val ringId: String?

  public val usersCount: Long?

  public val icon: String?

  public val allUsersGroup: Boolean?

  public val teamForProject: IssueFolderRead.Project?

  @SerialName("UserGroup")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val name: String? = null,
    override val ringId: String? = null,
    override val usersCount: Long? = null,
    override val icon: String? = null,
    override val allUsersGroup: Boolean? = null,
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("AllUsersGroup")
  @Serializable
  public data class AllUsersGroup(
    override val id: String? = null,
    override val name: String? = null,
    override val ringId: String? = null,
    override val usersCount: Long? = null,
    override val icon: String? = null,
    override val allUsersGroup: Boolean? = null,
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("RegisteredUsersGroup")
  @Serializable
  public data class RegisteredUsersGroup(
    override val id: String? = null,
    override val name: String? = null,
    override val ringId: String? = null,
    override val usersCount: Long? = null,
    override val icon: String? = null,
    override val allUsersGroup: Boolean? = null,
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("NestedGroup")
  @Serializable
  public data class NestedGroup(
    override val id: String? = null,
    override val name: String? = null,
    override val ringId: String? = null,
    override val usersCount: Long? = null,
    override val icon: String? = null,
    override val allUsersGroup: Boolean? = null,
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("ProjectTeam")
  @Serializable
  public data class ProjectTeam(
    override val id: String? = null,
    override val name: String? = null,
    override val ringId: String? = null,
    override val usersCount: Long? = null,
    override val icon: String? = null,
    override val allUsersGroup: Boolean? = null,
    override val teamForProject: IssueFolderRead.Project? = null,
    public val project: IssueFolderRead.Project? = null,
  ) : UserGroupRead
}
