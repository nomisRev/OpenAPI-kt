package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
  ) : IssueCustomFieldRead

  @SerialName("PeriodIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class PeriodIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: PeriodValue? = null,
  ) : IssueCustomFieldRead

  @SerialName("SimpleIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SimpleIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("DateIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class DateIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleValueIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleValueIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("StateIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class StateIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.StateBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleBuildIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleBuildIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.BuildBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleUserIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleUserIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: UserRead? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleGroupIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleGroupIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: UserGroupRead? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleVersionIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleVersionIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.VersionBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleOwnedIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleOwnedIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.OwnedBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("SingleEnumIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleEnumIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: BundleElementRead.EnumBundleElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiValueIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiValueIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: JsonElement? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiBuildIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiBuildIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.BuildBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiGroupIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiGroupIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserGroupRead>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiVersionIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiVersionIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.VersionBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiOwnedIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiOwnedIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiEnumIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiEnumIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<BundleElementRead.EnumBundleElement>? = null,
  ) : IssueCustomFieldRead

  @SerialName("MultiUserIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiUserIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: List<UserRead>? = null,
  ) : IssueCustomFieldRead

  @SerialName("StateMachineIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : IssueCustomFieldRead

  @SerialName("TextIssueCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TextIssueCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val name: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val projectCustomField: ProjectCustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val `value`: TextFieldValue? = null,
  ) : IssueCustomFieldRead
}
