package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

/**
 * Represents an issue folder, such as a project, a saved search, or a tag.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueFolderRead {
  public val id: String?

  public val name: String?

  @SerialName("IssueFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : IssueFolderRead

  @SerialName("WatchFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderRead

  @SerialName("IssueTag")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderRead

  @SerialName("Tag")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderRead

  @SerialName("SavedQuery")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderRead

  @SerialName("Project")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderRead
}
