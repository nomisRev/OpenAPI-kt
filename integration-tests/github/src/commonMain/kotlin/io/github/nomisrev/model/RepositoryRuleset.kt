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
data class RepositoryRuleset(
    val id: Long,
    val name: String,
    val target: Target? = null,
    @SerialName("source_type") val sourceType: SourceType? = null,
    val source: String,
    val enforcement: RepositoryRuleEnforcement,
    @SerialName("bypass_actors") val bypassActors: List<RepositoryRulesetBypassActor>? = null,
    @SerialName("current_user_can_bypass") val currentUserCanBypass: CurrentUserCanBypass? = null,
    @SerialName("node_id") val nodeId: String? = null,
    @SerialName("_links") val links: Links? = null,
    val conditions: Conditions? = null,
    val rules: List<RepositoryRule>? = null,
    @SerialName("created_at") val createdAt: LocalDateTime? = null,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
) {
    @Serializable
    enum class Target {
        @SerialName("branch")
        Branch,
        @SerialName("tag")
        Tag,
        @SerialName("push")
        Push,
        @SerialName("repository")
        Repository;
    }

    @Serializable
    enum class SourceType {
        Repository, Organization, Enterprise;
    }

    @Serializable
    enum class CurrentUserCanBypass {
        @SerialName("always")
        Always,
        @SerialName("pull_requests_only")
        PullRequestsOnly,
        @SerialName("never")
        Never,
        @SerialName("exempt")
        Exempt;
    }

    @Serializable
    data class Links(val self: Self? = null, val html: Html? = null) {
        @Serializable
        @JvmInline
        value class Self(val href: String? = null)

        @Serializable
        @JvmInline
        value class Html(val href: String? = null)
    }

    @Serializable(with = Conditions.Serializer::class)
    sealed interface Conditions {
        @Serializable
        @JvmInline
        value class CaseRepositoryRulesetConditions(val value: RepositoryRulesetConditions) : Conditions

        @Serializable
        @JvmInline
        value class CaseOrgRulesetConditions(val value: OrgRulesetConditions) : Conditions

        object Serializer : KSerializer<Conditions> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.RepositoryRuleset.Conditions", PolymorphicKind.SEALED) {
                    element("CaseRepositoryRulesetConditions", RepositoryRulesetConditions.serializer().descriptor)
                    element("CaseOrgRulesetConditions", OrgRulesetConditions.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Conditions {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseRepositoryRulesetConditions::class to { CaseRepositoryRulesetConditions(decodeFromJsonElement(RepositoryRulesetConditions.serializer(), it)) },
                    CaseOrgRulesetConditions::class to { CaseOrgRulesetConditions(decodeFromJsonElement(OrgRulesetConditions.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Conditions) = when(value) {
                is CaseRepositoryRulesetConditions -> encoder.encodeSerializableValue(RepositoryRulesetConditions.serializer(), value.value)
                is CaseOrgRulesetConditions -> encoder.encodeSerializableValue(OrgRulesetConditions.serializer(), value.value)
            }
        }
    }
}
