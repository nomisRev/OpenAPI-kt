package io.github.model

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
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * A set of rules to apply when specified conditions are met.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RepositoryRuleset(
  public val id: Long,
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val target: Target? = null,
  @SerialName("source_type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sourceType: SourceType? = null,
  public val source: String,
  public val enforcement: RepositoryRuleEnforcement,
  @SerialName("bypass_actors")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val bypassActors: List<RepositoryRulesetBypassActor>? = null,
  @SerialName("current_user_can_bypass")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val currentUserCanBypass: CurrentUserCanBypass? = null,
  @SerialName("node_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nodeId: String? = null,
  @SerialName("_links")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val links: Links? = null,
  public val conditions: Conditions? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val rules: List<RepositoryRule>? = null,
  @SerialName("created_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val createdAt: Instant? = null,
  @SerialName("updated_at")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updatedAt: Instant? = null,
) {
  @Serializable(with = Conditions.Serializer::class)
  public sealed interface Conditions {
    @Serializable
    @JvmInline
    public value class CaseRepositoryRulesetConditions(
      public val `value`: RepositoryRulesetConditions,
    ) : Conditions

    @Serializable
    @JvmInline
    public value class CaseOrgRulesetConditions(
      public val `value`: OrgRulesetConditions,
    ) : Conditions

    public object Serializer : KSerializer<Conditions> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.RepositoryRuleset.Conditions", PolymorphicKind.SEALED) {
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

      override fun serialize(encoder: Encoder, `value`: Conditions) {
        when(value) {
          is CaseRepositoryRulesetConditions -> encoder.encodeSerializableValue(RepositoryRulesetConditions.serializer(), value.value)
          is CaseOrgRulesetConditions -> encoder.encodeSerializableValue(OrgRulesetConditions.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class CurrentUserCanBypass(
    public val `value`: String,
  ) {
    @SerialName("always")
    Always("always"),
    @SerialName("pull_requests_only")
    PullRequestsOnly("pull_requests_only"),
    @SerialName("never")
    Never("never"),
    @SerialName("exempt")
    Exempt("exempt"),
    ;
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Links(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val self: Self? = null,
    public val html: Html? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Html(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val href: String? = null,
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Self(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val href: String? = null,
    )
  }

  @Serializable
  public enum class SourceType {
    Repository,
    Organization,
    Enterprise,
  }

  @Serializable
  public enum class Target(
    public val `value`: String,
  ) {
    @SerialName("branch")
    Branch("branch"),
    @SerialName("tag")
    Tag("tag"),
    @SerialName("push")
    Push("push"),
    @SerialName("repository")
    Repository("repository"),
    ;
  }
}
