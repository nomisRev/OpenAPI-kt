package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = UserGroupRead.Serializer::class)
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
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
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
  ) : UserGroupRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "UserGroup")
  }

  @SerialName("AllUsersGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AllUsersGroup.Serializer::class)
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
  ) : UserGroupRead {
    public object Serializer : TaggedCaseSerializer<AllUsersGroup>(AllUsersGroup.generatedSerializer(), "${'$'}type", "AllUsersGroup")
  }

  @SerialName("RegisteredUsersGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = RegisteredUsersGroup.Serializer::class)
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
  ) : UserGroupRead {
    public object Serializer : TaggedCaseSerializer<RegisteredUsersGroup>(RegisteredUsersGroup.generatedSerializer(), "${'$'}type", "RegisteredUsersGroup")
  }

  @SerialName("NestedGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = NestedGroup.Serializer::class)
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
  ) : UserGroupRead {
    public object Serializer : TaggedCaseSerializer<NestedGroup>(NestedGroup.generatedSerializer(), "${'$'}type", "NestedGroup")
  }

  @SerialName("ProjectTeam")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = ProjectTeam.Serializer::class)
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
  ) : UserGroupRead {
    public object Serializer : TaggedCaseSerializer<ProjectTeam>(ProjectTeam.generatedSerializer(), "${'$'}type", "ProjectTeam")
  }

  public object Serializer : JsonContentPolymorphicSerializer<UserGroupRead>(UserGroupRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<UserGroupRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "UserGroup" -> Default.serializer()
      "AllUsersGroup" -> AllUsersGroup.serializer()
      "RegisteredUsersGroup" -> RegisteredUsersGroup.serializer()
      "NestedGroup" -> NestedGroup.serializer()
      "ProjectTeam" -> ProjectTeam.serializer()
      else -> throw SerializationException("'${'$'}type' of UserGroupRead selects none of UserGroup, AllUsersGroup, RegisteredUsersGroup, NestedGroup, ProjectTeam: " + tag)
    }
  }
}
