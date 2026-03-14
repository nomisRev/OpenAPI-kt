package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class ProjectsV2View(
    val id: Long,
    val number: Long,
    val name: String,
    val layout: Layout,
    @SerialName("node_id") val nodeId: String,
    @SerialName("project_url") val projectUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    val creator: Creator,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val filter: String? = null,
    @SerialName("visible_fields") val visibleFields: List<Long>,
    @SerialName("sort_by") val sortBy: List<List<SortBy>>,
    @SerialName("group_by") val groupBy: List<Long>,
    @SerialName("vertical_group_by") val verticalGroupBy: List<Long>,
) {
    @Serializable
    enum class Layout {
        @SerialName("table") Table, @SerialName("board") Board, @SerialName("roadmap") Roadmap;
    }

    @Serializable
    data class Creator(
        val name: String? = null,
        val email: String? = null,
        val login: String,
        val id: Long,
        @SerialName("node_id") val nodeId: String,
        @SerialName("avatar_url") val avatarUrl: String,
        @SerialName("gravatar_id") val gravatarId: String?,
        val url: String,
        @SerialName("html_url") val htmlUrl: String,
        @SerialName("followers_url") val followersUrl: String,
        @SerialName("following_url") val followingUrl: String,
        @SerialName("gists_url") val gistsUrl: String,
        @SerialName("starred_url") val starredUrl: String,
        @SerialName("subscriptions_url") val subscriptionsUrl: String,
        @SerialName("organizations_url") val organizationsUrl: String,
        @SerialName("repos_url") val reposUrl: String,
        @SerialName("events_url") val eventsUrl: String,
        @SerialName("received_events_url") val receivedEventsUrl: String,
        val type: String,
        @SerialName("site_admin") val siteAdmin: Boolean,
        @SerialName("starred_at") val starredAt: String? = null,
        @SerialName("user_view_type") val userViewType: String? = null,
    )

    @Serializable(with = SortBy.Serializer::class)
    sealed interface SortBy {
        @Serializable
        @JvmInline
        value class CaseLong(val value: Long) : SortBy

        @Serializable
        @JvmInline
        value class CaseString(val value: String) : SortBy

        object Serializer : KSerializer<SortBy> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.ProjectsV2View.SortBy", PolymorphicKind.SEALED) {
                    element("CaseLong", Long.serializer().descriptor)
                    element("CaseString", String.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): SortBy {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: SortBy) = when(value) {
                is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            }
        }
    }
}
