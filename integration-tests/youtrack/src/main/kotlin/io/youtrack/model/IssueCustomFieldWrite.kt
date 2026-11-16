package io.youtrack.model

import kotlin.OptIn
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

@Serializable(with = IssueCustomFieldWrite.Serializer::class)
public sealed interface IssueCustomFieldWrite {
  @SerialName("IssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
  public data object Default : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "IssueCustomField")
  }

  @SerialName("PeriodIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = PeriodIssueCustomField.Serializer::class)
  public class PeriodIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<PeriodIssueCustomField>(PeriodIssueCustomField.generatedSerializer(), "${'$'}type", "PeriodIssueCustomField")
  }

  @SerialName("SimpleIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SimpleIssueCustomField.Serializer::class)
  public class SimpleIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SimpleIssueCustomField>(SimpleIssueCustomField.generatedSerializer(), "${'$'}type", "SimpleIssueCustomField")
  }

  @SerialName("DateIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = DateIssueCustomField.Serializer::class)
  public class DateIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<DateIssueCustomField>(DateIssueCustomField.generatedSerializer(), "${'$'}type", "DateIssueCustomField")
  }

  @SerialName("SingleValueIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleValueIssueCustomField.Serializer::class)
  public class SingleValueIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SingleValueIssueCustomField>(SingleValueIssueCustomField.generatedSerializer(), "${'$'}type", "SingleValueIssueCustomField")
  }

  @SerialName("StateIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateIssueCustomField.Serializer::class)
  public class StateIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<StateIssueCustomField>(StateIssueCustomField.generatedSerializer(), "${'$'}type", "StateIssueCustomField")
  }

  @SerialName("SingleBuildIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleBuildIssueCustomField.Serializer::class)
  public class SingleBuildIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SingleBuildIssueCustomField>(SingleBuildIssueCustomField.generatedSerializer(), "${'$'}type", "SingleBuildIssueCustomField")
  }

  @SerialName("SingleUserIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleUserIssueCustomField.Serializer::class)
  public class SingleUserIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SingleUserIssueCustomField>(SingleUserIssueCustomField.generatedSerializer(), "${'$'}type", "SingleUserIssueCustomField")
  }

  @SerialName("SingleGroupIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleGroupIssueCustomField.Serializer::class)
  public class SingleGroupIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SingleGroupIssueCustomField>(SingleGroupIssueCustomField.generatedSerializer(), "${'$'}type", "SingleGroupIssueCustomField")
  }

  @SerialName("SingleVersionIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleVersionIssueCustomField.Serializer::class)
  public class SingleVersionIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SingleVersionIssueCustomField>(SingleVersionIssueCustomField.generatedSerializer(), "${'$'}type", "SingleVersionIssueCustomField")
  }

  @SerialName("SingleOwnedIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleOwnedIssueCustomField.Serializer::class)
  public class SingleOwnedIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SingleOwnedIssueCustomField>(SingleOwnedIssueCustomField.generatedSerializer(), "${'$'}type", "SingleOwnedIssueCustomField")
  }

  @SerialName("SingleEnumIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleEnumIssueCustomField.Serializer::class)
  public class SingleEnumIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<SingleEnumIssueCustomField>(SingleEnumIssueCustomField.generatedSerializer(), "${'$'}type", "SingleEnumIssueCustomField")
  }

  @SerialName("MultiValueIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiValueIssueCustomField.Serializer::class)
  public class MultiValueIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<MultiValueIssueCustomField>(MultiValueIssueCustomField.generatedSerializer(), "${'$'}type", "MultiValueIssueCustomField")
  }

  @SerialName("MultiBuildIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiBuildIssueCustomField.Serializer::class)
  public data class MultiBuildIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<MultiBuildIssueCustomField>(MultiBuildIssueCustomField.generatedSerializer(), "${'$'}type", "MultiBuildIssueCustomField")
  }

  @SerialName("MultiGroupIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiGroupIssueCustomField.Serializer::class)
  public data class MultiGroupIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserGroupWrite>? = null,
  ) : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<MultiGroupIssueCustomField>(MultiGroupIssueCustomField.generatedSerializer(), "${'$'}type", "MultiGroupIssueCustomField")
  }

  @SerialName("MultiVersionIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiVersionIssueCustomField.Serializer::class)
  public data class MultiVersionIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<MultiVersionIssueCustomField>(MultiVersionIssueCustomField.generatedSerializer(), "${'$'}type", "MultiVersionIssueCustomField")
  }

  @SerialName("MultiOwnedIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiOwnedIssueCustomField.Serializer::class)
  public data class MultiOwnedIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<MultiOwnedIssueCustomField>(MultiOwnedIssueCustomField.generatedSerializer(), "${'$'}type", "MultiOwnedIssueCustomField")
  }

  @SerialName("MultiEnumIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiEnumIssueCustomField.Serializer::class)
  public data class MultiEnumIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<MultiEnumIssueCustomField>(MultiEnumIssueCustomField.generatedSerializer(), "${'$'}type", "MultiEnumIssueCustomField")
  }

  @SerialName("MultiUserIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiUserIssueCustomField.Serializer::class)
  public data class MultiUserIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserWrite>? = null,
  ) : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<MultiUserIssueCustomField>(MultiUserIssueCustomField.generatedSerializer(), "${'$'}type", "MultiUserIssueCustomField")
  }

  @SerialName("StateMachineIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = StateMachineIssueCustomField.Serializer::class)
  public class StateMachineIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<StateMachineIssueCustomField>(StateMachineIssueCustomField.generatedSerializer(), "${'$'}type", "StateMachineIssueCustomField")
  }

  @SerialName("TextIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TextIssueCustomField.Serializer::class)
  public class TextIssueCustomField() : IssueCustomFieldWrite {
    public object Serializer : TaggedCaseSerializer<TextIssueCustomField>(TextIssueCustomField.generatedSerializer(), "${'$'}type", "TextIssueCustomField")
  }

  public object Serializer : JsonContentPolymorphicSerializer<IssueCustomFieldWrite>(IssueCustomFieldWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<IssueCustomFieldWrite> = when (val tag = element.discriminatorValue("${'$'}type")) {
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
      else -> throw SerializationException("'${'$'}type' of IssueCustomFieldWrite selects none of IssueCustomField, PeriodIssueCustomField, SimpleIssueCustomField, DateIssueCustomField, SingleValueIssueCustomField, StateIssueCustomField, SingleBuildIssueCustomField, SingleUserIssueCustomField, SingleGroupIssueCustomField, SingleVersionIssueCustomField, SingleOwnedIssueCustomField, SingleEnumIssueCustomField, MultiValueIssueCustomField, MultiBuildIssueCustomField, MultiGroupIssueCustomField, MultiVersionIssueCustomField, MultiOwnedIssueCustomField, MultiEnumIssueCustomField, MultiUserIssueCustomField, StateMachineIssueCustomField, TextIssueCustomField: " + tag)
    }
  }
}
