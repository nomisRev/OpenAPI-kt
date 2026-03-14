package io.github.nomisrev.model

import kotlinx.datetime.LocalDate
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
data class CopilotSeatDetails(
    val assignee: NullableSimpleUser? = null,
    val organization: NullableOrganizationSimple? = null,
    @SerialName("assigning_team") val assigningTeam: AssigningTeam? = null,
    @SerialName("pending_cancellation_date") val pendingCancellationDate: LocalDate? = null,
    @SerialName("last_activity_at") val lastActivityAt: LocalDateTime? = null,
    @SerialName("last_activity_editor") val lastActivityEditor: String? = null,
    @SerialName("last_authenticated_at") val lastAuthenticatedAt: LocalDateTime? = null,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime? = null,
    @SerialName("plan_type") val planType: PlanType? = null,
) {
    @Serializable(with = AssigningTeam.Serializer::class)
    sealed interface AssigningTeam {
        @Serializable
        @JvmInline
        value class CaseTeam(val value: Team) : AssigningTeam

        @Serializable
        @JvmInline
        value class CaseEnterpriseTeam(val value: EnterpriseTeam) : AssigningTeam

        object Serializer : KSerializer<AssigningTeam> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.CopilotSeatDetails.AssigningTeam", PolymorphicKind.SEALED) {
                    element("CaseTeam", Team.serializer().descriptor)
                    element("CaseEnterpriseTeam", EnterpriseTeam.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): AssigningTeam {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseTeam::class to { CaseTeam(decodeFromJsonElement(Team.serializer(), it)) },
                    CaseEnterpriseTeam::class to { CaseEnterpriseTeam(decodeFromJsonElement(EnterpriseTeam.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: AssigningTeam) = when(value) {
                is CaseTeam -> encoder.encodeSerializableValue(Team.serializer(), value.value)
                is CaseEnterpriseTeam -> encoder.encodeSerializableValue(EnterpriseTeam.serializer(), value.value)
            }
        }
    }

    @Serializable
    enum class PlanType {
        @SerialName("business") Business, @SerialName("enterprise") Enterprise, @SerialName("unknown") Unknown;
    }
}
