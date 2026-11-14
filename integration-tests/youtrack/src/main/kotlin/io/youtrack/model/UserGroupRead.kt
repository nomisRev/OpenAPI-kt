package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val usersCount: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val icon: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val allUsersGroup: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("AllUsersGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AllUsersGroup(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val usersCount: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val icon: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val allUsersGroup: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("RegisteredUsersGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class RegisteredUsersGroup(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val usersCount: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val icon: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val allUsersGroup: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("NestedGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class NestedGroup(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val usersCount: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val icon: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val allUsersGroup: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val teamForProject: IssueFolderRead.Project? = null,
  ) : UserGroupRead

  @SerialName("ProjectTeam")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectTeam(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val usersCount: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val icon: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val allUsersGroup: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val teamForProject: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val project: IssueFolderRead.Project? = null,
  ) : UserGroupRead
}
