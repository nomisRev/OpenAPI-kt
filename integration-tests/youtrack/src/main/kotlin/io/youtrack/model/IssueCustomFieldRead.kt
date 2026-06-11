package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface IssueCustomFieldRead {
  public val id: String?

  public val name: String?

  public val projectCustomField: ProjectCustomFieldRead?

  @SerialName("IssueCustomField")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
  ) : IssueCustomFieldRead

  @SerialName("PeriodIssueCustomField")
  @Serializable
  public data class PeriodIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: PeriodValueRead? = null,
  ) : IssueCustomFieldRead

  @SerialName("SimpleIssueCustomField")
  @Serializable
  public data class SimpleIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("DateIssueCustomField")
  @Serializable
  public data class DateIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleValueIssueCustomField")
  @Serializable
  public data class SingleValueIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("StateIssueCustomField")
  @Serializable
  public data class StateIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: BundleElementRead.StateBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleBuildIssueCustomField")
  @Serializable
  public data class SingleBuildIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: BundleElementRead.BuildBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleUserIssueCustomField")
  @Serializable
  public data class SingleUserIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: UserRead? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleGroupIssueCustomField")
  @Serializable
  public data class SingleGroupIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: UserGroupRead? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleVersionIssueCustomField")
  @Serializable
  public data class SingleVersionIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: BundleElementRead.VersionBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleOwnedIssueCustomField")
  @Serializable
  public data class SingleOwnedIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: BundleElementRead.OwnedBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleEnumIssueCustomField")
  @Serializable
  public data class SingleEnumIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: BundleElementRead.EnumBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiValueIssueCustomField")
  @Serializable
  public data class MultiValueIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiBuildIssueCustomField")
  @Serializable
  public data class MultiBuildIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: List<BundleElementRead.BuildBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiGroupIssueCustomField")
  @Serializable
  public data class MultiGroupIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: List<UserGroupRead>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiVersionIssueCustomField")
  @Serializable
  public data class MultiVersionIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: List<BundleElementRead.VersionBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiOwnedIssueCustomField")
  @Serializable
  public data class MultiOwnedIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiEnumIssueCustomField")
  @Serializable
  public data class MultiEnumIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: List<BundleElementRead.EnumBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiUserIssueCustomField")
  @Serializable
  public data class MultiUserIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: List<UserRead>? = null,
  ) : IssueCustomFieldRead

  @SerialName("StateMachineIssueCustomField")
  @Serializable
  public data class StateMachineIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: JsonElement? = null,
    public val event: Event? = null,
    public val possibleEvents: List<Event>? = null,
  ) : IssueCustomFieldRead

  @SerialName("TextIssueCustomField")
  @Serializable
  public data class TextIssueCustomField(
    override val id: String? = null,
    override val name: String? = null,
    override val projectCustomField: ProjectCustomFieldRead? = null,
    public val `value`: TextFieldValueRead? = null,
  ) : IssueCustomFieldRead
}
