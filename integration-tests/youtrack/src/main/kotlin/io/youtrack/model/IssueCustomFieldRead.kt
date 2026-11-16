package io.youtrack.model

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

@Serializable(with = IssueCustomFieldRead.Serializer::class)
public sealed interface IssueCustomFieldRead {
  public val id: String?

  public val name: String?

  public val projectCustomField: ProjectCustomFieldRead?

  @SerialName("IssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "IssueCustomField")
  }

  @SerialName("PeriodIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = PeriodIssueCustomField.Serializer::class)
  public data class PeriodIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: PeriodValue? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<PeriodIssueCustomField>(PeriodIssueCustomField.generatedSerializer(), "${'$'}type", "PeriodIssueCustomField")
  }

  @SerialName("SimpleIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SimpleIssueCustomField.Serializer::class)
  public data class SimpleIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SimpleIssueCustomField>(SimpleIssueCustomField.generatedSerializer(), "${'$'}type", "SimpleIssueCustomField")
  }

  @SerialName("DateIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = DateIssueCustomField.Serializer::class)
  public data class DateIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<DateIssueCustomField>(DateIssueCustomField.generatedSerializer(), "${'$'}type", "DateIssueCustomField")
  }

  @SerialName("SingleValueIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleValueIssueCustomField.Serializer::class)
  public data class SingleValueIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SingleValueIssueCustomField>(SingleValueIssueCustomField.generatedSerializer(), "${'$'}type", "SingleValueIssueCustomField")
  }

  @SerialName("StateIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateIssueCustomField.Serializer::class)
  public data class StateIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.StateBundleElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<StateIssueCustomField>(StateIssueCustomField.generatedSerializer(), "${'$'}type", "StateIssueCustomField")
  }

  @SerialName("SingleBuildIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleBuildIssueCustomField.Serializer::class)
  public data class SingleBuildIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.BuildBundleElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SingleBuildIssueCustomField>(SingleBuildIssueCustomField.generatedSerializer(), "${'$'}type", "SingleBuildIssueCustomField")
  }

  @SerialName("SingleUserIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleUserIssueCustomField.Serializer::class)
  public data class SingleUserIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: UserRead? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SingleUserIssueCustomField>(SingleUserIssueCustomField.generatedSerializer(), "${'$'}type", "SingleUserIssueCustomField")
  }

  @SerialName("SingleGroupIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleGroupIssueCustomField.Serializer::class)
  public data class SingleGroupIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: UserGroupRead? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SingleGroupIssueCustomField>(SingleGroupIssueCustomField.generatedSerializer(), "${'$'}type", "SingleGroupIssueCustomField")
  }

  @SerialName("SingleVersionIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleVersionIssueCustomField.Serializer::class)
  public data class SingleVersionIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.VersionBundleElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SingleVersionIssueCustomField>(SingleVersionIssueCustomField.generatedSerializer(), "${'$'}type", "SingleVersionIssueCustomField")
  }

  @SerialName("SingleOwnedIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleOwnedIssueCustomField.Serializer::class)
  public data class SingleOwnedIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.OwnedBundleElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SingleOwnedIssueCustomField>(SingleOwnedIssueCustomField.generatedSerializer(), "${'$'}type", "SingleOwnedIssueCustomField")
  }

  @SerialName("SingleEnumIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleEnumIssueCustomField.Serializer::class)
  public data class SingleEnumIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.EnumBundleElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<SingleEnumIssueCustomField>(SingleEnumIssueCustomField.generatedSerializer(), "${'$'}type", "SingleEnumIssueCustomField")
  }

  @SerialName("MultiValueIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiValueIssueCustomField.Serializer::class)
  public data class MultiValueIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<MultiValueIssueCustomField>(MultiValueIssueCustomField.generatedSerializer(), "${'$'}type", "MultiValueIssueCustomField")
  }

  @SerialName("MultiBuildIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiBuildIssueCustomField.Serializer::class)
  public data class MultiBuildIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.BuildBundleElement>? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<MultiBuildIssueCustomField>(MultiBuildIssueCustomField.generatedSerializer(), "${'$'}type", "MultiBuildIssueCustomField")
  }

  @SerialName("MultiGroupIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiGroupIssueCustomField.Serializer::class)
  public data class MultiGroupIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserGroupRead>? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<MultiGroupIssueCustomField>(MultiGroupIssueCustomField.generatedSerializer(), "${'$'}type", "MultiGroupIssueCustomField")
  }

  @SerialName("MultiVersionIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiVersionIssueCustomField.Serializer::class)
  public data class MultiVersionIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.VersionBundleElement>? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<MultiVersionIssueCustomField>(MultiVersionIssueCustomField.generatedSerializer(), "${'$'}type", "MultiVersionIssueCustomField")
  }

  @SerialName("MultiOwnedIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiOwnedIssueCustomField.Serializer::class)
  public data class MultiOwnedIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<MultiOwnedIssueCustomField>(MultiOwnedIssueCustomField.generatedSerializer(), "${'$'}type", "MultiOwnedIssueCustomField")
  }

  @SerialName("MultiEnumIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiEnumIssueCustomField.Serializer::class)
  public data class MultiEnumIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.EnumBundleElement>? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<MultiEnumIssueCustomField>(MultiEnumIssueCustomField.generatedSerializer(), "${'$'}type", "MultiEnumIssueCustomField")
  }

  @SerialName("MultiUserIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiUserIssueCustomField.Serializer::class)
  public data class MultiUserIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserRead>? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<MultiUserIssueCustomField>(MultiUserIssueCustomField.generatedSerializer(), "${'$'}type", "MultiUserIssueCustomField")
  }

  @SerialName("StateMachineIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateMachineIssueCustomField.Serializer::class)
  public data class StateMachineIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val event: Event? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val possibleEvents: List<Event>? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<StateMachineIssueCustomField>(StateMachineIssueCustomField.generatedSerializer(), "${'$'}type", "StateMachineIssueCustomField")
  }

  @SerialName("TextIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TextIssueCustomField.Serializer::class)
  public data class TextIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: TextFieldValue? = null,
  ) : IssueCustomFieldRead {
    public object Serializer : TaggedCaseSerializer<TextIssueCustomField>(TextIssueCustomField.generatedSerializer(), "${'$'}type", "TextIssueCustomField")
  }

  public object Serializer : JsonContentPolymorphicSerializer<IssueCustomFieldRead>(IssueCustomFieldRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<IssueCustomFieldRead> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "IssueCustomField" -> Default.serializer()
      "PeriodIssueCustomField" -> PeriodIssueCustomField.serializer()
      "SimpleIssueCustomField" -> SimpleIssueCustomField.serializer()
      "DateIssueCustomField" -> DateIssueCustomField.serializer()
      "SingleValueIssueCustomField" -> SingleValueIssueCustomField.serializer()
      "StateIssueCustomField" -> StateIssueCustomField.serializer()
      "SingleBuildIssueCustomField" -> SingleBuildIssueCustomField.serializer()
      "SingleUserIssueCustomField" -> SingleUserIssueCustomField.serializer()
      "SingleGroupIssueCustomField" -> SingleGroupIssueCustomField.serializer()
      "SingleVersionIssueCustomField" -> SingleVersionIssueCustomField.serializer()
      "SingleOwnedIssueCustomField" -> SingleOwnedIssueCustomField.serializer()
      "SingleEnumIssueCustomField" -> SingleEnumIssueCustomField.serializer()
      "MultiValueIssueCustomField" -> MultiValueIssueCustomField.serializer()
      "MultiBuildIssueCustomField" -> MultiBuildIssueCustomField.serializer()
      "MultiGroupIssueCustomField" -> MultiGroupIssueCustomField.serializer()
      "MultiVersionIssueCustomField" -> MultiVersionIssueCustomField.serializer()
      "MultiOwnedIssueCustomField" -> MultiOwnedIssueCustomField.serializer()
      "MultiEnumIssueCustomField" -> MultiEnumIssueCustomField.serializer()
      "MultiUserIssueCustomField" -> MultiUserIssueCustomField.serializer()
      "StateMachineIssueCustomField" -> StateMachineIssueCustomField.serializer()
      "TextIssueCustomField" -> TextIssueCustomField.serializer()
      else -> throw SerializationException("'${'$'}type' of IssueCustomFieldRead selects none of IssueCustomField, PeriodIssueCustomField, SimpleIssueCustomField, DateIssueCustomField, SingleValueIssueCustomField, StateIssueCustomField, SingleBuildIssueCustomField, SingleUserIssueCustomField, SingleGroupIssueCustomField, SingleVersionIssueCustomField, SingleOwnedIssueCustomField, SingleEnumIssueCustomField, MultiValueIssueCustomField, MultiBuildIssueCustomField, MultiGroupIssueCustomField, MultiVersionIssueCustomField, MultiOwnedIssueCustomField, MultiEnumIssueCustomField, MultiUserIssueCustomField, StateMachineIssueCustomField, TextIssueCustomField: " + tag)
    }
  }
}
