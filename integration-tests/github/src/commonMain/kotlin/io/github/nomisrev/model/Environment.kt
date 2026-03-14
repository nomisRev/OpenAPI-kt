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
data class Environment(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val name: String,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("protection_rules") val protectionRules: List<ProtectionRules>? = null,
    @SerialName("deployment_branch_policy") val deploymentBranchPolicy: DeploymentBranchPolicySettings? = null,
) {
    @Serializable(with = ProtectionRules.Serializer::class)
    sealed interface ProtectionRules {
        @Serializable
        data class IdAndNodeIdAndTypeAndWaitTimer(
            val id: Long,
            @SerialName("node_id") val nodeId: String,
            val type: String,
            @SerialName("wait_timer") val waitTimer: WaitTimer? = null,
        ) : ProtectionRules

        @Serializable
        data class IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers(
            val id: Long,
            @SerialName("node_id") val nodeId: String,
            @SerialName("prevent_self_review") val preventSelfReview: Boolean? = null,
            val type: String,
            val reviewers: List<Reviewers>? = null,
        ) : ProtectionRules {
            @Serializable
            data class Reviewers(val type: DeploymentReviewerType? = null, val reviewer: Reviewer? = null) {
                @Serializable(with = Reviewer.Serializer::class)
                sealed interface Reviewer {
                    @Serializable
                    @JvmInline
                    value class CaseSimpleUser(val value: SimpleUser) : Reviewer

                    @Serializable
                    @JvmInline
                    value class CaseTeam(val value: Team) : Reviewer

                    object Serializer : KSerializer<Reviewer> {
                        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                        override val descriptor: SerialDescriptor =
                            buildSerialDescriptor("io.github.nomisrev.model.Environment.ProtectionRules.IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.Reviewers.Reviewer", PolymorphicKind.SEALED) {
                                element("CaseSimpleUser", SimpleUser.serializer().descriptor)
                                element("CaseTeam", Team.serializer().descriptor)
                            }

                        override fun deserialize(decoder: Decoder): Reviewer {
                            val value = decoder.decodeSerializableValue(JsonElement.serializer())
                            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                            return json.attemptDeserialize(
                                value,
                                CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
                                CaseTeam::class to { CaseTeam(decodeFromJsonElement(Team.serializer(), it)) },
                            )
                        }

                        override fun serialize(encoder: Encoder, value: Reviewer) = when(value) {
                            is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
                            is CaseTeam -> encoder.encodeSerializableValue(Team.serializer(), value.value)
                        }
                    }
                }
            }
        }

        @Serializable
        data class IdAndNodeIdAndType(
            val id: Long,
            @SerialName("node_id") val nodeId: String,
            val type: String,
        ) : ProtectionRules

        object Serializer : KSerializer<ProtectionRules> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Environment.ProtectionRules", PolymorphicKind.SEALED) {
                    element("IdAndNodeIdAndTypeAndWaitTimer", ProtectionRules.IdAndNodeIdAndTypeAndWaitTimer.serializer().descriptor)
                    element("IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers", ProtectionRules.IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.serializer().descriptor)
                    element("IdAndNodeIdAndType", ProtectionRules.IdAndNodeIdAndType.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): ProtectionRules {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers::class to { decodeFromJsonElement(ProtectionRules.IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.serializer(), it) },
                    IdAndNodeIdAndTypeAndWaitTimer::class to { decodeFromJsonElement(ProtectionRules.IdAndNodeIdAndTypeAndWaitTimer.serializer(), it) },
                    IdAndNodeIdAndType::class to { decodeFromJsonElement(ProtectionRules.IdAndNodeIdAndType.serializer(), it) },
                )
            }

            override fun serialize(encoder: Encoder, value: ProtectionRules) = when(value) {
                is IdAndNodeIdAndTypeAndWaitTimer -> encoder.encodeSerializableValue(ProtectionRules.IdAndNodeIdAndTypeAndWaitTimer.serializer(), value)
                is IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers -> encoder.encodeSerializableValue(ProtectionRules.IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.serializer(), value)
                is IdAndNodeIdAndType -> encoder.encodeSerializableValue(ProtectionRules.IdAndNodeIdAndType.serializer(), value)
            }
        }
    }
}
