package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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
 * Conditions for an organization ruleset.
 * The branch and tag rulesets conditions object should contain both `repository_name` and `ref_name` properties, or both `repository_id` and `ref_name` properties, or both `repository_property` and `ref_name` properties.
 * The push rulesets conditions object does not require the `ref_name` property.
 * For repository policy rulesets, the conditions object should only contain the `repository_name`, the `repository_id`, or the `repository_property`.
 */
@Serializable(with = OrgRulesetConditionsRead.Serializer::class)
public sealed interface OrgRulesetConditionsRead {
  /**
   * Parameters for a repository ruleset ref name condition
   */
  @Serializable
  public data class RepositoryNameAndRefName(
    @SerialName("ref_name")
    public val refName: RefName? = null,
    @SerialName("repository_name")
    public val repositoryName: RepositoryName? = null,
  ) : OrgRulesetConditionsRead {
    @Serializable
    public data class RefName(
      public val include: List<String>? = null,
      public val exclude: List<String>? = null,
    )

    @Serializable
    public data class RepositoryName(
      public val include: List<String>? = null,
      public val exclude: List<String>? = null,
      public val `protected`: Boolean? = null,
    )
  }

  /**
   * Parameters for a repository ruleset ref name condition
   */
  @Serializable
  public data class RepositoryIdAndRefName(
    @SerialName("ref_name")
    public val refName: RefName? = null,
    @SerialName("repository_id")
    public val repositoryId: RepositoryId? = null,
  ) : OrgRulesetConditionsRead {
    @Serializable
    public data class RefName(
      public val include: List<String>? = null,
      public val exclude: List<String>? = null,
    )

    @JvmInline
    @Serializable
    public value class RepositoryId(
      @SerialName("repository_ids")
      public val repositoryIds: List<Long>? = null,
    )
  }

  /**
   * Parameters for a repository ruleset ref name condition
   */
  @Serializable
  public data class RepositoryPropertyAndRefName(
    @SerialName("ref_name")
    public val refName: RefName? = null,
    @SerialName("repository_property")
    public val repositoryProperty: RepositoryProperty? = null,
  ) : OrgRulesetConditionsRead {
    @Serializable
    public data class RefName(
      public val include: List<String>? = null,
      public val exclude: List<String>? = null,
    )

    @Serializable
    public data class RepositoryProperty(
      public val include: List<RepositoryRulesetConditionsRepositoryPropertySpecRead>? = null,
      public val exclude: List<RepositoryRulesetConditionsRepositoryPropertySpecRead>? = null,
    )
  }

  public object Serializer : KSerializer<OrgRulesetConditionsRead> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.model.OrgRulesetConditionsRead", PolymorphicKind.SEALED) {
      element("RepositoryNameAndRefName", RepositoryNameAndRefName.serializer().descriptor)
      element("RepositoryIdAndRefName", RepositoryIdAndRefName.serializer().descriptor)
      element("RepositoryPropertyAndRefName", RepositoryPropertyAndRefName.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): OrgRulesetConditionsRead {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        RepositoryNameAndRefName::class to { decodeFromJsonElement(RepositoryNameAndRefName.serializer(), it) },
        RepositoryIdAndRefName::class to { decodeFromJsonElement(RepositoryIdAndRefName.serializer(), it) },
        RepositoryPropertyAndRefName::class to { decodeFromJsonElement(RepositoryPropertyAndRefName.serializer(), it) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: OrgRulesetConditionsRead) {
      when(value) {
        is RepositoryNameAndRefName -> encoder.encodeSerializableValue(RepositoryNameAndRefName.serializer(), value)
        is RepositoryIdAndRefName -> encoder.encodeSerializableValue(RepositoryIdAndRefName.serializer(), value)
        is RepositoryPropertyAndRefName -> encoder.encodeSerializableValue(RepositoryPropertyAndRefName.serializer(), value)
      }
    }
  }
}
