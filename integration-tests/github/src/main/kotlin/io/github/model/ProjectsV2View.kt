package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull

/**
 * A view inside a projects v2 project
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ProjectsV2View(
  public val id: Long,
  public val number: Long,
  public val name: String,
  public val layout: Layout,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("project_url")
  public val projectUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val creator: Creator,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val filter: Optional<String> = Optional.Absent,
  @SerialName("visible_fields")
  public val visibleFields: List<Long>,
  @SerialName("sort_by")
  public val sortBy: List<List<SortBy>>,
  @SerialName("group_by")
  public val groupBy: List<Long>,
  @SerialName("vertical_group_by")
  public val verticalGroupBy: List<Long>,
) {
  /**
   * A GitHub user.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Creator(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val email: Optional<String> = Optional.Absent,
    public val login: String,
    public val id: Long,
    @SerialName("node_id")
    public val nodeId: String,
    @SerialName("avatar_url")
    public val avatarUrl: String,
    @SerialName("gravatar_id")
    public val gravatarId: String?,
    public val url: String,
    @SerialName("html_url")
    public val htmlUrl: String,
    @SerialName("followers_url")
    public val followersUrl: String,
    @SerialName("following_url")
    public val followingUrl: String,
    @SerialName("gists_url")
    public val gistsUrl: String,
    @SerialName("starred_url")
    public val starredUrl: String,
    @SerialName("subscriptions_url")
    public val subscriptionsUrl: String,
    @SerialName("organizations_url")
    public val organizationsUrl: String,
    @SerialName("repos_url")
    public val reposUrl: String,
    @SerialName("events_url")
    public val eventsUrl: String,
    @SerialName("received_events_url")
    public val receivedEventsUrl: String,
    public val type: String,
    @SerialName("site_admin")
    public val siteAdmin: Boolean,
    @SerialName("starred_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val starredAt: String? = null,
    @SerialName("user_view_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val userViewType: String? = null,
  )

  @Serializable
  public enum class Layout(
    public val `value`: String,
  ) {
    @SerialName("table")
    Table("table"),
    @SerialName("board")
    Board("board"),
    @SerialName("roadmap")
    Roadmap("roadmap"),
    ;
  }

  @Serializable(with = SortBy.Serializer::class)
  public sealed interface SortBy {
    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : SortBy

    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : SortBy

    public object Serializer : KSerializer<SortBy> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.ProjectsV2View.SortBy", PolymorphicKind.SEALED) {
        element("CaseLong", Long.serializer().descriptor)
        element("CaseString", String.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): SortBy {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
            value,
            CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
      }

      override fun serialize(encoder: Encoder, `value`: SortBy) {
        when(value) {
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
        }
      }
    }
  }
}
