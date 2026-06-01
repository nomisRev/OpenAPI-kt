package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface UserRead {
  public val id: String?

  public val login: String?

  public val fullName: String?

  public val email: String?

  public val ringId: String?

  public val guest: Boolean?

  public val online: Boolean?

  public val banned: Boolean?

  public val tags: List<IssueFolderRead.Tag>?

  public val savedQueries: List<IssueFolderRead.SavedQuery>?

  public val avatarUrl: String?

  public val profiles: UserProfiles?

  @SerialName("User")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val login: String? = null,
    override val fullName: String? = null,
    override val email: String? = null,
    override val ringId: String? = null,
    override val guest: Boolean? = null,
    override val online: Boolean? = null,
    override val banned: Boolean? = null,
    override val tags: List<IssueFolderRead.Tag>? = null,
    override val savedQueries: List<IssueFolderRead.SavedQuery>? = null,
    override val avatarUrl: String? = null,
    override val profiles: UserProfiles? = null,
  ) : UserRead

  @SerialName("VcsUnresolvedUser")
  @Serializable
  public data class VcsUnresolvedUser(
    override val id: String? = null,
    override val login: String? = null,
    override val fullName: String? = null,
    override val email: String? = null,
    override val ringId: String? = null,
    override val guest: Boolean? = null,
    override val online: Boolean? = null,
    override val banned: Boolean? = null,
    override val tags: List<IssueFolderRead.Tag>? = null,
    override val savedQueries: List<IssueFolderRead.SavedQuery>? = null,
    override val avatarUrl: String? = null,
    override val profiles: UserProfiles? = null,
    public val name: String? = null,
  ) : UserRead

  @SerialName("Me")
  @Serializable
  public data class Me(
    override val id: String? = null,
    override val login: String? = null,
    override val fullName: String? = null,
    override val email: String? = null,
    override val ringId: String? = null,
    override val guest: Boolean? = null,
    override val online: Boolean? = null,
    override val banned: Boolean? = null,
    override val tags: List<IssueFolderRead.Tag>? = null,
    override val savedQueries: List<IssueFolderRead.SavedQuery>? = null,
    override val avatarUrl: String? = null,
    override val profiles: UserProfiles? = null,
  ) : UserRead
}
