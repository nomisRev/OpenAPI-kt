package io.youtrack.model

import kotlin.OptIn
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = UserGroupWrite.Serializer::class)
public sealed interface UserGroupWrite {
  @SerialName("UserGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : UserGroupWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "UserGroup")
  }

  @SerialName("AllUsersGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AllUsersGroup.Serializer::class)
  public class AllUsersGroup() : UserGroupWrite {
    public object Serializer : TaggedCaseSerializer<AllUsersGroup>(AllUsersGroup.generatedSerializer(), "${'$'}type", "AllUsersGroup")
  }

  @SerialName("RegisteredUsersGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = RegisteredUsersGroup.Serializer::class)
  public class RegisteredUsersGroup() : UserGroupWrite {
    public object Serializer : TaggedCaseSerializer<RegisteredUsersGroup>(RegisteredUsersGroup.generatedSerializer(), "${'$'}type", "RegisteredUsersGroup")
  }

  @SerialName("NestedGroup")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = NestedGroup.Serializer::class)
  public class NestedGroup() : UserGroupWrite {
    public object Serializer : TaggedCaseSerializer<NestedGroup>(NestedGroup.generatedSerializer(), "${'$'}type", "NestedGroup")
  }

  @SerialName("ProjectTeam")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = ProjectTeam.Serializer::class)
  public class ProjectTeam() : UserGroupWrite {
    public object Serializer : TaggedCaseSerializer<ProjectTeam>(ProjectTeam.generatedSerializer(), "${'$'}type", "ProjectTeam")
  }

  public object Serializer : JsonContentPolymorphicSerializer<UserGroupWrite>(UserGroupWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<UserGroupWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "UserGroup" -> Default.serializer()
      "AllUsersGroup" -> AllUsersGroup.serializer()
      "RegisteredUsersGroup" -> RegisteredUsersGroup.serializer()
      "NestedGroup" -> NestedGroup.serializer()
      "ProjectTeam" -> ProjectTeam.serializer()
      else -> throw SerializationException("'${'$'}type' of UserGroupWrite selects none of UserGroup, AllUsersGroup, RegisteredUsersGroup, NestedGroup, ProjectTeam: " + tag)
    }
  }
}
