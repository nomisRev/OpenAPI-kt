package io.youtrack.model

import kotlin.Boolean
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

@Serializable(with = UserRead.Serializer::class)
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
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
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
  ) : UserRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "User")
  }

  @SerialName("VcsUnresolvedUser")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VcsUnresolvedUser.Serializer::class)
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
  ) : UserRead {
    public object Serializer : TaggedCaseSerializer<VcsUnresolvedUser>(VcsUnresolvedUser.generatedSerializer(), "${'$'}type", "VcsUnresolvedUser")
  }

  @SerialName("Me")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Me.Serializer::class)
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
  ) : UserRead {
    public object Serializer : TaggedCaseSerializer<Me>(Me.generatedSerializer(), "${'$'}type", "Me")
  }

  public object Serializer : JsonContentPolymorphicSerializer<UserRead>(UserRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<UserRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "User" -> Default.serializer()
      "VcsUnresolvedUser" -> VcsUnresolvedUser.serializer()
      "Me" -> Me.serializer()
      else -> throw SerializationException("'${'$'}type' of UserRead selects none of User, VcsUnresolvedUser, Me: " + tag)
    }
  }
}
