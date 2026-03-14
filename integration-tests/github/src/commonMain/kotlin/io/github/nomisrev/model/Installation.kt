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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class Installation(
    val id: Long,
    val account: Account?,
    @SerialName("repository_selection") val repositorySelection: RepositorySelection,
    @SerialName("access_tokens_url") val accessTokensUrl: String,
    @SerialName("repositories_url") val repositoriesUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("app_id") val appId: Long,
    @SerialName("client_id") val clientId: String? = null,
    @SerialName("target_id") val targetId: Long,
    @SerialName("target_type") val targetType: String,
    val permissions: AppPermissions,
    val events: List<String>,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("single_file_name") val singleFileName: String?,
    @SerialName("has_multiple_single_files") val hasMultipleSingleFiles: Boolean? = null,
    @SerialName("single_file_paths") val singleFilePaths: List<String>? = null,
    @SerialName("app_slug") val appSlug: String,
    @SerialName("suspended_by") val suspendedBy: NullableSimpleUser?,
    @SerialName("suspended_at") val suspendedAt: LocalDateTime?,
    @SerialName("contact_email") val contactEmail: String? = null,
) {
    @Serializable(with = Account.Serializer::class)
    sealed interface Account {
        @Serializable
        @JvmInline
        value class CaseSimpleUser(val value: SimpleUser) : Account

        @Serializable
        @JvmInline
        value class CaseEnterprise(val value: Enterprise) : Account

        object Serializer : KSerializer<Account> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Installation.Account", PolymorphicKind.SEALED) {
                    element("CaseSimpleUser", SimpleUser.serializer().descriptor)
                    element("CaseEnterprise", Enterprise.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Account {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
                    CaseEnterprise::class to { CaseEnterprise(decodeFromJsonElement(Enterprise.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Account) = when(value) {
                is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
                is CaseEnterprise -> encoder.encodeSerializableValue(Enterprise.serializer(), value.value)
            }
        }
    }

    @Serializable
    enum class RepositorySelection {
        @SerialName("all") All, @SerialName("selected") Selected;
    }
}
