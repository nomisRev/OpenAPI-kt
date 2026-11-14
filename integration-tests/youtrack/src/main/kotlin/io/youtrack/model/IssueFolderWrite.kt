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

/**
 * Represents an issue folder, such as a project, a saved search, or a tag.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueFolderWrite {
  public val name: String?

  @SerialName("IssueFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
  ) : IssueFolderWrite

  @SerialName("WatchFolder")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class WatchFolder(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupWrite? = null,
  ) : IssueFolderWrite

  @SerialName("IssueTag")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderWrite

  @SerialName("Tag")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderWrite

  @SerialName("SavedQuery")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SavedQuery(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val visibleFor: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updateableBy: UserGroupWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val query: String? = null,
  ) : IssueFolderWrite

  @SerialName("Project")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueFolderWrite
}
