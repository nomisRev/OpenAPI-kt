package io.youtrack.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueCustomFieldWrite {
  @SerialName("IssueCustomField")
  @Serializable
  public data object Default : IssueCustomFieldWrite

  @SerialName("PeriodIssueCustomField")
  @Serializable
  public class PeriodIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SimpleIssueCustomField")
  @Serializable
  public class SimpleIssueCustomField() : IssueCustomFieldWrite

  @SerialName("DateIssueCustomField")
  @Serializable
  public class DateIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SingleValueIssueCustomField")
  @Serializable
  public class SingleValueIssueCustomField() : IssueCustomFieldWrite

  @SerialName("StateIssueCustomField")
  @Serializable
  public class StateIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SingleBuildIssueCustomField")
  @Serializable
  public class SingleBuildIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SingleUserIssueCustomField")
  @Serializable
  public class SingleUserIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SingleGroupIssueCustomField")
  @Serializable
  public class SingleGroupIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SingleVersionIssueCustomField")
  @Serializable
  public class SingleVersionIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SingleOwnedIssueCustomField")
  @Serializable
  public class SingleOwnedIssueCustomField() : IssueCustomFieldWrite

  @SerialName("SingleEnumIssueCustomField")
  @Serializable
  public class SingleEnumIssueCustomField() : IssueCustomFieldWrite

  @SerialName("MultiValueIssueCustomField")
  @Serializable
  public class MultiValueIssueCustomField() : IssueCustomFieldWrite

  @SerialName("MultiBuildIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiBuildIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiGroupIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiGroupIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserGroupWrite>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiVersionIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiVersionIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiOwnedIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiOwnedIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiEnumIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiEnumIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiUserIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiUserIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserWrite>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("StateMachineIssueCustomField")
  @Serializable
  public class StateMachineIssueCustomField() : IssueCustomFieldWrite

  @SerialName("TextIssueCustomField")
  @Serializable
  public class TextIssueCustomField() : IssueCustomFieldWrite
}
