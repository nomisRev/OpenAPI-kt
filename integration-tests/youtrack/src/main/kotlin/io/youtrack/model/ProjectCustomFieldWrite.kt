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
public sealed interface ProjectCustomFieldWrite {
  public val canBeEmpty: Boolean?

  public val emptyFieldText: String?

  public val ordinal: Int?

  public val isPublic: Boolean?

  public val condition: CustomFieldConditionWrite?

  @SerialName("ProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("GroupProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("BundleProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("UserProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("BuildProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("StateProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("VersionProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("OwnedProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("EnumProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("SimpleProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("PeriodProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite

  @SerialName("TextProjectCustomField")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
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
  ) : ProjectCustomFieldWrite
}
