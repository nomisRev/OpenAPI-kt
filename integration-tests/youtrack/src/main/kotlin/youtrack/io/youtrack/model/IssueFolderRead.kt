package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val name: String? = null,
  ) : IssueFolderRead

  @SerialName("WatchFolder")
  @Serializable
  public data class WatchFolder(
    override val id: String? = null,
    override val name: String? = null,
    public val owner: UserRead? = null,
    public val visibleFor: UserGroupRead? = null,
    public val updateableBy: UserGroupRead? = null,
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
  ) : IssueFolderRead

  @SerialName("IssueTag")
  @Serializable
  public data class IssueTag(
    override val id: String? = null,
    override val name: String? = null,
    public val issues: List<IssueRead>? = null,
    public val color: FieldStyleRead? = null,
    public val untagOnResolve: Boolean? = null,
    public val visibleFor: UserGroupRead? = null,
    public val updateableBy: UserGroupRead? = null,
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    public val tagSharingSettings: TagSharingSettings? = null,
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
    public val owner: UserRead? = null,
  ) : IssueFolderRead

  @SerialName("Tag")
  @Serializable
  public data class Tag(
    override val id: String? = null,
    override val name: String? = null,
    public val owner: UserRead? = null,
    public val visibleFor: UserGroupRead? = null,
    public val updateableBy: UserGroupRead? = null,
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
    public val issues: List<IssueRead>? = null,
    public val color: FieldStyleRead? = null,
    public val untagOnResolve: Boolean? = null,
    public val tagSharingSettings: TagSharingSettings? = null,
  ) : IssueFolderRead

  @SerialName("SavedQuery")
  @Serializable
  public data class SavedQuery(
    override val id: String? = null,
    override val name: String? = null,
    public val owner: UserRead? = null,
    public val visibleFor: UserGroupRead? = null,
    public val updateableBy: UserGroupRead? = null,
    public val readSharingSettings: WatchFolderSharingSettings? = null,
    public val updateSharingSettings: WatchFolderSharingSettings? = null,
    public val query: String? = null,
    public val issues: List<IssueRead>? = null,
  ) : IssueFolderRead

  @SerialName("Project")
  @Serializable
  public data class Project(
    override val id: String? = null,
    override val name: String? = null,
    public val archived: Boolean? = null,
    public val createdBy: UserRead? = null,
    public val customFields: JsonElement? = null,
    public val description: String? = null,
    public val fromEmail: String? = null,
    public val iconUrl: String? = null,
    public val issues: List<IssueRead>? = null,
    public val leader: UserRead? = null,
    public val replyToEmail: String? = null,
    public val shortName: String? = null,
    public val startingNumber: Long? = null,
    public val team: UserGroupRead.ProjectTeam? = null,
    public val template: Boolean? = null,
  ) : IssueFolderRead
}
