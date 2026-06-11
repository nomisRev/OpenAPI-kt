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

/**
 * Represents an issue folder, such as a project, a saved search, or a tag.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueFolderWrite {
  public val name: String?

  @SerialName("IssueFolder")
  @Serializable
  public data class Default(
    override val name: String? = null,
  ) : IssueFolderWrite

  @SerialName("WatchFolder")
  @Serializable
  public data class WatchFolder(
    override val name: String? = null,
    public val visibleFor: UserGroupWrite? = null,
    public val updateableBy: UserGroupWrite? = null,
  ) : IssueFolderWrite

  @SerialName("IssueTag")
  @Serializable
  public data class IssueTag(
    override val name: String? = null,
    public val issues: List<IssueWrite>? = null,
    public val color: FieldStyleWrite? = null,
    public val untagOnResolve: Boolean? = null,
    public val visibleFor: UserGroupWrite? = null,
    public val updateableBy: UserGroupWrite? = null,
  ) : IssueFolderWrite

  @SerialName("Tag")
  @Serializable
  public data class Tag(
    override val name: String? = null,
    public val visibleFor: UserGroupWrite? = null,
    public val updateableBy: UserGroupWrite? = null,
    public val issues: List<IssueWrite>? = null,
    public val color: FieldStyleWrite? = null,
    public val untagOnResolve: Boolean? = null,
  ) : IssueFolderWrite

  @SerialName("SavedQuery")
  @Serializable
  public data class SavedQuery(
    override val name: String? = null,
    public val visibleFor: UserGroupWrite? = null,
    public val updateableBy: UserGroupWrite? = null,
    public val query: String? = null,
  ) : IssueFolderWrite

  @SerialName("Project")
  @Serializable
  public data class Project(
    override val name: String? = null,
    public val archived: Boolean? = null,
    public val createdBy: UserWrite? = null,
    public val description: String? = null,
    public val fromEmail: String? = null,
    public val issues: List<IssueWrite>? = null,
    public val leader: UserWrite? = null,
    public val replyToEmail: String? = null,
    public val shortName: String? = null,
    public val startingNumber: Long? = null,
    public val template: Boolean? = null,
  ) : IssueFolderWrite
}
