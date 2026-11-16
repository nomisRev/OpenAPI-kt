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
@Serializable(with = IssueFolderRead.Serializer::class)
public sealed interface IssueFolderRead {
  public val id: String?

  public val name: String?

  @SerialName("IssueFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "IssueFolder")
  }

  @SerialName("WatchFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = WatchFolder.Serializer::class)
  public data class WatchFolder(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<WatchFolder>(WatchFolder.generatedSerializer(), "${'$'}type", "WatchFolder")
  }

  @SerialName("IssueTag")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueTag.Serializer::class)
  public data class IssueTag(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issues: List<IssueRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val color: FieldStyleRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val untagOnResolve: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tagSharingSettings: TagSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: UserRead? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<IssueTag>(IssueTag.generatedSerializer(), "${'$'}type", "IssueTag")
  }

  @SerialName("Tag")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Tag.Serializer::class)
  public data class Tag(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issues: List<IssueRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val color: FieldStyleRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val untagOnResolve: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val tagSharingSettings: TagSharingSettings? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<Tag>(Tag.generatedSerializer(), "${'$'}type", "Tag")
  }

  @SerialName("SavedQuery")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SavedQuery.Serializer::class)
  public data class SavedQuery(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val owner: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val query: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issues: List<IssueRead>? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<SavedQuery>(SavedQuery.generatedSerializer(), "${'$'}type", "SavedQuery")
  }

  @SerialName("Project")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Project.Serializer::class)
  public data class Project(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val archived: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdBy: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val customFields: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val description: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val fromEmail: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val iconUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val issues: List<IssueRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val leader: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val replyToEmail: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val shortName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val startingNumber: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val team: UserGroupRead.ProjectTeam? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val template: Boolean? = null,
  ) : IssueFolderRead {
    public object Serializer : TaggedCaseSerializer<Project>(Project.generatedSerializer(), "${'$'}type", "Project")
  }

  public object Serializer : JsonContentPolymorphicSerializer<IssueFolderRead>(IssueFolderRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<IssueFolderRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "IssueFolder" -> Default.serializer()
      "WatchFolder" -> WatchFolder.serializer()
      "IssueTag" -> IssueTag.serializer()
      "Tag" -> Tag.serializer()
      "SavedQuery" -> SavedQuery.serializer()
      "Project" -> Project.serializer()
      else -> throw SerializationException("'${'$'}type' of IssueFolderRead selects none of IssueFolder, WatchFolder, IssueTag, Tag, SavedQuery, Project: " + tag)
    }
  }
}
