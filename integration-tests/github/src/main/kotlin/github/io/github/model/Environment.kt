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
import kotlinx.serialization.json.jsonObject

/**
 * Details of a deployment environment
 */
@Serializable
public data class Environment(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("protection_rules")
  public val protectionRules: List<ProtectionRules>? = null,
  @SerialName("deployment_branch_policy")
  public val deploymentBranchPolicy: DeploymentBranchPolicySettings? = null,
) {
  @Serializable(with = ProtectionRules.Serializer::class)
  public sealed interface ProtectionRules {
    @Serializable
    public data class IdAndNodeIdAndTypeAndWaitTimer(
      public val id: Long,
      @SerialName("node_id")
      public val nodeId: String,
      public val type: String,
      @SerialName("wait_timer")
      public val waitTimer: WaitTimer? = null,
    ) : ProtectionRules

    @Serializable
    public data class IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers(
      public val id: Long,
      @SerialName("node_id")
      public val nodeId: String,
      @SerialName("prevent_self_review")
      public val preventSelfReview: Boolean? = null,
      public val type: String,
      public val reviewers: List<Reviewers>? = null,
    ) : ProtectionRules {
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
                buildSerialDescriptor("io.github.model.Environment.ProtectionRules.IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.Reviewers.Reviewer", PolymorphicKind.SEALED) {
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

    @Serializable
    public data class IdAndNodeIdAndType(
      public val id: Long,
      @SerialName("node_id")
      public val nodeId: String,
      public val type: String,
    ) : ProtectionRules

    public object Serializer : KSerializer<ProtectionRules> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Environment.ProtectionRules", PolymorphicKind.SEALED) {
        element("IdAndNodeIdAndTypeAndWaitTimer", IdAndNodeIdAndTypeAndWaitTimer.serializer().descriptor)
        element("IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers", IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.serializer().descriptor)
        element("IdAndNodeIdAndType", IdAndNodeIdAndType.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): ProtectionRules {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        val keys = value.jsonObject.keys
        if ("prevent_self_review" in keys || "reviewers" in keys) {
          return json.decodeFromJsonElement(IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.serializer(), value)
        }
        if ("wait_timer" in keys) {
          return json.decodeFromJsonElement(IdAndNodeIdAndTypeAndWaitTimer.serializer(), value)
        }
        return json.decodeFromJsonElement(IdAndNodeIdAndType.serializer(), value)
      }

      override fun serialize(encoder: Encoder, `value`: ProtectionRules) {
        when(value) {
          is IdAndNodeIdAndTypeAndWaitTimer -> encoder.encodeSerializableValue(IdAndNodeIdAndTypeAndWaitTimer.serializer(), value)
          is IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers -> encoder.encodeSerializableValue(IdAndNodeIdAndPreventSelfReviewAndTypeAndReviewers.serializer(), value)
          is IdAndNodeIdAndType -> encoder.encodeSerializableValue(IdAndNodeIdAndType.serializer(), value)
        }
      }
    }
  }
}
