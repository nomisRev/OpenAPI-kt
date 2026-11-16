package io.github.nomisrev.render.test.object_.plain.model.no.read.suffix

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

@Serializable(with = IssueFolderRead.Serializer::class)
public sealed interface IssueFolderRead {
  public val id: String?

  @SerialName("IssueFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "IssueFolder")
  }

  @SerialName("Project")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Project.Serializer::class)
  public data class Project(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shortName: String? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<Project>(Project.generatedSerializer(), "${'$'}type", "Project")
  }

  public object Serializer : JsonContentPolymorphicSerializer<IssueFolderRead>(IssueFolderRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<IssueFolderRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "IssueFolder" -> Default.serializer()
      "Project" -> Project.serializer()
      else -> throw SerializationException("'${'$'}type' of IssueFolderRead selects none of IssueFolder, Project: " + tag)
    }
  }
}
