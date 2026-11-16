package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = ProjectCustomFieldWrite.Serializer::class)
public sealed interface ProjectCustomFieldWrite {
  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val ordinal: Int?

  public val isPublic: Boolean?

  public val condition: CustomFieldConditionWrite?

  @SerialName("ProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "ProjectCustomField")
  }

  @SerialName("GroupProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = GroupProjectCustomField.Serializer::class)
  public data class GroupProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<UserGroupWrite>? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<GroupProjectCustomField>(GroupProjectCustomField.generatedSerializer(), "${'$'}type", "GroupProjectCustomField")
  }

  @SerialName("BundleProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BundleProjectCustomField.Serializer::class)
  public data class BundleProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<BundleProjectCustomField>(BundleProjectCustomField.generatedSerializer(), "${'$'}type", "BundleProjectCustomField")
  }

  @SerialName("UserProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = UserProjectCustomField.Serializer::class)
  public data class UserProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.UserBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<UserWrite>? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<UserProjectCustomField>(UserProjectCustomField.generatedSerializer(), "${'$'}type", "UserProjectCustomField")
  }

  @SerialName("BuildProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = BuildProjectCustomField.Serializer::class)
  public data class BuildProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.BuildBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<BuildProjectCustomField>(BuildProjectCustomField.generatedSerializer(), "${'$'}type", "BuildProjectCustomField")
  }

  @SerialName("StateProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateProjectCustomField.Serializer::class)
  public data class StateProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.StateBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.StateBundleElement>? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<StateProjectCustomField>(StateProjectCustomField.generatedSerializer(), "${'$'}type", "StateProjectCustomField")
  }

  @SerialName("VersionProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VersionProjectCustomField.Serializer::class)
  public data class VersionProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.VersionBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<VersionProjectCustomField>(VersionProjectCustomField.generatedSerializer(), "${'$'}type", "VersionProjectCustomField")
  }

  @SerialName("OwnedProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = OwnedProjectCustomField.Serializer::class)
  public data class OwnedProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.OwnedBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<OwnedProjectCustomField>(OwnedProjectCustomField.generatedSerializer(), "${'$'}type", "OwnedProjectCustomField")
  }

  @SerialName("EnumProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = EnumProjectCustomField.Serializer::class)
  public data class EnumProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleWrite.EnumBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<EnumProjectCustomField>(EnumProjectCustomField.generatedSerializer(), "${'$'}type", "EnumProjectCustomField")
  }

  @SerialName("SimpleProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SimpleProjectCustomField.Serializer::class)
  public data class SimpleProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SimpleProjectCustomField>(SimpleProjectCustomField.generatedSerializer(), "${'$'}type", "SimpleProjectCustomField")
  }

  @SerialName("PeriodProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = PeriodProjectCustomField.Serializer::class)
  public data class PeriodProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<PeriodProjectCustomField>(PeriodProjectCustomField.generatedSerializer(), "${'$'}type", "PeriodProjectCustomField")
  }

  @SerialName("TextProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TextProjectCustomField.Serializer::class)
  public data class TextProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionWrite? = null,
  ) : ProjectCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<TextProjectCustomField>(TextProjectCustomField.generatedSerializer(), "${'$'}type", "TextProjectCustomField")
  }

  public object Serializer : JsonContentPolymorphicSerializer<ProjectCustomFieldWrite>(ProjectCustomFieldWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ProjectCustomFieldWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "ProjectCustomField" -> Default.serializer()
      "GroupProjectCustomField" -> GroupProjectCustomField.serializer()
      "BundleProjectCustomField" -> BundleProjectCustomField.serializer()
      "UserProjectCustomField" -> UserProjectCustomField.serializer()
      "BuildProjectCustomField" -> BuildProjectCustomField.serializer()
      "StateProjectCustomField" -> StateProjectCustomField.serializer()
      "VersionProjectCustomField" -> VersionProjectCustomField.serializer()
      "OwnedProjectCustomField" -> OwnedProjectCustomField.serializer()
      "EnumProjectCustomField" -> EnumProjectCustomField.serializer()
      "SimpleProjectCustomField" -> SimpleProjectCustomField.serializer()
      "PeriodProjectCustomField" -> PeriodProjectCustomField.serializer()
      "TextProjectCustomField" -> TextProjectCustomField.serializer()
      else -> throw SerializationException("'${'$'}type' of ProjectCustomFieldWrite selects none of ProjectCustomField, GroupProjectCustomField, BundleProjectCustomField, UserProjectCustomField, BuildProjectCustomField, StateProjectCustomField, VersionProjectCustomField, OwnedProjectCustomField, EnumProjectCustomField, SimpleProjectCustomField, PeriodProjectCustomField, TextProjectCustomField: " + tag)
    }
  }
}
