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
data class PendingDeployment(
    val environment: Environment,
    @SerialName("wait_timer") val waitTimer: Long,
    @SerialName("wait_timer_started_at") val waitTimerStartedAt: LocalDateTime?,
    @SerialName("current_user_can_approve") val currentUserCanApprove: Boolean,
    val reviewers: List<Reviewers>,
) {
    @Serializable
    data class Environment(
        val id: Long? = null,
        @SerialName("node_id") val nodeId: String? = null,
        val name: String? = null,
        val url: String? = null,
        @SerialName("html_url") val htmlUrl: String? = null,
    )

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
                    buildSerialDescriptor("io.github.nomisrev.model.PendingDeployment.Reviewers.Reviewer", PolymorphicKind.SEALED) {
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
