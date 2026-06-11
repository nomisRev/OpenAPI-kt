package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
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
 * Details of a deployment that is waiting for protection rules to pass
 */
@Serializable
public data class PendingDeployment(
  public val environment: Environment,
  @SerialName("wait_timer")
  public val waitTimer: Long,
  @SerialName("wait_timer_started_at")
  public val waitTimerStartedAt: Instant?,
  @SerialName("current_user_can_approve")
  public val currentUserCanApprove: Boolean,
  public val reviewers: List<Reviewers>,
) {
  @Serializable
  public data class Environment(
    public val id: Long? = null,
    @SerialName("node_id")
    public val nodeId: String? = null,
    public val name: String? = null,
    public val url: String? = null,
    @SerialName("html_url")
    public val htmlUrl: String? = null,
  )

  @Serializable
  public data class Reviewers(
    public val type: DeploymentReviewerType? = null,
    public val reviewer: Reviewer? = null,
  ) {
    @Serializable(with = Reviewer.Serializer::class)
    public sealed interface Reviewer {
      @Serializable
      @JvmInline
      public value class CaseSimpleUser(
        public val `value`: SimpleUser,
      ) : Reviewer

      @Serializable
      @JvmInline
      public value class CaseTeam(
        public val `value`: Team,
      ) : Reviewer

      public object Serializer : KSerializer<Reviewer> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.model.PendingDeployment.Reviewers.Reviewer", PolymorphicKind.SEALED) {
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

        override fun serialize(encoder: Encoder, `value`: Reviewer) {
          when(value) {
            is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
            is CaseTeam -> encoder.encodeSerializableValue(Team.serializer(), value.value)
          }
        }
      }
    }
  }
}
