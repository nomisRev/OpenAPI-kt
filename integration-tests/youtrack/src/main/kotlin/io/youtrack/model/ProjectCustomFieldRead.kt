package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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

  @SerialName("ProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("GroupProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class GroupProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<UserGroupRead>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("BundleProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BundleProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("UserProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UserProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.UserBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<UserRead>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("BuildProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class BuildProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.BuildBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.BuildBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("StateProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class StateProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.StateBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.StateBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("VersionProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VersionProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.VersionBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.VersionBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("OwnedProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class OwnedProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.OwnedBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.OwnedBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("EnumProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class EnumProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val bundle: BundleRead.EnumBundle? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val defaultValues: List<BundleElementRead.EnumBundleElement>? = null,
  ) : ProjectCustomFieldRead

  @SerialName("SimpleProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SimpleProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("PeriodProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class PeriodProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead

  @SerialName("TextProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TextProjectCustomField(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: CustomFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val project: IssueFolderRead.Project? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val canBeEmpty: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val emptyFieldText: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val ordinal: Int? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val isPublic: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val hasRunningJob: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val condition: CustomFieldConditionRead? = null,
  ) : ProjectCustomFieldRead
}
