package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Information about a Copilot Business seat assignment for a user, team, or organization.
 */
@Serializable
public data class CopilotSeatDetails(
  public val assignee: NullableSimpleUser? = null,
  public val organization: NullableOrganizationSimple? = null,
  @SerialName("assigning_team")
  public val assigningTeam: AssigningTeam? = null,
  @SerialName("pending_cancellation_date")
  public val pendingCancellationDate: LocalDate? = null,
  @SerialName("last_activity_at")
  public val lastActivityAt: Instant? = null,
  @SerialName("last_activity_editor")
  public val lastActivityEditor: String? = null,
  @SerialName("last_authenticated_at")
  public val lastAuthenticatedAt: Instant? = null,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant? = null,
  @SerialName("plan_type")
  public val planType: PlanType? = null,
) {
  /**
   * The team through which the assignee is granted access to GitHub Copilot, if applicable.
   */
  @Serializable(with = AssigningTeam.Serializer::class)
  public sealed interface AssigningTeam {
    @Serializable
    @JvmInline
    public value class CaseTeam(
      public val `value`: Team,
    ) : AssigningTeam

    @Serializable
    @JvmInline
    public value class CaseEnterpriseTeam(
      public val `value`: EnterpriseTeam,
    ) : AssigningTeam

    public object Serializer : KSerializer<AssigningTeam> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.CopilotSeatDetails.AssigningTeam", PolymorphicKind.SEALED) {
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

      override fun serialize(encoder: Encoder, `value`: AssigningTeam) {
        when(value) {
          is CaseTeam -> encoder.encodeSerializableValue(Team.serializer(), value.value)
          is CaseEnterpriseTeam -> encoder.encodeSerializableValue(EnterpriseTeam.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class PlanType(
    public val `value`: String,
  ) {
    @SerialName("business")
    Business("business"),
    @SerialName("enterprise")
    Enterprise("enterprise"),
    @SerialName("unknown")
    Unknown("unknown"),
    ;
  }
}
