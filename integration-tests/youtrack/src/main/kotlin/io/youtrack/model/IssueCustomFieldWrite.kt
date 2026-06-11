package io.youtrack.model

import kotlin.OptIn
import kotlin.collections.List
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
  public class Default() : IssueCustomFieldWrite

  @SerialName("PeriodIssueCustomField")
  @Serializable
  public data class PeriodIssueCustomField(
    public val `value`: PeriodValueWrite? = null,
  ) : IssueCustomFieldWrite

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
  public data class StateIssueCustomField(
    public val `value`: BundleElementWrite.StateBundleElement? = null,
  ) : IssueCustomFieldWrite

  @SerialName("SingleBuildIssueCustomField")
  @Serializable
  public data class SingleBuildIssueCustomField(
    public val `value`: BundleElementWrite.BuildBundleElement? = null,
  ) : IssueCustomFieldWrite

  @SerialName("SingleUserIssueCustomField")
  @Serializable
  public data class SingleUserIssueCustomField(
    public val `value`: UserWrite? = null,
  ) : IssueCustomFieldWrite

  @SerialName("SingleGroupIssueCustomField")
  @Serializable
  public data class SingleGroupIssueCustomField(
    public val `value`: UserGroupWrite? = null,
  ) : IssueCustomFieldWrite

  @SerialName("SingleVersionIssueCustomField")
  @Serializable
  public data class SingleVersionIssueCustomField(
    public val `value`: BundleElementWrite.VersionBundleElement? = null,
  ) : IssueCustomFieldWrite

  @SerialName("SingleOwnedIssueCustomField")
  @Serializable
  public data class SingleOwnedIssueCustomField(
    public val `value`: BundleElementWrite.OwnedBundleElement? = null,
  ) : IssueCustomFieldWrite

  @SerialName("SingleEnumIssueCustomField")
  @Serializable
  public data class SingleEnumIssueCustomField(
    public val `value`: BundleElementWrite.EnumBundleElement? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiValueIssueCustomField")
  @Serializable
  public class MultiValueIssueCustomField() : IssueCustomFieldWrite

  @SerialName("MultiBuildIssueCustomField")
  @Serializable
  public data class MultiBuildIssueCustomField(
    public val `value`: List<BundleElementWrite.BuildBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiGroupIssueCustomField")
  @Serializable
  public data class MultiGroupIssueCustomField(
    public val `value`: List<UserGroupWrite>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiVersionIssueCustomField")
  @Serializable
  public data class MultiVersionIssueCustomField(
    public val `value`: List<BundleElementWrite.VersionBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiOwnedIssueCustomField")
  @Serializable
  public data class MultiOwnedIssueCustomField(
    public val `value`: List<BundleElementWrite.OwnedBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiEnumIssueCustomField")
  @Serializable
  public data class MultiEnumIssueCustomField(
    public val `value`: List<BundleElementWrite.EnumBundleElement>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("MultiUserIssueCustomField")
  @Serializable
  public data class MultiUserIssueCustomField(
    public val `value`: List<UserWrite>? = null,
  ) : IssueCustomFieldWrite

  @SerialName("StateMachineIssueCustomField")
  @Serializable
  public class StateMachineIssueCustomField() : IssueCustomFieldWrite

  @SerialName("TextIssueCustomField")
  @Serializable
  public data class TextIssueCustomField(
    public val `value`: TextFieldValueWrite? = null,
  ) : IssueCustomFieldWrite
}
