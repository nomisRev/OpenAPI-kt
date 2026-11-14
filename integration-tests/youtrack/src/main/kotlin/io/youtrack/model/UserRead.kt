package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val login: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val fullName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val email: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val guest: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val online: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val banned: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val tags: List<IssueFolderRead.Tag>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val savedQueries: List<IssueFolderRead.SavedQuery>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val avatarUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val profiles: UserProfiles? = null,
  ) : UserRead

  @SerialName("VcsUnresolvedUser")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VcsUnresolvedUser(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val login: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val fullName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val email: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val guest: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val online: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val banned: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val tags: List<IssueFolderRead.Tag>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val savedQueries: List<IssueFolderRead.SavedQuery>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val avatarUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val profiles: UserProfiles? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
  ) : UserRead

  @SerialName("Me")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Me(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val login: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val fullName: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val email: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ringId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val guest: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val online: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val banned: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val tags: List<IssueFolderRead.Tag>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val savedQueries: List<IssueFolderRead.SavedQuery>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val avatarUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val profiles: UserProfiles? = null,
  ) : UserRead
}
