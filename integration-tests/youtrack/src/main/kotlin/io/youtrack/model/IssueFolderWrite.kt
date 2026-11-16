package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

/**
 * Represents an issue folder, such as a project, a saved search, or a tag.
 */
@Serializable(with = IssueFolderWrite.Serializer::class)
public sealed interface IssueFolderWrite {
  public val name: String?

  @SerialName("IssueFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : IssueFolderWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "IssueFolder")
  }

  @SerialName("WatchFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = WatchFolder.Serializer::class)
  public data class WatchFolder(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupWrite? = null,
  ) : IssueFolderWrite {
    public object Serializer : TaggedCaseSerializer<WatchFolder>(WatchFolder.generatedSerializer(), "${'$'}type", "WatchFolder")
  }

  @SerialName("IssueTag")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueTag.Serializer::class)
  public data class IssueTag(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issues: List<IssueWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val untagOnResolve: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupWrite? = null,
  ) : IssueFolderWrite {
    public object Serializer : TaggedCaseSerializer<IssueTag>(IssueTag.generatedSerializer(), "${'$'}type", "IssueTag")
  }

  @SerialName("Tag")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Tag.Serializer::class)
  public data class Tag(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issues: List<IssueWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val color: FieldStyleWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val untagOnResolve: Boolean? = null,
  ) : IssueFolderWrite {
    public object Serializer : TaggedCaseSerializer<Tag>(Tag.generatedSerializer(), "${'$'}type", "Tag")
  }

  @SerialName("SavedQuery")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SavedQuery.Serializer::class)
  public data class SavedQuery(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val query: String? = null,
  ) : IssueFolderWrite {
    public object Serializer : TaggedCaseSerializer<SavedQuery>(SavedQuery.generatedSerializer(), "${'$'}type", "SavedQuery")
  }

  @SerialName("Project")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Project.Serializer::class)
  public data class Project(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdBy: UserWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fromEmail: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issues: List<IssueWrite>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val leader: UserWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val replyToEmail: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shortName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val startingNumber: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val template: Boolean? = null,
  ) : IssueFolderWrite {
    public object Serializer : TaggedCaseSerializer<Project>(Project.generatedSerializer(), "${'$'}type", "Project")
  }

  public object Serializer : JsonContentPolymorphicSerializer<IssueFolderWrite>(IssueFolderWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<IssueFolderWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "IssueFolder" -> Default.serializer()
      "WatchFolder" -> WatchFolder.serializer()
      "IssueTag" -> IssueTag.serializer()
      "Tag" -> Tag.serializer()
      "SavedQuery" -> SavedQuery.serializer()
      "Project" -> Project.serializer()
      else -> throw SerializationException("'${'$'}type' of IssueFolderWrite selects none of IssueFolder, WatchFolder, IssueTag, Tag, SavedQuery, Project: " + tag)
    }
  }
}
