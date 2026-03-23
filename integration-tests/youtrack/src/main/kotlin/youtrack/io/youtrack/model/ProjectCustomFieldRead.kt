package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface ProjectCustomFieldRead {
  public val id: String?

  public val `field`: CustomFieldRead?

  public val project: IssueFolderRead.Project?

  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val ordinal: Int?

  public val isPublic: Boolean?

  public val hasRunningJob: Boolean?

  public val condition: CustomFieldConditionRead?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("GroupProjectCustomField")
  @Serializable
  public data class GroupProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
    public val defaultValues: List<UserGroupRead>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("BundleProjectCustomField")
  @Serializable
  public data class BundleProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("UserProjectCustomField")
  @Serializable
  public data class UserProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
    public val bundle: BundleRead.UserBundle? = null,
    public val defaultValues: List<UserRead>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("BuildProjectCustomField")
  @Serializable
  public data class BuildProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
    public val bundle: BundleRead.BuildBundle? = null,
    public val defaultValues: List<BundleElementRead.BuildBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("StateProjectCustomField")
  @Serializable
  public data class StateProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
    public val bundle: BundleRead.StateBundle? = null,
    public val defaultValues: List<BundleElementRead.StateBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("VersionProjectCustomField")
  @Serializable
  public data class VersionProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
    public val bundle: BundleRead.VersionBundle? = null,
    public val defaultValues: List<BundleElementRead.VersionBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("OwnedProjectCustomField")
  @Serializable
  public data class OwnedProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
    public val bundle: BundleRead.OwnedBundle? = null,
    public val defaultValues: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("EnumProjectCustomField")
  @Serializable
  public data class EnumProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
    public val bundle: BundleRead.EnumBundle? = null,
    public val defaultValues: List<BundleElementRead.EnumBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("SimpleProjectCustomField")
  @Serializable
  public data class SimpleProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("PeriodProjectCustomField")
  @Serializable
  public data class PeriodProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("TextProjectCustomField")
  @Serializable
  public data class TextProjectCustomField(
    override val id: String? = null,
    override val `field`: CustomFieldRead? = null,
    override val project: IssueFolderRead.Project? = null,
    override val canBeEmpty: Boolean? = null,
    override val emptyFieldText: String? = null,
    override val ordinal: Int? = null,
    override val isPublic: Boolean? = null,
    override val hasRunningJob: Boolean? = null,
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead
}
